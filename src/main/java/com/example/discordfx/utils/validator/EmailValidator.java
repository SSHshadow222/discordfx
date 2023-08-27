package com.example.discordfx.utils.validator;

import java.util.Arrays;
import java.util.List;

public class EmailValidator
{
    /**
     * Verifies if an email has a valid format
     * @param email the email
     * @return true if the email is valid, false otherwise
     */
    public static boolean isEmail(String email) {
        List<String> topLevelDomains = Arrays.asList("com", "net", "org", "gov",
                "edu", "info", "biz", "gg", "ninja", "ro", "cn", "tk", "de", "uk", "tw", "ro", "ru", "nl");

        // (aBcDe_1F$g3) (@) (HiJk2LmN4o_P) (.) (com, net, org, ...)
        if (email.matches(".*" + '@' + ".*" + "\\." + "(?:" + String.join("|", topLevelDomains) + ")")) {
            return true;
        }

        return false;
    }
}
