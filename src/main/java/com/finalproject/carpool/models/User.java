package com.finalproject.carpool.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;
    @JsonIgnore
    @Email
    @Column(name = "email",nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "is_banned")
    private boolean isBanned;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "rating")
    private double rating;

    @Column(name = "pictures")
    private String picture;

    @OneToMany()
    @JoinColumn(name = "travel_id")
    private List<Travel> createdTravels;

    @OneToMany()
    @JoinColumn(name = "travel_id")
    private List<Travel> participatedInTravels;

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    // @JoinColumn(name = "feedback_id")
    private List<Feedback> feedbacks;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getRaiting() {
        return rating;
    }

    public void setRaiting(double raiting) {
        this.rating = raiting;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Travel> getCreatedTravels() {
        return createdTravels;
    }

    public void setCreatedTravels(List<Travel> createdTravels) {
        this.createdTravels = createdTravels;
    }

    public List<Travel> getParticipatedInTravels() {
        return participatedInTravels;
    }

    public void setParticipatedInTravels(List<Travel> participatedInTravels) {
        this.participatedInTravels = participatedInTravels;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(username, user.username) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, username, phoneNumber);
    }
}
