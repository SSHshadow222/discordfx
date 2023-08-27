package com.example.discordfx.repository.file;

import com.example.discordfx.domain.friendship.Friendship;
import com.example.discordfx.exception.FileException;
import com.example.discordfx.validator.Validator;
import com.example.discordfx.repository.FileRepo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendshipRepo extends FileRepo<Long, Friendship> {
    public FriendshipRepo(Validator<Long, Friendship> valid) throws FileException {
        super(valid, "src/resources/Friends.csv");
        loadDataFromFile();
        saveDataToFile(); // prev. loaded file could've contained invalid data
    }

    @Override
    public boolean save(Friendship f) throws IllegalArgumentException, FileException {
        boolean saved = super.save(f);
        if (saved) {
            saveDataToFile();
        }

        return saved;
    }

    @Override
    public boolean update(Friendship f) throws IllegalArgumentException, FileException {
        boolean updated = super.update(f);
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
    protected Friendship extractEntity(List<String> elems) {
        if (elems.size() != 4) {
            return null;
        }

        Long id, u1, u2;
        id = u1 = u2 = (long) -1;
        LocalDateTime date;

        try {
            id = Long.parseLong(elems.get(0));
            u1 = Long.parseLong(elems.get(1));
            u2 = Long.parseLong(elems.get(2));
        } catch (NumberFormatException ignored) {
        }

        date = LocalDateTime.parse(elems.get(3), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return new Friendship(id, u1, u2, date);
    }

    @Override
    protected String entityToFileFormat(Friendship f) {
        return f.getId() + "," +
                f.getU1() + "," +
                f.getU2() + "," +
                f.getDate();
    }
}
