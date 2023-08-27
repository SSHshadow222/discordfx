package com.example.discordfx.service.mapper;

import com.example.discordfx.domain.User;
import com.example.discordfx.service.dto.Dto;
import com.example.discordfx.service.dto.UserDto;

public class UserMapper extends Mapper<Long, User>{
    @Override
    public Dto toDto(User u) {
        if (u == null)  return null;

        long id = u.getId(), age = u.getAge();
        String first = u.getFirst(),
                last = u.getLast(),
                email = u.getEmail(),
                phone = u.getPhone(),
                pictureId = u.getPictureId();

        UserDto userDto = new UserDto(id, first, last, age, email, phone, pictureId);
        return userDto;
    }
}
