package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.WithoutBlanks;

import javax.validation.constraints.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class User {
    @PositiveOrZero
    private Integer id = 0;
    @Email
    @NotNull
    private String email;

    @NotNull
    @NotEmpty
    @WithoutBlanks
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friends;

    public User() {
        this.friends = new HashSet<>();
    }

    public User(Integer id, String email, String login, String name, LocalDate date) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = date;
        this.friends = new HashSet<>();
    }

    public User(int id, String email, String login, String name, LocalDate date, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = date;
        if (friends != null) {
            this.friends = friends;
        } else {
            this.friends = new HashSet<>();
        }
    }

    public void addFriend(long userId) {
        friends.add(userId);
    }

    public void removeFriend(long userId) {
        friends.remove(userId);
    }

    public Set<Long> findIdsOfFriends() {
        return friends;
    }
}
