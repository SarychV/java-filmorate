package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.*;
import ru.yandex.practicum.filmorate.validator.DateAfter;

import javax.validation.constraints.*;

@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @DateAfter("28.12.1895")
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
    @NotNull
    private Rating mpa;
    private Set<Genre> genres;
    private Set<Long> likes;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
        genres = new HashSet<>();
    }

    public int countLikes() {
        return likes.size();
    }
}