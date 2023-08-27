package com.example.discordfx.service.mapper;

import com.example.discordfx.domain.Entity;
import com.example.discordfx.service.dto.Dto;
public abstract class Mapper<ID, E extends Entity<ID>>
{
    public abstract Dto toDto(E entity);
}
