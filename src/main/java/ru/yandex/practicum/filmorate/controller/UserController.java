package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
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
        log.info("Регистрируется пользователь: " + user);
        userService.addUser(user);
        User savedUser = userService.findUserById(user.getId());
        log.info("Пользователь зарегистрирован: " + savedUser);
        return savedUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Вносятся изменения в данные пользователя: " + user);
        userService.updateUser(user);
        User modifiedUser = userService.findUserById(user.getId());
        log.info("Пользователь изменен: " + modifiedUser);
        return modifiedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeFriends(@PathVariable int id, @PathVariable int friendId) {
        userService.makeFriends(id, friendId);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable int id) {
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> showListOfFriends(@PathVariable int id) {
        return userService.formListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> showCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.formCommonFriendsList(id, otherId);
    }


    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriends(id, friendId);
    }
}
