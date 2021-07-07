package com.example.aplicatie;

import java.io.Serializable;

public class Person implements Serializable {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String id;

    public Person() {
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.password = "";
        this.id="";
    }
    public Person(String firstname, String lastname, String email, String password, String id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
