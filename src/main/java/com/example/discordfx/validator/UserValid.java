package com.example.discordfx.validator;

import com.example.discordfx.domain.User;
import com.example.discordfx.exception.ValidException;

import java.util.Objects;

import static com.example.discordfx.utils.validator.EmailValidator.isEmail;
import static com.example.discordfx.utils.validator.PhoneValidator.isPhoneNo;

public class UserValid implements Validator<Long, User> {
    @Override
    public void validate(User u) throws ValidException {
        String err = "";
        Long id = u.getId(), age = u.getAge();
        String first = u.getFirst(),
                last = u.getLast(),
                passwd = u.getPasswd(),
                email = u.getEmail(),
                phone = u.getPhone();

        if (id == null || id <= 0) {
            err += "Invalid ID\n";
        }
        if ((first == null) || (first.equals(""))) {
            err += "Invalid first name!\n";
        } else if (!first.matches("[a-zA-Z]+")) {
            err += "First name must only contain letters(no spaces)!\n";
        }
        if ((last == null) || last.equals("")) {
            err += "Invalid last name!\n";
        } else if (!last.matches("[a-zA-Z]+")) {
            err += "Last name must only contain letters(no spaces)!\n";
        }
        if (passwd == null || passwd.equals("")) {
            err += "Invalid password!\n";
        } else if (passwd.length() > 127) {
            err += "The password can be as big as 127 characters only!\n";
        }
        if (age < 0) {
            err += "Invalid age!\n";
        } else if (age < 13) {
            err += "You must ask someone older in your family for permission!\n";
        }
        if ((email != null) && !Objects.equals(email, "")) {
            if (!isEmail(email)) {
                err += "Invalid email!\n";
            }
        }
        if ((phone != null) && !phone.equals("")) {
            if (!isPhoneNo(phone)) {
                err += "Invalid phone number!\n";
            }
        }

        if (err.length() > 0) {
            throw new ValidException(err);
        }
    }
}
