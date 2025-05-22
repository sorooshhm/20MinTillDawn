package com.tillDawn.validators;

public class Validator {
    public static boolean validateUsername(String username) {
        return username.length() >= 4;
    }
    public static boolean validatePassword(String password) {
        return password.matches("^(?=.*[@%$#&*()_])(?=.*\\d)(?=.*[A-Z]).{8,}$");
    }
}
