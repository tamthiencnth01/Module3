package com.thien.model;

public class User {
    protected int id;
    protected String nameUser;
    protected String email;
    protected String country;

    public User() {
    }

    public User(String nameUser, String email, String country) {
        super();
        this.nameUser = nameUser;
        this.email = email;
        this.country = country;
    }
    public User(int id, String nameUser, String email, String country) {
        super();
        this.id = id;
        this.nameUser = nameUser;
        this.email = email;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
