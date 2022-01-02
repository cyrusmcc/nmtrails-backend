package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.entities.Mail;
import com.nmtrails.appcontest.entities.User;
import com.nmtrails.appcontest.payload.requests.*;
import com.nmtrails.appcontest.payload.responses.MessageResponse;
import com.nmtrails.appcontest.security.JWT.JwtUtils;
import com.nmtrails.appcontest.services.MailService;
import com.nmtrails.appcontest.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/setting")
public class SettingsController {

    private UserService userService;

    private MailService mailService;

    private JwtUtils jwtUtils;

    @Value("${EMAIL_USER}")
    private String emailSendAdd;


    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);

    @Autowired
    public SettingsController(UserService userService, MailService mailService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.mailService = mailService;
        this.jwtUtils = jwtUtils;
    }


    /**
     * When user submits a lost password reset request, verify that provided email is valid
     * then generate a URL with an embedded JWT to user's email.
     * */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> requestLostPasswordReset (@Valid @RequestBody LostPasswordResetRequest resetRequest,
                                                       HttpServletRequest request) {

        if (!userService.existsByEmail(resetRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("You will receive an email to reset your password if this email exists" +
                            " in our system."));
        }

        User user = userService.findByEmail(resetRequest.getEmail());

        // Use password hash and join date, jd makes secret unique in case of password leak
        String userSecret = user.getPassword() + "-" + user.getUserJoinDate();

        String token = jwtUtils.generateTokenFromUsernameAndUserSecret(user.getUsername(), userSecret);

        Mail mail = new Mail();
        mail.setFrom(emailSendAdd);
        mail.setTo(user.getEmail());
        mail.setSubject("Password reset request");

        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("user", user);
        model.put("signature", "a signature here");
        // replace 3000 w/ request.getServerPort() when done testing;
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        model.put("resetUrl", url + "/reset-password/" + user.getId() + "/" + token);
        mail.setModel(model);
        mailService.sendEmail(mail);

        log.info("User requested a password reset");

        return ResponseEntity.ok(new MessageResponse("You will receive an email to reset your password if this email exists" +
                " in our system."));
    }

    /**
     * When a user receives and opens their JWT containing reset link, decrypt the JWT and verify that the
     * request is valid, then return status.
     * */
    @PostMapping("/validate-password-reset-token")
    public ResponseEntity<?> validatePasswordResetToken(@Valid @RequestBody ValidatePasswordResetTokenRequest request) {

        User user = userService.findById(request.getUserId());
        String userSecret = user.getPassword() + "-" + user.getUserJoinDate();

        if (jwtUtils.validateJwtToken(request.getToken(), userSecret))
            return ResponseEntity.ok(true);

        else return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Invalid password reset token, please try again"));

    }

    /**
     * When a reset password JWT has been verified, a change password form is presented. Upon receiving the
     * password change request containing the new password, check if new password id valid then update
     * user's password.
     * */
    @PostMapping("/handle-password-reset")
    public ResponseEntity<?> handlePasswordReset(@Valid @RequestBody HandlePasswordResetRequest handlePassResetReq) {

            if (!userService.existsById(handlePassResetReq.getUserId())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Invalid password reset request, please try again"));
            }

            User user = userService.findById(handlePassResetReq.getUserId());
            String userSecret = user.getPassword() + "-" + user.getUserJoinDate();

            if (jwtUtils.validateJwtToken(handlePassResetReq.getToken(), userSecret)) {

                userService.updatePassword(user, handlePassResetReq.getPassword());
                log.info("Handling user password reset");
                return ResponseEntity.ok(new MessageResponse("Password successfully changed."));

            }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("An issue was encountered while trying to reset your password."));
    }

    /**
     * When a wants to change password while logged in, verify that both current and new passwords
     * are valid and then update user's password.
     * */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePasswordRequest(@Valid @RequestBody PasswordChangeRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            if (request.getCurrentPassword() == request.getNewPassword()) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Current and new password must be different."));
            }

            User user = userService.findByUsername(authentication.getName());

            if (!userService.isValidPassword(user, request.getCurrentPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Invalid entry for current password"));
            }

            userService.updatePassword(user, request.getNewPassword());
            userService.save(user);

            return ResponseEntity.ok(new MessageResponse("Account password has been updated."));

        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error encountered while processing password reset," +
                        " please try again"));

    }

    /**
     * When a user requests an email change, generate a JWT embedded URL and send to user's newly provided email.
     * */
    @PostMapping("/email-change-request")
    public ResponseEntity<?> emailChangeRequest(@Valid @RequestBody EmailChangeRequest emailChangeRequest,
                                                HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {

            if (userService.existsByEmail(emailChangeRequest.getEmail())) {
                log.info("User attempted to change email but the email provided is already in use");
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email already in use"));
            }

            User user = userService.findByUsername(authentication.getName());

            if (!userService.isValidPassword(user, emailChangeRequest.getPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid password, please try again"));
            }

            String userSecret = user.getEmail() + "-" + user.getPassword();

            String token = jwtUtils.generateTokenFromUsernameAndUserSecretWithPayload(
                    user.getUsername(), userSecret, emailChangeRequest.getEmail());

            Mail mail = new Mail();
            mail.setFrom(emailSendAdd);
            mail.setTo(emailChangeRequest.getEmail());
            mail.setSubject("Change email request");

            Map<String, Object> model = new HashMap<>();
            model.put("token", token);
            model.put("user", user);
            model.put("signature", "a signature here");
            // replace 3000 w/ request.getServerPort() when done testing;
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            model.put("resetUrl", url + "/email-change-confirmation/" + user.getId() + "/" + token);
            mail.setModel(model);
            mailService.sendEmail(mail);

            log.info("User requested an email change");

            return ResponseEntity.ok(new MessageResponse("A confirmation email will be sent to your new address. Click" +
                    " the link provided to update your email."));

        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error encountered while processing email reset request," +
                        " please try again"));

    }

    /**
     * When user opens JWT embedded URL from the new email, decrypt the JWT and ensure that the request is valid,
     * then update the user's email.
     * */
    @PostMapping("/handle-email-change")
    public ResponseEntity<?> handleEmailChange(@Valid @RequestBody HandleChangeEmailRequest request) {

        if (!userService.existsById(request.getUserId()))
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Invalid email change request, please try again"));

        User user = userService.findById(request.getUserId());
        String userSecret = user.getEmail() + "-" + user.getPassword();

        if (jwtUtils.validateJwtToken(request.getToken(), userSecret)) {

            String newEmail = jwtUtils
                    .getClaimsFromJwtToken(request.getToken(), userSecret)
                    .get("newEmail")
                    .toString();

            user.setEmail(newEmail);
            userService.save(user);

            log.info("User successfully changed email");
            return ResponseEntity.ok(new MessageResponse("Email successfully changed."));
        }

        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Error encountered while processing email change," +
                        " please try again"));
    }
}