package com.mordvinovdsw.library;
import java.util.HashMap;
import java.util.Map;

public class LogIn {
    private Map<String, String> users;

    public LogIn() {
        users = new HashMap<>();
        users.put("Admin", "1234");
    }

    public boolean checkLogin(String user, String pass) {
        return users.containsKey(user) && users.get(user).equals(pass);
    }
}
