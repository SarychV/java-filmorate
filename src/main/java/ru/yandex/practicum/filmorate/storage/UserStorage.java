package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void add(User user);

    User read(int id);

    Collection<User> selectAllUsers();

    void update(User user);

    void delete(User user);

    void deleteAll();

    int size();
}