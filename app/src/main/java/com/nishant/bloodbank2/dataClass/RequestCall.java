package com.nishant.bloodbank2.dataClass;


import java.util.List;

public class RequestCall {
    private int status;
    private String message;
    private RegisteredUser registeredUser;
    private int registrationStatus;
    private List<RegisteredUser> allRegisteredUser;

    public int getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(int registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    public List<RegisteredUser> getAllRegisteredUser() {
        return allRegisteredUser;
    }

    public void setAllRegisteredUser(List<RegisteredUser> allRegisteredUser) {
        this.allRegisteredUser = allRegisteredUser;
    }
}
