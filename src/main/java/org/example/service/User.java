package org.example.service;

public class User {
    private String role;
    private String login;
    private String password;

    public User(String role, String login, String password) {
        this.role = role;
        this.login = login;
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public String getLogin() {
        return login;
    }

    public int getPasswordHashCode() {
        return password.hashCode();
    }

    public boolean isAdmin() {
        return role.equals("admin");
    }

    @Override
    public String toString() {
        return "User{" +
                "role='" + role + '\'' +
                ", login='" + login + '\'' +
                ", passwordHash='" + getPasswordHashCode() + '\'' +
                '}';
    }
}