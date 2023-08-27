package com.example.discordfx.validator;

import com.example.discordfx.domain.friendship.Friendship;
import com.example.discordfx.exception.ValidException;

import java.time.LocalDateTime;

public class FriendshipValid implements Validator<Long, Friendship> {
    @Override
    public void validate(Friendship f) throws ValidException {
        String err = "";
        Long id = f.getId(),
                u1 = f.getU1(),
                u2 = f.getU2();
        LocalDateTime date = f.getDate();
        if (id == null || id <= 0) {
            err += "Invalid id!\n";
        }
        if (u1 <= 0) {
            err += "Invalid user1 id!\n";
        }
        if (u2 <= 0) {
            err += "Invalid user2 id!\n";
        }
        if (date == null) {
            err += "Invalid date!\n";
        }

        if (err.length() > 0) {
            throw new ValidException(err);
        }
    }
}
