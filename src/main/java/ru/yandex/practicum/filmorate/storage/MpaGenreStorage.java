package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class MpaGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Rating findRatingById(int id) {
        String sqlQuery = "SELECT * FROM ratings WHERE id=?;";
        SqlRowSet ratingRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!ratingRow.next()) {
            throw new NotFoundException(String.format("Рейтинг с id=%d отсутствует.", id));
        }
        Rating mpa = new Rating();
        mpa.setId(ratingRow.getInt("id"));
        mpa.setName(ratingRow.getString("name"));
        return mpa;
    }

    public Collection<Rating> findAllRatings() {
        List<Rating> ratings = new ArrayList<>();
        String sqlQuery = "SELECT * FROM ratings;";
        jdbcTemplate.query(sqlQuery, rs -> {
            Rating rating = new Rating();
            rating.setId(rs.getInt("id"));
            rating.setName(rs.getString("name"));
            ratings.add(rating);
        });
        return ratings;
    }

    public Genre findGenreById(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE id=?;";
        SqlRowSet genreRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!genreRow.next()) {
            throw new NotFoundException(String.format("Жанр фильма с id=%d отсутствует.", id));
        }
        Genre genre = new Genre();
        genre.setId(genreRow.getInt("id"));
        genre.setName(genreRow.getString("name"));
        return genre;
    }

    public Collection<Genre> findAllGenres() {
        List<Genre> genres = new ArrayList<>();
        String sqlQuery = "SELECT * FROM genres;";
        jdbcTemplate.query(sqlQuery, rs -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("id"));
            genre.setName(rs.getString("name"));
            genres.add(genre);
        });
        return genres;
    }
}
