package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    static int lastGeneratedId;
    Map<Integer, Film> films = new HashMap<>();
    public static final ch.qos.logback.classic.Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmController.class);

    static {
        log.setLevel(Level.DEBUG);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        film.validate();
        if (film.getId() == 0) film.setId(++lastGeneratedId);
        films.put(film.getId(), film);
        log.info("addFilm(): " + film + " добавлен");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        film.validate();
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("updateFilm(): " + film + " обновлен");
        } else {
            log.debug("updateFilm(): " + film + " не обновлен (отсутствует)");
            throw new ValidationException();
        }
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }
}
