package com.example.discordfx.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>
{
    private String text;
    private long fromUserId, toUserId;
    private LocalDateTime date;
    private boolean edited;

    public Message(Long id, String text, long fromUserId, long toUserId) {
        this.id = id;
        this.text = text;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.date = LocalDateTime.now();
        this.edited = false;
    }

    // region Getters and Setters

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    // endregion
}
