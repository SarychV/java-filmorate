package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.userStorage = storage;
    }

    public void addUser(User user) {
        String name = user.getName();
        if (name == null || name.isEmpty()) user.setName(user.getLogin());
        userStorage.add(user);
    }

    public void updateUser(User user) {
        String name = user.getName();
        if (name == null || name.isEmpty()) user.setName(user.getLogin());
        userStorage.update(user);
    }

    public User findUserById(int id) {
        return userStorage.read(id);
    }

    public Collection<User> findAllUsers() {
        return userStorage.selectAllUsers();
    }

    public void makeFriends(int id1, int id2) {
        User u1 = findUserById(id1);
        User u2 = findUserById(id2);
        if (u1 != null && u2 != null) {
            u1.addFriend(id2);
            u2.addFriend(id1);
        } else {
            throw new NotFoundException("Пользователь отсутствует.");
        }
    }

    public void removeFriends(int id1, int id2) {
        findUserById(id1).removeFriend(id2);
        findUserById(id2).removeFriend(id1);
    }

    public Collection<User> formListOfFriends(int userId) {
        return findUserById(userId).findIdsOfFriends()
                .stream()
                .map((id) -> findUserById((int)(long)id))
                .collect(Collectors.toList());
    }

    public Collection<User> formCommonFriendsList(int userId1, int userId2) {
        Set<Long> friendIds1 = findUserById(userId1).findIdsOfFriends();
        Set<Long> friendIds2 = findUserById(userId2).findIdsOfFriends();
        Set<Long> commonFriendIds = new HashSet<>();

        for (Long id : friendIds1) {
            if (friendIds2.contains(id)) {
                commonFriendIds.add(id);
            }
        }
        return commonFriendIds.stream()
                .map((id) -> findUserById((int)(long)id))
                .collect(Collectors.toList());
    }
}