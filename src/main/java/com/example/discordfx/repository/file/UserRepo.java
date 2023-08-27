package com.example.discordfx.repository.file;

import com.example.discordfx.domain.User;
import com.example.discordfx.exception.FileException;
import com.example.discordfx.validator.Validator;
import com.example.discordfx.repository.FileRepo;

import java.util.List;

public class UserRepo extends FileRepo<Long, User> {
    public UserRepo(Validator<Long, User> valid) throws FileException {
        super(valid, "src/resources/Users.csv");
        loadDataFromFile();
        saveDataToFile(); // dumping the possible invalid data
    }

    @Override
    public boolean save(User u) throws IllegalArgumentException, FileException {
        boolean saved = super.save(u);
        if (saved) {
            saveDataToFile();
        }

        return saved;
    }

    @Override
    public boolean update(User u) throws IllegalArgumentException, FileException {
        boolean updated = super.update(u);
        if (updated) {
            saveDataToFile();
        }

        return updated;
    }

    @Override
    public boolean delete(Long id) throws IllegalArgumentException, FileException {
        boolean deleted = super.delete(id);
        if (deleted) {
            saveDataToFile();
        }

        return deleted;
    }

    @Override
    protected User extractEntity(List<String> elems) {
        int len = elems.size();
        if (!(len >= 5 && len <= 7)) {
            return null;
        }

        String first = elems.get(1),
                last = elems.get(2),
                passwd = elems.get(3),
                email = "",
                phone = "";
        long age = -1, id = -1;
        try {
            age = Long.parseLong(elems.get(4));
            id = Long.parseLong(elems.get(0));
        } catch (NumberFormatException ignored) {
        }

        // null values for email and phone no. are accepted
        // they are not mandatory
        if (len >= 6) {
            email = elems.get(5);
        }
        if (len == 7) {
            phone = elems.get(6);
        }

        User user = new User(id, first, last, passwd, age);
        user.setEmail(email);
        user.setPhone(phone);

        return user;
    }

    @Override
    protected String entityToFileFormat(User u) {
        return u.getId() + "," +
                u.getFirst() + "," +
                u.getLast() + "," +
                u.getPasswd() + "," +
                u.getAge() + "," +
                u.getEmail() + "," +
                u.getPhone();
    }
}
