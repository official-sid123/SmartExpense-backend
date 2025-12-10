package com.example.demo.dto;

public class JwtResponse {

    private String token;
    private Long userId;
    private String role;
    private Long orgId;

    // Default Constructor
    public JwtResponse() {
    }

    // Parameterized Constructor
    public JwtResponse(String token, Long userId, String role, Long orgId) {
        this.token = token;
        this.userId = userId;
        this.role = role;
        this.orgId = orgId;
    }

    // Getters & Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
