package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void add(User user);

    User read(int id);

    Collection<User> findAllUsers();

    void update(User user);

    void delete(User user);

    void deleteAll();

    void makeFriends(int id1, int id2);

    void removeFriends(int id1, int id2);

    int size();
}