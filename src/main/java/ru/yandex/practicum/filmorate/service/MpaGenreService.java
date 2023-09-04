package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.MpaGenreStorage;

import java.util.Collection;

@Service
public class MpaGenreService {
    private final MpaGenreStorage mpaGenreStorage;

    public MpaGenreService(MpaGenreStorage mpaGenreStorage) {
        this.mpaGenreStorage = mpaGenreStorage;
    }

    public Rating findRatingById(int id) {
        return mpaGenreStorage.findRatingById(id);
    }

    public Collection<Rating> findAllRatings() {
        return mpaGenreStorage.findAllRatings();
    }

    public Genre findGenreById(int id) {
        return mpaGenreStorage.findGenreById(id);
    }

    public Collection<Genre> findAllGenres() {
        return mpaGenreStorage.findAllGenres();
    }
}
