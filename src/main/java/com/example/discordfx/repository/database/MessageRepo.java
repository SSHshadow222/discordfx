package com.example.discordfx.repository.database;

import com.example.discordfx.domain.Message;
import com.example.discordfx.exception.DatabaseException;
import com.example.discordfx.exception.RepoException;
import com.example.discordfx.repository.Repository;
import com.example.discordfx.utils.database.DBConnection;
import com.example.discordfx.validator.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageRepo implements Repository<Long, Message>
{
    Validator<Long, Message> valid;

    public MessageRepo(Validator<Long, Message> valid) {
        this.valid = valid;
    }

    @Override
    public boolean save(Message message) throws RepoException {
        try(Connection conn = DBConnection.getConnection()){
            valid.validate(message);

            String query = "INSERT INTO message VALUES(?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, message.getId());
            stmt.setString(2, message.getText());
            stmt.setLong(3, message.getFromUserId());
            stmt.setLong(4, message.getToUserId());
            stmt.setString(5, message.getDate().toString());
            stmt.setBoolean(6, message.isEdited());

            short rowsAffected = (short) stmt.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to insert the message.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean update(Message message) throws RepoException{
        try(Connection conn = DBConnection.getConnection()){
            valid.validate(message);

            String query = "UPDATE message SET text = ?, \"fromUserId\" = ?, \"toUserId\" = ?, date = ?, edited = true WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, message.getText());
            stmt.setLong(2, message.getFromUserId());
            stmt.setLong(3, message.getToUserId());
            stmt.setTimestamp(4, Timestamp.valueOf(message.getDate()));
            stmt.setLong(5, message.getId());

            short rowsAffected = (short) stmt.executeUpdate();
            return rowsAffected == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to update the message.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean delete(Long id) {
        if (id == null){
            throw new IllegalArgumentException();
        }
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM message WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);

            short affectedRows = (short) stmt.executeUpdate();
            return affectedRows == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to remove the message.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public Message get(Long id) {
        if (id == null){
            throw new IllegalArgumentException();
        }
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM message WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return extractEntity(rs);
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to get the message.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public Iterable<Message> getAll() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM message";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()){
                Message message = extractEntity(rs);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to get the messages.";
            throw new DatabaseException(err);
        }
    }

    /**
     * Gets the messages between two users. Note that the order in which the parameters are provided is irrelevant.
     * @param u1 The id of the first user
     * @param u2 The id of the second user
     */
    public Iterable<Message> getMessagesBetweenUsers(long u1, long u2){
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM message WHERE (\"from_user_id\" = ? AND \"to_user_id\" = ?) OR (\"from_user_id\" = ? AND \"to_user_id\" = ?) ORDER BY date";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, u1);
            stmt.setLong(2, u2);
            stmt.setLong(3, u2);
            stmt.setLong(4, u1);

            ResultSet rs = stmt.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()){
                Message message = extractEntity(rs);
                messages.add(message);
            }

            return messages;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established\n" +
                    "Failed to get the messages between the two users";
            throw new DatabaseException(err);
        }
    }

    @Override
    public long size() {
        try (Connection conn = DBConnection.getConnection()){
            String query = "SELECT COUNT(*) FROM message";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getLong("count");
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to get the number of existing messages.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Creates a new Message based on a result set
     *
     * @param rs A result set containing all the information about a message
     */
    private Message extractEntity(ResultSet rs){
        Message message = null;

        try{
            long id = rs.getLong(1);
            if (id == 0) {
                return null;
            }

            String text = rs.getString(2);

            long fromUserId, toUserId;
            fromUserId = rs.getLong(3);
            toUserId = rs.getLong(4);

            LocalDateTime date = LocalDateTime.parse(rs.getString(5));

            boolean edited = rs.getBoolean(6);

            message = new Message(id, text, fromUserId, toUserId);
            message.setDate(date);
            message.setEdited(edited);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return message;
    }
}
