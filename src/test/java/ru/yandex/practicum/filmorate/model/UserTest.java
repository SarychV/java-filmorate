package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class UserTest {
    User user;

    @BeforeEach
    public void initUser() {
        user = new User(1, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));
    }

    @Test
    public void userContainsCorrectFields() {
        Assertions.assertDoesNotThrow(user::validate);
    }

    @Test
    public void checkIncorrectEmailValues() {
        user = user.toBuilder().email("postbox").build();
        Assertions.assertThrows(ValidationException.class, user::validate);
        user = user.toBuilder().email("").build();
        Assertions.assertThrows(ValidationException.class, user::validate);
    }

    @Test
    public void checkIncorrectLoginValues() {
        user = user.toBuilder().login(" user").build();
        Assertions.assertThrows(ValidationException.class, user::validate);
        user = user.toBuilder().login("user ").build();
        Assertions.assertThrows(ValidationException.class, user::validate);
        user = user.toBuilder().login("us er").build();
        Assertions.assertThrows(ValidationException.class, user::validate);
        user = user.toBuilder().login("").build();
        Assertions.assertThrows(ValidationException.class, user::validate);
    }

    @Test
    public void checkIncorrectBirthdayValues() {
        user = user.toBuilder().birthday(LocalDate.now()).build();
        Assertions.assertDoesNotThrow(user::validate);
        user = user.toBuilder().birthday(LocalDate.now().plusDays(1)).build();
        Assertions.assertThrows(ValidationException.class, user::validate);
    }

    @Test
    public void checkIncorrectNameValues() {
        user = user.toBuilder().name("").build();
        Assertions.assertDoesNotThrow(user::validate);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }
}
