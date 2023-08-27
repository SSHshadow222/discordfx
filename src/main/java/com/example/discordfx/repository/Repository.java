package com.example.discordfx.repository;

import com.example.discordfx.domain.Entity;

public interface Repository<ID, E extends Entity<ID>>
{
    /**
     * Saves a new entry to the repository
     * @param entity The object to be saved
     * @return If the object was saved(In case the object already exists in repository, it will not be saved again).
     * @throws com.example.discordfx.exception.ValidException The object 'entity' was invalid
     */
    public boolean save(E entity);

    /**
     * Modifies an entry in the repository
     * @param entity The updated object
     * @return True if the object is found, false otherwise.
     * @throws IllegalArgumentException
     * @throws com.example.discordfx.exception.ValidException The object 'entity' was invalid
     */
    public boolean update(E entity);

    /**
     * Deletes an entry from the repository
     * @param id The id of the entity
     * @return True if the entity is found, false otherwise
     * @throws IllegalArgumentException
     */
    public boolean delete(ID id);

    /**
     * Returns an entity from the repository
     * @param id The id of the entity
     * @return An entity if the id is found in repository, null otherwise.
     * @throws IllegalArgumentException
     */
    E get(ID id);

    /**
     * Gets a copy of the elements in the repository
     * @return An iterable on the list
     */
    Iterable<E> getAll();

    /**
     * @return The size of the repository
     */
    public long size();

    public boolean isEmpty();
}
