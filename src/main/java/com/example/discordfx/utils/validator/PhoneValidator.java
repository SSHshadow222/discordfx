package com.example.discordfx.utils.validator;

import java.util.List;

public class PhoneValidator
{
    /**
     * Verifies if a phone no. has a valid format
     * @param phone the phone number
     * @return true if the phone no. is valid, false otherwise
     */
    public static boolean isPhoneNo(String phone) {
        List<String> formats = List.of("[0-9]{10}");
        for (String format : formats) {
            if (phone.matches(format)) {
                return true;
            }
        }

        return false;
    }
}
