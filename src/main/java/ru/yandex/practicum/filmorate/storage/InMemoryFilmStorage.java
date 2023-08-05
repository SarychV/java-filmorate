package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public void add(Film film) {
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            throw new ValidationException(String.format("Фильм id=%d уже есть в фильмотеке.", film.getId()));
        }
        films.put(filmId, film);
    }

    @Override
    public Film read(int id) {
        if (!films.containsKey(id))
            throw new ValidationException(String.format("Фильм id=%d отсутствует в фильмотеке.", id));
        return films.get(id);
    }

    @Override
    public Collection<Film> selectAllFilms() {
        return films.values();
    }

    @Override
    public void update(Film film) {
        int filmId = film.getId();
        if (!films.containsKey(filmId)) {
            throw new ValidationException(String.format("Фильм id=%d отсутствует в фильмотеке.", filmId));
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
}