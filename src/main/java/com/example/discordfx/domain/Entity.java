package com.example.discordfx.domain;

public abstract class Entity<ID>
{
    protected ID id;
    public ID getId() {
        return this.id;
    }
    public void setId(ID id) {
        this.id = id;
    }
}
