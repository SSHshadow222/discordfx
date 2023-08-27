package com.example.discordfx.service;

import com.example.discordfx.domain.User;
import com.example.discordfx.exception.RepoException;
import com.example.discordfx.repository.database.UserRepo;
import com.example.discordfx.service.dto.UserDto;
import com.example.discordfx.service.mapper.Mapper;
import com.example.discordfx.service.mapper.UserMapper;
import com.example.discordfx.validator.UserValid;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService extends Service
{
    private final UserRepo repo;
    private final Mapper<Long, User> mapper;
    private long autoincrement;

    public UserService() throws RepoException {
        UserValid userValid = new UserValid();
        repo = new UserRepo(userValid);
        mapper = new UserMapper();

        initAutoincrement();
    }

    /**
     * Creates a new user based on the given parameters and adds it to the repository
     *
     * @param first  First name of the user
     * @param last   Last name of the user
     * @param passwd The User's password
     * @param age    The age of the user
     * @return True if the user object was successfully added, false otherwise.
     */
    public boolean add(String first, String last, String passwd, int age) throws RepoException {
        long id = autoincrement;
        User user = new User(id, first, last, passwd, age);
        boolean saved = repo.save(user);
        if (saved) {
            notifyObservers();
            autoincrement++;
        }

        return saved;
    }

    public UserDto add(String first, String last, String passwd, int age, String email) throws RepoException {
        long id = autoincrement;
        User user = new User(id, first, last, passwd, age);
        user.setEmail(email);
        boolean saved = repo.save(user);
        if (saved) {
            notifyObservers();
            autoincrement++;

            return (UserDto) mapper.toDto(user);
        }

        return null;
    }

    /**
     * Removes a user based on an id
     *
     * @param id The id of the user
     * @return True if the id is found in the repository, false otherwise.
     */
    public boolean remove(long id) throws RepoException {
        User user = repo.get(id);
        if (user == null) {     // the user doesn't exist
            return false;
        }
        boolean removed = repo.delete(id);
        if (removed)    notifyObservers();

        return removed;
    }

    /**
     * Updates a user based on the given parameters
     *
     * @param id     The id of the user
     * @param first  The new first name of the user
     * @param last   The new last name of the user
     * @param passwd The new password for the user
     * @param age    The new age of the user
     * @param email  The new email for the user
     * @param phone  The new phone number for the user
     * @return True if the id is found in the repository, false otherwise.
     */
    public boolean update(long id, String first, String last, String passwd, int age, String email, String phone) throws RepoException {
        User user = new User(id, first, last, passwd, age);
        user.setEmail(email);
        user.setPhone(phone);
        setNullValuesToDefault(user);

        boolean updated = repo.update(user);
        if (updated)    notifyObservers();

        return updated;
    }

    public UserDto authenticate(String login, String passwd){
        if (login == null)  return null;

        User _u = repo.get(login), u = null;
        if (_u == null)     return null;
        if (Objects.equals(_u.getPasswd(), passwd)){
            u = _u;
        }

        return (UserDto) mapper.toDto(u);
    }

    public UserDto get(long id) {
        return (UserDto) mapper.toDto(repo.get(id));
    }

    public UserDto get(String login){
        if (login == null)  return null;

        return (UserDto) mapper.toDto(repo.get(login));
    }

    /**
     * Gets a copy of the elements in the repository
     *
     * @return An iterable on the list
     */
    public Iterable<UserDto> getAll() {
        List<UserDto> users = new ArrayList<>();
        repo.getAll().forEach(u -> users.add((UserDto) mapper.toDto(u)));

        return users;
    }

    private void setNullValuesToDefault(User u){
        long id = u.getId();
        User _u = repo.get(id);

        String _first = _u.getFirst(),
            _last = _u.getLast(),
            _passwd = _u.getPasswd(),
            _email = _u.getEmail(),
            _phone = _u.getPhone();

        long _age = _u.getAge();

        if (u.getFirst() == null)   u.setFirst(_first);
        if (u.getLast() == null)    u.setLast(_last);
        if (u.getPasswd() == null)    u.setPasswd(_passwd);
        if (u.getEmail() == null)    u.setEmail(_email);
        if (u.getPhone() == null)    u.setPhone(_phone);
        if (u.getAge() == -1)    u.setAge(_age);
    }
    private void initAutoincrement() {
        autoincrement =  0;
        repo.getAll().forEach((User u) -> {
            if (autoincrement < u.getId())
                autoincrement = u.getId();
            });
        autoincrement += 1;
    }
}
