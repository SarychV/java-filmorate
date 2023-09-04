package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int lastGeneratedUserId = 0;

    @Override
    public void add(User user) {
        user.setId(++lastGeneratedUserId);
        users.put(user.getId(), user);
    }

    @Override
    public User read(int id) {
        if (!users.containsKey(id))
            throw new NotFoundException(String.format("Пользователь id=%d не зарегистрирован.", id));
        return users.get(id);
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public void update(User user) {
        int userId = user.getId();
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь id=%d не зарегистрирован.", userId));
        }
        users.put(userId, user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public void makeFriends(int id1, int id2) {
        User u1 = read(id1);
        User u2 = read(id2);
        if (u1 != null && u2 != null) {
            u1.addFriend(id2);
            u2.addFriend(id1);
        } else {
            throw new NotFoundException("Пользователь отсутствует.");
        }
    }

    @Override
    public void removeFriends(int id1, int id2) {
        read(id1).removeFriend(id2);
        read(id2).removeFriend(id1);
    }

    @Override
    public int size() {
        return users.size();
    }
}
