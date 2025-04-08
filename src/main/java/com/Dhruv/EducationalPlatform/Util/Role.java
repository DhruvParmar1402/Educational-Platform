package com.Dhruv.EducationalPlatform.Util;

public enum Role {
    INSTRUCTOR,
    STUDENT;

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
