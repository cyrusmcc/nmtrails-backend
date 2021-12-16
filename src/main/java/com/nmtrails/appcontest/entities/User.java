package com.nmtrails.appcontest.entities;

import org.springframework.transaction.annotation.Transactional;

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

   // @ManyToMany(mappedBy = "wishListedUsers")
   @ManyToMany
    private Set<Trail> wishList = new HashSet<>();

   // @ManyToMany(mappedBy = "hikedListUsers")
    @ManyToMany
    private Set<Trail> hikedList = new HashSet<>();

    public User() {
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

    public Set<Trail> getWishList() {
        return wishList;
    }

    public void addTrailToWishList(Trail trail) {

        if (wishList.contains(trail)) {
            // TODO - add logging (investigate log4j vulnerability)
            System.out.println("Trail already in wish list");
            throw new IllegalArgumentException();
        }
        wishList.add(trail);
    }

    public boolean hasTrailInWishList(Trail trail) {
        return wishList.contains(trail);
    }

    public void removeTrailFromWishlist(Trail trail) {
        if (wishList.contains(trail)) wishList.remove(trail);
    }

    public void setWishList(Set<Trail> wishList) {
        this.wishList = wishList;
    }

    public Set<Trail> getHikedList() {
        return hikedList;
    }

    public void setHikedList(Set<Trail> hikedList) {
        this.hikedList = hikedList;
    }
}
