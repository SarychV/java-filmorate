package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import javax.validation.constraints.*;

@Data
public class Film {
    @PositiveOrZero
    private Integer id = 0;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @DateAfter("28.12.1895")
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
    private Set<Long> likes;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
    }

    public void addLike(long userId) {
        likes.add(userId);
    }

    public void removeLike(long userId) {
        if (!likes.remove(userId))
            throw new NotFoundException(String.format("Пользователь с id=%d отсутствует.", userId));
    }

    public int countLikes() {
        return likes.size();
    }
}
