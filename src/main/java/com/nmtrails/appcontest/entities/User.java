package com.nmtrails.appcontest.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "role")
    private EnumRole role;

    @NotBlank
    @Column(name = "username")
    private String username;

    @NotBlank
    @Column(name= "password")
    private String password;

    @NotBlank
    @Column(name= "email")
    private String email;

    private LocalDate userJoinDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Trail> toHikeList = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Trail> hikedList = new HashSet<>();

    public User() {
        this.role = EnumRole.ROLE_USER;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = EnumRole.ROLE_USER;
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

    public Set<Trail> getToHikeList() {
        return toHikeList;
    }

    public void addTrailToHikeList(Trail trail) {

        if (toHikeList.contains(trail)) {
            // TODO - add logging (investigate log4j vulnerability status)
            System.out.println("Trail already in wish list");
            throw new IllegalArgumentException();
        }
        toHikeList.add(trail);
    }

    public boolean hasTrailInToHikeList(Trail trail) {
        return toHikeList.contains(trail);
    }

    public void removeTrailFromToHikeList(Trail trail) {
        if (toHikeList.contains(trail)) toHikeList.remove(trail);
    }

    public void setToHikeList(Set<Trail> wishList) {
        this.toHikeList = wishList;
    }

    public void addTrailToHikedList(Trail trail) {

        if (hikedList.contains(trail)) throw new IllegalArgumentException();

        if (toHikeList.contains(trail)) {
            toHikeList.remove(trail);
        }

        hikedList.add(trail);

    }

    public void removeTrailFromHikedList(Trail trail) {
        if (hikedList.contains(trail)) hikedList.remove(trail);
    }

    public Set<Trail> getHikedList() {
        return hikedList;
    }

    public void setHikedList(Set<Trail> hikedList) {
        this.hikedList = hikedList;
    }
}
