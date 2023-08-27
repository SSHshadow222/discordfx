package com.example.discordfx.repository.database;

import com.example.discordfx.domain.User;
import com.example.discordfx.exception.DatabaseException;
import com.example.discordfx.exception.RepoException;
import com.example.discordfx.utils.database.DBConnection;
import com.example.discordfx.validator.Validator;
import com.example.discordfx.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepo implements Repository<Long, User> {
    private final Validator<Long, User> valid;

    public UserRepo(Validator<Long, User> valid) {
        this.valid = valid;
    }

    @Override
    public boolean save(User u) throws RepoException {
        try (Connection conn = DBConnection.getConnection()) {
            valid.validate(u);

            String query = "INSERT INTO app_user VALUES(?,?,?,?,?,?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, u.getId());
            stmt.setString(2, u.getFirst());
            stmt.setString(3, u.getLast());
            stmt.setString(4, u.getPasswd());
            stmt.setLong(5, u.getAge());

            String email = u.getEmail(), phone = u.getPhone();
            if (email != null && !email.equals(""))
                stmt.setString(6, email);
            else
                stmt.setNull(6, Types.NULL);
            if (phone != null && !phone.equals(""))
                stmt.setString(7, phone);
            else
                stmt.setNull(7, Types.NULL);

            stmt.setString(8, u.getPictureId());

            short affectedRows = (short) stmt.executeUpdate();

            return affectedRows == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to insert the user.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean update(User u) throws RepoException {
        try (Connection conn = DBConnection.getConnection()) {
            valid.validate(u);

            String query = "UPDATE app_user SET first = ?, last = ?, passwd = ?, age = ?, email = ?, phone = ? WHERE id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(7, u.getId());
            stmt.setString(1, u.getFirst());
            stmt.setString(2, u.getLast());
            stmt.setString(3, u.getPasswd());
            stmt.setLong(4, u.getAge());

            String email = u.getEmail(), phone = u.getPhone();
            if (email != null && !email.equals(""))
                stmt.setString(5, email);
            else
                stmt.setNull(5, Types.NULL);
            if (phone != null && !phone.equals(""))
                stmt.setString(6, phone);
            else
                stmt.setNull(6, Types.NULL);

            short affectedRows = (short) stmt.executeUpdate();

            return affectedRows == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to update the user.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        try (Connection conn = DBConnection.getConnection()) {
            String query = "DELETE FROM app_user WHERE id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);

            short affectedRows = (short) stmt.executeUpdate();

            return affectedRows == 1;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to delete the user.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public User get(Long id) throws RepoException {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM app_user WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return extractEntity(rs);
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to return the user.";
            throw new DatabaseException(err);
        }
    }

    public User get(String login){
        if (login == null) {
            throw new IllegalArgumentException();
        }

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM app_user WHERE email = ? OR phone = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, login);
            stmt.setString(2, login);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return extractEntity(rs);
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to return the user.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public Iterable<User> getAll() throws RepoException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM app_user";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User u = extractEntity(rs);
                users.add(u);
            }

            return users;
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to return all the users";
            throw new DatabaseException(err);
        }
    }

    @Override
    public long size() throws RepoException {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM app_user";
            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getLong("count");
        } catch (SQLException e) {
            String err = "The connection to the database could not be established.\n" +
                    "Failed to check the number of users.";
            throw new DatabaseException(err);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Creates a new User based on a result set
     *
     * @param rs A result set containing all the information about a user
     */
    private User extractEntity(ResultSet rs) {
        User user = null;

        try {
            user = new User(rs.getLong(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getLong(5));

            if (user.getId() == 0) {
                return null;
            }

            user.setEmail(rs.getString(6));
            user.setPhone(rs.getString(7));

        } catch (SQLException ignored) {
        }

        return user;
    }
}
