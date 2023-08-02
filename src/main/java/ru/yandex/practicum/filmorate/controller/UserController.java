package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    int lastGeneratedId = 0;
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws ValidationException {
        user.validate();
        if (user.getId() == 0) user.setId(++lastGeneratedId);
        users.put(user.getId(), user);
        log.info("addUser(): " + user + " добавлен");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            log.debug("updateUser(): " + user + " не обновлен (id =" + user.getId() + " отсутствует)");
            throw new ValidationException("id пользователя отсутствует в списке");
        }
        users.put(user.getId(), user);
        log.info("updateUser(): " + user + " обновлен");
        return user;
    }

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }
}
