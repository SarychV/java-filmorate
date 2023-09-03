package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void add(Film film);

    Film read(int id);

    Collection<Film> findAllFilms();

    void update(Film film);

    void delete(Film film);

    void deleteAll();

    int size();

    void addLikeToFilm(int filmId, int userId);

    void removeLikeFromFilm(int filmId, int userId);
}
