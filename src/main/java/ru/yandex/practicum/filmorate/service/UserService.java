package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private int lastGeneratedUserId = 0;
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.userStorage = storage;
    }

    public void addUser(User user) {
        if (user.getId() <= 0) {
            user.setId(++lastGeneratedUserId);
        }
        String name = user.getName();
        if (name == null || name.isEmpty()) user.setName(user.getLogin());
        userStorage.add(user);
    }

    public void updateUser(User user) {
        if (user.getId() <= 0) throw new ValidationException("Недопустимый id:" + user);
        String name = user.getName();
        if (name == null || name.isEmpty()) user.setName(user.getLogin());
        userStorage.update(user);
    }

    public User selectUserById(int id) {
        return userStorage.read(id);
    }

    public Collection<User> selectAllUsers() {
        return userStorage.selectAllUsers();
    }

    public void makeFriends(int id1, int id2) {
        selectUserById(id1).addFriend(id2);
        selectUserById(id2).addFriend(id1);
    }

    public void removeFriends(int id1, int id2) {
        selectUserById(id1).removeFriend(id2);
        selectUserById(id2).removeFriend(id1);
    }

    public Collection<User> formListOfFriends(int userId) {
        return selectUserById(userId).findIdsOfFriends()
                .stream()
                .map((id) -> selectUserById((int)(long)id))
                .collect(Collectors.toList());
    }

    public Collection<User> formCommonFriendsList(int userId1, int userId2) {
        Set<Long> friendIds1 = selectUserById(userId1).findIdsOfFriends();
        Set<Long> friendIds2 = selectUserById(userId2).findIdsOfFriends();
        Set<Long> commonFriendIds = new HashSet<>();

        for (Long id : friendIds1) {
            if (friendIds2.contains(id)) {
                commonFriendIds.add(id);
            }
        }
        return commonFriendIds.stream()
                .map((id) -> selectUserById((int)(long)id))
                .collect(Collectors.toList());
    }
}