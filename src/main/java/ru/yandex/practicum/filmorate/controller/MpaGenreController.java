package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.MpaGenreService;

import java.util.Collection;

@RestController
public class MpaGenreController {
    private MpaGenreService mpaGenreService;

    public MpaGenreController(MpaGenreService mpaGenreService) {
        this.mpaGenreService = mpaGenreService;
    }

    @GetMapping("/mpa/{id}")
    public Rating findMpaByRatingId(@PathVariable int id) {
        return mpaGenreService.findRatingById(id);
    }

    @GetMapping("/mpa")
    public Collection<Rating> findAllRatings() {
        return mpaGenreService.findAllRatings();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable int id) {
        return mpaGenreService.findGenreById(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> findAllGenres() {
        return mpaGenreService.findAllGenres();
    }
}
