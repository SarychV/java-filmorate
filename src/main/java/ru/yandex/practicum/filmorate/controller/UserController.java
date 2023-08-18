package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public static final ch.qos.logback.classic.Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(UserController.class);

    static {
        log.setLevel(Level.DEBUG);
    }

    @Autowired
    public UserController(UserService service) {
        this.userService = service;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        log.info("Пользователь зарегистрирован: " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        hasValidUserId(user.getId());
        userService.updateUser(user);
        log.info("Обновлены сведения о пользователе: " + user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeFriends(@PathVariable int id, @PathVariable int friendId) {
        hasValidUserId(id);
        userService.makeFriends(id, friendId);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        hasValidUserId(id);
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> showListOfFriends(@PathVariable int id) {
        hasValidUserId(id);
        return userService.formListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> showCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        hasValidUserId(id);
        hasValidUserId(otherId);
        return userService.formCommonFriendsList(id, otherId);
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable int id, @PathVariable int friendId) {
        hasValidUserId(id);
        hasValidUserId(friendId);
        userService.removeFriends(id, friendId);
    }

    private boolean hasValidUserId(int id) {
        if (id <= 0) throw new ValidationException(
                String.format("Используется неверный идентификатор пользователя: id=%d", id));
        return true;
    }
}
