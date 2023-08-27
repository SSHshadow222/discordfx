package com.example.discordfx.repository.database;

import com.example.discordfx.domain.friendship.Friendship;
import com.example.discordfx.domain.friendship.FriendshipState;
import com.example.discordfx.exception.DatabaseException;
import com.example.discordfx.exception.RepoException;
import com.example.discordfx.repository.Repository;
import com.example.discordfx.utils.database.DBConnection;
import com.example.discordfx.validator.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipRepo implements Repository<Long, Friendship> {
    private final Validator<Long, Friendship> valid;

    public FriendshipRepo(Validator<Long, Friendship> valid) {
        this.valid = valid;
    }

    @Override
    public boolean save(Friendship f) throws RepoException {
        try (Connection conn = DBConnection.getConnection()) {
            valid.validate(f);

            String query = "INSERT INTO friendship VALUES(?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, f.getId());
            stmt.setLong(2, f.getU1());
            stmt.setLong(3, f.getU2());
            stmt.setString(4, f.getDate().toString());
            stmt.setString(5, f.getState().toString());

            short affectedRows = (short) stmt.executeUpdate();

            return affectedRows == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to insert the friendship relationship.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean update(Friendship f) throws RepoException {
        try (Connection conn = DBConnection.getConnection()) {
            valid.validate(f);

            String query = "UPDATE friendship SET u1 = ?, u2 = ?, date = ?, state = ? WHERE id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, f.getU1());
            stmt.setLong(2, f.getU2());
            stmt.setString(3, f.getDate().toString());
            stmt.setString(4, f.getState().toString());
            stmt.setLong(5, f.getId());

            short affectedRows = (short) stmt.executeUpdate();

            return affectedRows == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to update the friendship status.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM friendship WHERE id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);

            short affectedRows = (short) stmt.executeUpdate();

            return affectedRows == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to remove the friendship.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public Friendship get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM friendship WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return extractEntity(rs);
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to return the friendship.";
            throw new DatabaseException(err);
        }
    }

    /**
     * Returns a friendship from the repository based on the given users
     *
     * @param u1 the id of the first user
     * @param u2 the id of the second user
     * @return A friendship if the users ids do exist in the database, null otherwise.
     * @throws IllegalArgumentException one or both of the users ids are null
     * @throws DatabaseException        the connection to the database was forcefully closed during the execution
     */
    public Friendship get(Long u1, Long u2) throws RepoException {
        if (u1 == null || u2 == null) {
            throw new IllegalArgumentException();
        }

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM friendship WHERE (u1 = ? AND u2 = ?) OR (u1 = ? AND u2 = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, u1);
            stmt.setLong(2, u2);
            stmt.setLong(3, u2);
            stmt.setLong(4, u1);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return extractEntity(rs);
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to return the friendship.";
            throw new DatabaseException(err);
        }
    }

    /**
     * Returns a list with the friends ids for a given user
     *
     * @param u the id of the user
     * @return a list with friends ids
     * @throws IllegalArgumentException the id of the user was null
     * @throws DatabaseException        the connection to the database was forcefully closed during the execution
     */
    public List<Long> getFriendsIds(Long u) throws RepoException {
        if (u == null) {
            throw new IllegalArgumentException();
        }
        List<Long> friends = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT u1, u2 FROM friendship WHERE (u1 = ? OR u2 = ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, u);
            stmt.setLong(2, u);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long u1, u2;
                u1 = rs.getLong(1);
                u2 = rs.getLong(2);

                if (u1 == u) friends.add(u2);
                else friends.add(u1);
            }

        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to return the friends for the user.";
            throw new DatabaseException(err);
        }

        return friends;
    }
    public List<Long> getFriendsIds(Long u, FriendshipState state) throws RepoException {
        if (u == null) {
            throw new IllegalArgumentException();
        }
        List<Long> friends = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT u1, u2 FROM friendship WHERE (u1 = ? OR u2 = ?) AND state = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, u);
            stmt.setLong(2, u);
            stmt.setString(3, state.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                long u1, u2;
                u1 = rs.getLong(1);
                u2 = rs.getLong(2);

                if (u1 == u) friends.add(u2);
                else friends.add(u1);
            }

        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to return the friends for the user.";
            throw new DatabaseException(err);
        }

        return friends;
    }

    @Override
    public Iterable<Friendship> getAll() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM friendship";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<Friendship> fships = new ArrayList<>();
            while (rs.next()) {
                Friendship u = extractEntity(rs);
                fships.add(u);
            }

            return fships;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to return all the established friendships";
            throw new DatabaseException(err);
        }
    }

    @Override
    public long size() throws RepoException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM friendship";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getLong("count");
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to check the number of established friendships.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Creates a new Friendship based on a result set
     *
     * @param rs A result set containing all the information about a friendship
     */
    private Friendship extractEntity(ResultSet rs) {
        Friendship friendship = null;
        try {
            long id = rs.getLong(1);
            if (id == 0) {
                return null;
            }

            long u1, u2;
            u1 = rs.getLong(2);
            u2 = rs.getLong(3);

            LocalDateTime date = LocalDateTime.parse(rs.getString(4));

            friendship = new Friendship(id, u1, u2, date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return friendship;
    }
}
