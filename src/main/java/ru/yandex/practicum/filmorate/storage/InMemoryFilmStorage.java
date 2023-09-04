package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int lastGeneratedFilmId = 0;

    @Override
    public void add(Film film) {
        film.setId(++lastGeneratedFilmId);
        films.put(film.getId(), film);
    }

    @Override
    public Film read(int id) {
        if (!films.containsKey(id))
            throw new NotFoundException(String.format("Фильм id=%d отсутствует в фильмотеке.", id));
        return films.get(id);
    }

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    @Override
    public void update(Film film) {
        int filmId = film.getId();
        if (!films.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм id=%d отсутствует в фильмотеке.", filmId));
        }
        films.put(filmId, film);
    }

    @Override
    public void delete(Film film) {
        films.remove(film.getId());
    }

    @Override
    public void deleteAll() {
        films.clear();
    }

    @Override
    public int size() {
        return films.size();
    }

    @Override
    public void addLikeToFilm(int filmId, int userId) {
        read(filmId).getLikes().add((long) userId);
    }

    @Override
    public void removeLikeFromFilm(int filmId, int userId) {
        if (!read(filmId).getLikes().remove((long) userId)) {
            throw new NotFoundException(
                    String.format("Лайк пользователя с id=%d для фильма id=%d не удален.", userId, filmId));
        }
    }
}
