package com.yht.exerciseassist.util;

import java.util.Random;

public class TempPassword {

    public static String generateRandomString(int length) {
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "-_.~!@<>()$*?";
        String allChars = uppercase + lowercase + numbers + specialChars;

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Add at least one uppercase letter
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));

        // Add at least one lowercase letter
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));

        // Add at least one digit
        password.append(numbers.charAt(random.nextInt(numbers.length())));

        // Add at least one special character
        password.append(specialChars.charAt(random.nextInt(specialChars.length())));

        // Add remaining characters randomly
        for (int i = 0; i < length - 4; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        return password.toString();
    }
}
