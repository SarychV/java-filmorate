package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

@Data
@Builder(toBuilder = true)
public class User {
    private int id;
    @NotNull
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate date) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = date;
    }

    public void validate() throws ValidationException {
        if (email.isEmpty())
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        if (!email.contains("@"))
            throw new ValidationException("Адрес электронной почты должен содержать символ '@'.");
        if (login.isEmpty())
            throw new ValidationException("Логин пользователя не может быть пустым.");
        if (login.contains(" "))
            throw new ValidationException("Логин пользователя не может содержать пробелы.");
        if (birthday.isAfter(LocalDate.now()))
            throw new ValidationException("Дата рождения не может быть позже текущей даты.");
    }
}
