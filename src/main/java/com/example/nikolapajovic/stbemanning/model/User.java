package com.example.nikolapajovic.stbemanning.model;

import java.io.Serializable;

/**
 * Model class for a user
 * @author Linus Forsberg
 */

public class User implements Serializable {
    private int userId;
    private String firstName;
    private String lastName;

    public User(int userId, String firstName, String lastName) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
