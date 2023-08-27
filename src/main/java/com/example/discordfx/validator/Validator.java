package com.example.discordfx.validator;

import com.example.discordfx.domain.Entity;
import com.example.discordfx.exception.ValidException;

public interface Validator<ID, E extends Entity<ID>> {
    /**
     * Validates the values/fields inside an entity
     * @param entity
     * @throws com.example.discordfx.exception.ValidException
     */
    void validate(E entity) throws ValidException;
}
