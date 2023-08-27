package com.example.discordfx.service;

import com.example.discordfx.domain.friendship.Friendship;
import com.example.discordfx.domain.friendship.FriendshipState;
import com.example.discordfx.exception.RepoException;
import com.example.discordfx.repository.database.FriendshipRepo;
import com.example.discordfx.validator.FriendshipValid;

import java.time.LocalDateTime;

public class FriendshipService extends Service{
    private final FriendshipRepo repo;
    private Long autoincrement;

    public FriendshipService() {
        FriendshipValid valid = new FriendshipValid();
        repo = new FriendshipRepo(valid);

        initAutoincrement();
    }

    /**
     * Establishes a friendship between two users
     *
     * @param u1 The id of the first user
     * @param u2 The id of the second user
     * @return True if the friendship was successfully made between the users, false otherwise.
     */
    public boolean add(long u1, long u2) throws RepoException {
        if (u1 == u2) return false;

        // users are already friends
        if (repo.get(u1, u2) != null)   return false;

        long id = autoincrement;
        Friendship f = new Friendship(id, u1, u2, LocalDateTime.now());
        boolean saved = repo.save(f);
        if (saved) {
            notifyObservers();
            autoincrement++;
        }

        return true;
    }

    /**
     * Removes an established friendship between two users
     *
     * @param u1 The first user
     * @param u2 The second user
     * @return True if the friendship was successfully removed, false otherwise.
     */
    public boolean remove(long u1, long u2) throws RepoException {
        if (u1 == u2) return false;

        Friendship f = repo.get(u1, u2);
        if (f == null)  return false;
        else {
            repo.delete(f.getId());
            notifyObservers();
            return true;
        }
    }

    public boolean update(long u1, long u2, FriendshipState state) {
        if (u1 == u2)   return false;

        Friendship f = repo.get(u1, u2);
        if (f == null)  return false;
        else{
            Friendship newf = new Friendship(f.getId(), u1, u2, LocalDateTime.now());
            newf.setState(state);
            repo.update(newf);
            notifyObservers();
            return true;
        }
    }

    public Friendship get(long u1, long u2) {
        if (u1 == u2)   return null;
        return repo.get(u1, u2);
    }

    public Iterable<Long> getFriendsIds(long u){
        return repo.getFriendsIds(u);
    }
    public Iterable<Long> getFriendsIds(long u, FriendshipState state){
        return repo.getFriendsIds(u, state);
    }

    /**
     * Sets up the autoincrement.
     */
    private void initAutoincrement() {
        autoincrement = (long) 0;
        repo.getAll().forEach((Friendship f) -> {
            if (autoincrement < f.getId())
                autoincrement = f.getId();
        });
        autoincrement += 1;
    }

}
