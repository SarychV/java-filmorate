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
    public Collection<User> selectAllUsers() {
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
    public int size() {
        return users.size();
    }
}
