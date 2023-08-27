package com.example.discordfx.repository.memory;

import com.example.discordfx.domain.User;
import com.example.discordfx.repository.MemoryRepo;
import com.example.discordfx.validator.Validator;

public class UserRepo extends MemoryRepo<Long, User> {
    public UserRepo(Validator<Long, User> valid) {
        super(valid);
    }
}
