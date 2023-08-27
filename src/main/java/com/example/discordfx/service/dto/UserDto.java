package com.example.discordfx.service.dto;

import java.util.Objects;

public class UserDto extends Dto {
    private long id, age;
    private String first, last, email, phone, pictureId;

    public UserDto(long id, String first, String last, long age, String email, String phone, String pictureId) {
        this.id = id;
        this.first = first;
        this.last = last;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.pictureId = pictureId;
    }

    // region Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPictureId(){
        return pictureId;
    }

    public void setPictureId(String pictureId){
        this.pictureId = pictureId;
    }

    // endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + ". " + first + ' ' + last + ' ' + age + ' ' +
                email + ' ' + phone;
    }
}
