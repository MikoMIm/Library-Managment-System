package com.mordvinovdsw.library.models;

public class User {
    private final int id;
    private final String login;
    final private String password;

    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }


}

