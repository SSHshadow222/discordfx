package com.example.discordfx.repository.memory;

import com.example.discordfx.domain.friendship.Friendship;
import com.example.discordfx.repository.MemoryRepo;
import com.example.discordfx.validator.Validator;

public class FriendshipRepo extends MemoryRepo<Long, Friendship> {
    public FriendshipRepo(Validator<Long, Friendship> valid) {
        super(valid);
    }
}
