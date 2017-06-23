package com.example.ruslan.towncare.Models.User;

/**
 * Created by Namregog on 6/21/2017.
 */

public class User {
    private String userEmail;
    private String userId;
    private String userTown;
    private String userPhone;
    private String userRole;

<<<<<<< HEAD
    // for FireBase
=======
>>>>>>> origin/master
    public User() {
    }

    public User(String userEmail, String userId, String userTown, String userPhone) {
        this.userEmail = userEmail;
        this.userId = userId;
        this.userTown = userTown;
        this.userPhone = userPhone;
        this.userRole = "user";
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserTown() {
        return userTown;
    }

    public void setUserTown(String userTown) {
        this.userTown = userTown;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
