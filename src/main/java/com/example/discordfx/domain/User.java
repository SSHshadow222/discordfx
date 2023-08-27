package com.example.discordfx.domain;

import java.util.Objects;

public class User extends Entity<Long> {
    private long age;
    private String first, last, passwd, email = "", phone = "", pictureId;

    public User(long id, String first, String last, String passwd, long age) {
        this.id = id;
        this.first = first;
        this.last = last;
        this.passwd = passwd;
        this.age = age;

        pictureId = "Discord48x48.png";
    }

    // region Getters and Setters

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

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId){
        this.pictureId = pictureId;
    }

    // endregion

    @Override
    public String toString() {
        return id + ". " + first + ' ' + last + ' ' + age + ' ' +
                email + ' ' + phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
