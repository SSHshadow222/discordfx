package com.example.discordfx.domain.friendship;

import com.example.discordfx.domain.Entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private long u1;
    private long u2;
    private LocalDateTime date;
    private FriendshipState state;
    public Friendship(long id, long u1, long u2, LocalDateTime date) {
        this.id = id;
        this.u1 = u1;
        this.u2 = u2;
        this.date = date;
        this.state = FriendshipState.PENDING;
    }

    // region Getters And Setters

    public long getU1() {
        return u1;
    }

    public void setU1(long u1) {
        this.u1 = u1;
    }

    public long getU2() {
        return u2;
    }

    public void setU2(long u2) {
        this.u2 = u2;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FriendshipState getState() {
        return state;
    }

    public void setState(FriendshipState state) {
        this.state = state;
    }

    // endregion

    @Override
    public String toString() {
        return "[" + u1 + " " + u2 + "] date: " + String.valueOf(date) + " " + state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(u1, that.u1) && Objects.equals(u2, that.u2) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(u1, u2, date);
    }
}

