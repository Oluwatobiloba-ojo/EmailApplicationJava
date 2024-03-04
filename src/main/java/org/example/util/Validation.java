package org.example.util;

public class Validation {

    public static boolean validatePassword(String password) {
        return password.matches("[0-9]{4}");
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.startsWith("+"))return phoneNumber.matches("[+1-9][0-9]{6,13}");
        else return phoneNumber.matches("0[7-9][0-1][0-9]{8}");
    }
}
