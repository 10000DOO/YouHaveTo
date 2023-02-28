package com.yht.exerciseassist.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("No authentication information.");
        }
        return authentication.getName();
    }

    public static String getMemberRole() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            throw new IllegalArgumentException("No authentication information.");
        }
        String userRole = authentication.getAuthorities().toString();
        return userRole.substring(userRole.indexOf("_") + 1, userRole.length() - 1);
    }
}
