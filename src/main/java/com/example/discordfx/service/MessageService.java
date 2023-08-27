package com.example.discordfx.service;

import com.example.discordfx.domain.Message;
import com.example.discordfx.repository.database.MessageRepo;
import com.example.discordfx.validator.MessageValid;

public class MessageService extends Service
{
    private final MessageRepo repo;
    private Long autoincrement;

    public MessageService() {
        MessageValid valid = new MessageValid();
        repo = new MessageRepo(valid);

        initAutoincrement();
    }

    public boolean add(String text, long fromUserId, long toUserId){
        if (fromUserId == toUserId)
            return false;

        long id = autoincrement;
        Message message = new Message(id, text, fromUserId, toUserId);
        boolean saved = repo.save(message);
        if (saved) {
            notifyObservers();
            autoincrement++;
        }

        return saved;
    }

    public boolean remove(long id){
        return repo.delete(id);
    }

    public boolean update(long id, String text){
        Message message = repo.get(id);
        if (message == null)
            return false;

        message.setText(text);
        message.setEdited(true);
        repo.update(message);
        return true;
    }

    /**
     * Returns a conversation between two users. Note that the order in which the users are provided is irrelevant.
     * @param u1 The id of the first user
     * @param u2 The id of the second user
     * @throws com.example.discordfx.exception.RepoException If the provided users are equivalent
     */
    public Iterable<Message> getConversation(long u1, long u2){
        if (u1 == u2){
            throw new RuntimeException("Cannot return a conversation between the user and itself!\n");
        }

        return repo.getMessagesBetweenUsers(u1, u2);
    }

    /**
     * Sets up the autoincrement.
     */
    private void initAutoincrement(){
        autoincrement = (long) 0;
        repo.getAll().forEach(msg -> {
            long msgId = msg.getId();
            if (msgId > autoincrement){
                autoincrement = msgId;
            }
        });
        autoincrement++;
    }
}
