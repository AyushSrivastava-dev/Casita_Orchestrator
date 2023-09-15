package com.example.casita_v1;

public class DataStore {

    String name, username, email, phone, pass;

    public DataStore(String name, String username, String email, String phone, String pass) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }

    public DataStore() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
