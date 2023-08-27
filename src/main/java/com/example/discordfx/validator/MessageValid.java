package com.example.discordfx.validator;

import com.example.discordfx.domain.Message;
import com.example.discordfx.exception.ValidException;

import java.util.Objects;

public class MessageValid implements Validator<Long, Message>{
    @Override
    public void validate(Message message) throws ValidException {
        String err = "";
        if (message.getText() == null || Objects.equals(message.getText(), "")){
            err += "The text of the message cannot be empty or null!\n";
        }

        if (err.length() > 0){
            throw new ValidException(err);
        }
    }
}
