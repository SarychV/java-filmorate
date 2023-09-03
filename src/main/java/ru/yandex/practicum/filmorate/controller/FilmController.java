package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;
    public static final ch.qos.logback.classic.Logger log =
            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(FilmController.class);

    static {
        log.setLevel(Level.DEBUG);
    }

    @Autowired
    public FilmController(FilmService service) {
        this.filmService = service;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("В фильмотеку добавляется фильм: " + film);
        filmService.addFilm(film);
        Film savedFilm = filmService.findFilmById(film.getId());
        log.info("Добавлен фильм: " + savedFilm);
        return savedFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        hasValidFilmId(film.getId());
        log.info("Вносятся изменения в фильм:" + film);
        filmService.updateFilm(film);
        Film modifiedFilm = filmService.findFilmById(film.getId());
        log.info("Фильм изменен: " + modifiedFilm);
        return modifiedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable int id, @PathVariable int userId) {
        hasValidFilmId(id);
        hasValidUserId(userId);
        filmService.addLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable int id, @PathVariable Integer userId) {
        hasValidFilmId(id);
        filmService.removeLikeFromFilm(id, userId);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        hasValidFilmId(id);
        return filmService.findFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> showMorePopularFilms(@RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.findMorePopularFilms(count.get());
        }
        return filmService.findMorePopularFilms(10);
    }

    private boolean hasValidUserId(int id) {
        if (id <= 0) throw new ValidationException(
                String.format("Используется неверный идентификатор пользователя: id=%d", id));
        return true;
    }

    private boolean hasValidFilmId(int id) {
        if (id <= 0) throw new ValidationException(
                String.format("Используется неверный идентификатор фильма: id=%d", id));
        return true;
    }
}
