package com.example.discordfx.repository;

import com.example.discordfx.domain.Entity;
import com.example.discordfx.exception.RepoException;
import com.example.discordfx.validator.Validator;

import java.util.HashMap;
import java.util.Map;

public abstract class MemoryRepo<ID, E extends Entity<ID>> implements Repository<ID, E> {
    protected final Validator<ID, E> valid;
    private final Map<ID, E> entities;

    public MemoryRepo(Validator<ID, E> valid) {
        this.valid = valid;
        entities = new HashMap<ID, E>();
    }

    @Override
    public boolean save(E entity) throws RepoException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        valid.validate(entity);

        if (entities.get(entity.getId()) != null) {
            return false;
        }

        entities.put(entity.getId(), entity);
        return true;
    }

    @Override
    public boolean update(E entity) throws RepoException{
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        valid.validate(entity);

        // The entity is not yet contained in the mapper
        if (entities.get(entity.getId()) == null) {
            return false;
        }

        entities.put(entity.getId(), entity);
        return true;
    }

    @Override
    public boolean delete(ID id) throws RepoException{
        if( id == null ){
            throw new IllegalArgumentException("Id must not be null!");
        }

        entities.remove(id);
        return true;
    }

    @Override
    public E get(ID id) throws RepoException{
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null!");
        }

        return entities.get(id);
    }

    @Override
    public Iterable<E> getAll() {
        return entities.values();
    }

    @Override
    public long size(){
        return entities.size();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}
