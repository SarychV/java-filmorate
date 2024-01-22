package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Film film) {
        film.setId(createFilm(film));
        if (film.getId() > 0) {
            createGenreLinks(film);
            createLikeLinks(film);
        }
    }

    private int createFilm(Film film) {
        String sqlQuery =
                "INSERT INTO films (name, description, release_date, duration, rating_id) "
                        + "VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setString(3, film.getReleaseDate().toString());
            stmt.setString(4, film.getDuration() + "");
            stmt.setString(5, film.getMpa().getId() + "");
            return stmt;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private void createGenreLinks(Film film) {
        String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?);";
        int filmId = film.getId();
        for (Genre genre: film.getGenres()) {
            if (genre != null) {
                jdbcTemplate.update(sqlQuery, filmId, genre.getId());
            }
        }
    }

    private void createLikeLinks(Film film) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?);";
        int filmId = film.getId();
        for (long userId: film.getLikes()) {
            if (userId > 0) {
                jdbcTemplate.update(sqlQuery, filmId, (int) userId);
            }
        }
    }

    @Override
    public Film read(int id) {
        Film film;
        String sqlQuery = "SELECT * FROM films WHERE id=?;";
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!filmRow.next()) {
            throw new NotFoundException(String.format("Фильм id=%d отсутствует в фильмотеке.", id));
        }
        film = new Film(
                filmRow.getInt("id"),
                filmRow.getString("name"),
                filmRow.getString("description"),
                Objects.requireNonNull(filmRow.getDate("release_date")).toLocalDate(),
                filmRow.getInt("duration")
        );

        // Прикрутим рейтинг.
        Rating mpa = new Rating();
        int ratingId = filmRow.getInt("rating_id");
        mpa.setId(ratingId);
        mpa.setName(getRatingName(ratingId));

        film.setMpa(mpa);

        // Займемся жанрами.
        Set<Genre> genres = new HashSet<>();
        sqlQuery = "SELECT genre_id FROM film_genre WHERE film_id=? ORDER BY genre_id ASC;";
        jdbcTemplate.query(sqlQuery, (RowCallbackHandler) rs -> addGenre(genres, rs), id);

        film.setGenres(genres);

        // Добавим лайки.
        Set<Long> likes = new HashSet<>();
        sqlQuery = "SELECT user_id FROM likes WHERE film_id=?;";
        jdbcTemplate.query(sqlQuery, (RowCallbackHandler) rs -> likes.add((long) rs.getInt("user_id")), id);

        film.setLikes(likes);

        return film;
    }

    private String getRatingName(int ratingId) {
        String sqlQuery = "SELECT name FROM ratings WHERE id=?;";
        return  jdbcTemplate.queryForObject(sqlQuery, (resultSet, rows) -> resultSet.getString("name"), ratingId);
    }

    private String getGenreName(int genreId) {
        String sqlQuery = "SELECT name FROM genres WHERE id=?;";
        return  jdbcTemplate.queryForObject(sqlQuery, (resultSet, rows) -> resultSet.getString("name"), genreId);
    }

    private void addGenre(Set<Genre> genres, ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        int genreId = rs.getInt("genre_id");
        genre.setId(genreId);
        genre.setName(getGenreName(genreId));
        genres.add(genre);
    }

    private void addLike(Set<Long> likes, ResultSet rs) throws SQLException {
        likes.add((long) rs.getInt("user_id"));
    }

/*    @Override
    public Collection<Film> findAllFilms() {
        List<Film> films = new ArrayList<>();
        String sqlQuery = "SELECT id FROM films ORDER BY id ASC;";
        List<Integer> ids = jdbcTemplate.query(sqlQuery, (rs, num) -> rs.getInt("id"));
        for (int id : ids) {
            if (id != 0) {
                films.add(read(id));
            }
        }
        return films;
    }

 */

    @Override
    public Collection<Film> findAllFilms() {
        String sqlQuery =
        "SELECT id, name, description, release_date, duration, rating_id, rating_name, genre_id, genre_name, user_id " +
        "FROM (  SELECT id, name, description, release_date, duration, rating_id, rating_name, genre_id, genre_name " +
                "FROM (  SELECT films.id, films.name, description, release_date, duration, " +
                        "rating_id, ratings.name AS rating_name " +
                        "FROM films " +
                        "LEFT OUTER JOIN ratings " +
                        "ON rating_id=ratings.id " +
                ") as t1 " +
                "LEFT OUTER JOIN " +
                        "(   SELECT film_id, genre_id, name AS genre_name " +
                                "FROM film_genre " +
                                "LEFT OUTER JOIN genres ON genre_id=id ORDER BY genre_id ASC " +
                        ") as t2 " +
               " ON t1.id=t2.film_id ORDER BY id ASC " +
            ") " +
        "LEFT OUTER JOIN likes ON id=film_id; ";

        /*
        SELECT id, name, description, release_date, duration, rating_id, rating_name, genre_id, genre_name, user_id
        FROM (  SELECT id, name, description, release_date, duration, rating_id, rating_name, genre_id, genre_name
                FROM (  SELECT films.id, films.name, description, release_date, duration,
                             rating_id, ratings.name AS rating_name
                        FROM films
                        LEFT OUTER JOIN ratings
                        ON rating_id=ratings.id
                     ) as t1
                LEFT OUTER JOIN
                    (   SELECT film_id, genre_id, name AS genre_name
                        FROM film_genre
                        LEFT OUTER JOIN genres ON genre_id=id ORDER BY genre_id ASC
                    ) as t2
                ON t1.id=t2.film_id ORDER BY id ASC
             )
        LEFT OUTER JOIN likes ON id=film_id;
        */

        return jdbcTemplate.query(sqlQuery, (ResultSetExtractor<List<Film>>)(rs) -> {
            List<Film> films = new ArrayList<>();
            Film film = null;
            int lastFilmId = 0;

            while (rs.next()) {
                int filmId = rs.getInt("id");
                if (filmId != lastFilmId) {
                    lastFilmId = filmId;
                    if (film != null) {
                        films.add(film);
                    }
                    film = new Film(filmId,
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDate("release_date").toLocalDate(),
                            rs.getInt("duration"));

                    Rating rating = new Rating();
                    rating.setId(rs.getInt("rating_id"));
                    rating.setName(rs.getString("rating_name"));
                    film.setMpa(rating);

                    Genre genre = new Genre();
                    genre.setId(rs.getInt("genre_id"));
                    genre.setName(rs.getString("genre_name"));
                    if (genre.getId() != 0) {
                        film.getGenres().add(genre);
                    }
                    int userLikeId = rs.getInt("user_id");
                    if (userLikeId != 0) {
                        film.getLikes().add((long) userLikeId);
                    }
                } else {
                    Genre genre = new Genre();
                    genre.setId(rs.getInt("genre_id"));
                    genre.setName(rs.getString("genre_name"));
                    if (film != null && genre.getId() != 0) {
                        film.getGenres().add(genre);
                    }
                    int userLikeId = rs.getInt("user_id");
                    if (userLikeId != 0) {
                        film.getLikes().add((long) userLikeId);
                    }
                }
            }
            if (film != null) {
                films.add(film);
            }
            return films;
        });
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "UPDATE films SET name=?, description=?, release_date=?, duration=?, rating_id=? "
                + "WHERE id = ?;";
        int result = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate().toString(),
                film.getDuration() + "",
                film.getMpa().getId() + "",
                film.getId()
        );
        if (result <= 0) {
            throw new NotFoundException(
                    String.format("Обновление сведений о фильме с id=%d не выполнено.", film.getId()));
        }
        //Обновить жанры.
        removeGenreLinks(film.getId());
        createGenreLinks(film);
    }

    private void removeGenreLinks(int filmId) {
        String sqlQuery = "DELETE FROM film_genre WHERE film_id=?;";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void delete(Film film) {
        String sqlQuery = "DELETE FROM films WHERE id=?;";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "DELETE FROM films;";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public int size() {
        String sqlQuery = "SELECT COUNT(id) AS counter FROM films;";
        return jdbcTemplate.queryForObject(sqlQuery, (resultSet, rows) -> resultSet.getInt("counter"));
    }

    @Override
    public void addLikeToFilm(int filmId, int userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLikeFromFilm(int filmId, int userId) {
        if (filmId <= 0 || userId <= 0) {
            throw new NotFoundException(
                    String.format("Лайк пользователя с id=%d для фильма id=%d не удален.", userId, filmId));
        }
        String sqlQuery = "DELETE FROM likes WHERE (film_id=? and user_id=?);";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }
}
