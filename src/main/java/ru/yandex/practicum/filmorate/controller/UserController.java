package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;
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
        userService.updateUser(user);
        log.info("Обновлены сведения о пользователе: " + user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeFriends(@PathVariable int id, @PathVariable int friendId) {
        if (id <= 0 || friendId <= 0)
            throw new ValidationException(
                    String.format("Указан неверный идентификатор пользователя: id=%d", id <= 0 ? id : friendId)
            );
        userService.makeFriends(id, friendId);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.selectAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        if (id <= 0) throw new ValidationException("Неверное значение идентификатора пользователя.");
        return userService.selectUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> showListOfFriends(@PathVariable int id) {
        if (id <= 0) throw new ValidationException(
                String.format("Указан неверный идентификатор пользователя: id=%d", id)
                );
        return userService.formListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> showCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (id <= 0 || otherId <= 0)
            throw new ValidationException(
                    String.format("Указан неверный идентификатор пользователя: id=%d", id <= 0 ? id : otherId)
            );
        return userService.formCommonFriendsList(id, otherId);
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable int id, @PathVariable int friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new ValidationException(
                    String.format("Указан неверный идентификатор пользователя: id=%d", id <= 0 ? id : friendId)
            );
        }
        userService.removeFriends(id, friendId);
    }

}
