package com.nmtrails.appcontest.controllers;

import com.nmtrails.appcontest.entities.Mail;
import com.nmtrails.appcontest.entities.User;
import com.nmtrails.appcontest.payload.requests.HandlePasswordResetRequest;
import com.nmtrails.appcontest.payload.requests.LostPasswordResetRequest;
import com.nmtrails.appcontest.payload.requests.ValidatePasswordResetTokenRequest;
import com.nmtrails.appcontest.payload.responses.MessageResponse;
import com.nmtrails.appcontest.security.JWT.JwtUtils;
import com.nmtrails.appcontest.services.MailService;
import com.nmtrails.appcontest.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
        String url = request.getScheme() + "://" + request.getServerName() + ":" + 3000;
        model.put("resetUrl", url + "/reset-password/" + user.getId() + "/" + token);
        mail.setModel(model);
        mailService.sendEmail(mail);

        log.info("User requested a password reset");

        return ResponseEntity.ok(new MessageResponse("You will receive an email to reset your password if this email exists" +
                " in our system."));
    }

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

    @PostMapping("/handle-password-reset")
    public ResponseEntity<?> handlePasswordReset(@Valid @RequestBody HandlePasswordResetRequest handlePassResetReq) {

            System.out.println("handle");

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

}