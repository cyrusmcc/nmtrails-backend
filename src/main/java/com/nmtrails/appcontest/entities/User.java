package com.nmtrails.appcontest.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private EnumRole role;

    @NotBlank
    @Column(name ="username")
    private String username;

    @NotBlank
    @Column(name="password")
    private String password;

    @NotBlank
    @Column(name="email")
    private String email;

    private LocalDate userJoinDate;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public EnumRole getRole() {
        return role;
    }

    public void setRole(EnumRole role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getUserJoinDate() {
        return userJoinDate;
    }

    public void setUserJoinDate(LocalDate userJoinDate) {
        this.userJoinDate = userJoinDate;
    }
}
