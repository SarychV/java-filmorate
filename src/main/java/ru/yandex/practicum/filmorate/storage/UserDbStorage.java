package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(User user) {
        user.setId(createUser(user));
        if (user.getId() > 0) {
            createFriendsLink(user);
        }
    }

    private int createUser(User user) {
        String sqlQuery =
                "INSERT INTO users (name, login, email, birthday) "
                        + "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getBirthday().toString());
            return stmt;
            }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    private void createFriendsLink(User user) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id) "
                + "VALUES (?, ?);";
        int userId = user.getId();
        for (long friendId : user.findIdsOfFriends()) {
            if (friendId > 0) {
                jdbcTemplate.update(sqlQuery, userId, (int) friendId);
            }
        }
    }

    @Override
    public User read(int id) {
        User user;
        String sqlQuery = "SELECT * FROM users WHERE id=?;";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!userRow.next()) {
            throw new NotFoundException(String.format("Пользователь id=%d не зарегистрирован.", id));
        }
        user = new User(
                userRow.getInt("id"),
                userRow.getString("email"),
                userRow.getString("login"),
                userRow.getString("name"),
                Objects.requireNonNull(userRow.getDate("birthday")).toLocalDate()
        );

        sqlQuery = "SELECT friend_id FROM friendship WHERE user_id=?;";
        jdbcTemplate.query(sqlQuery, (RowCallbackHandler) rs -> addFriend(user, rs), id);

        return user;

    }

    private void addFriend(User user, ResultSet rs) throws SQLException {
        user.addFriend(rs.getInt("friend_id"));
    }

    @Override
    public Collection<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        String sqlQuery = "SELECT id FROM users;";
        List<Integer> ids = jdbcTemplate.query(sqlQuery, (rs, num) -> rs.getInt("id"));
        for (int id : ids) {
            if (id != 0) {
                users.add(read(id));
            }
        }
        return users;
    }

    @Override
    public void update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? "
                + "WHERE id = ?;";
        int result = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday().toString(),
                user.getId()
        );
        if (result <= 0) throw new NotFoundException(
                String.format("Обновление сведений о пользователе с id=%d не выполнено.", user.getId()));
    }

    @Override
    public void delete(User user) {
        String sqlQuery = "DELETE FROM users WHERE id = ?;";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public void deleteAll() {
        String sqlQuery = "DELETE FROM users;";
        jdbcTemplate.update(sqlQuery);
    }

    @Override
    public void makeFriends(int userId, int friendId) {
        String sqlQuery = "INSERT INTO friendship (user_id, friend_id) VALUES (?, ?);";

        if (userId > 0 && friendId > 0) {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } else {
            throw new NotFoundException("Пользователь отсутствует.");
        }
    }

    @Override
    public void removeFriends(int id1, int id2) {
        String sqlQuery = "DELETE FROM friendship "
            + "WHERE (user_id = ? and friend_id = ?);";
        jdbcTemplate.update(sqlQuery, id1, id2);
    }

    @Override
    public int size() {
        String sqlQuery = "SELECT COUNT(id) AS counter FROM users;";
        return  jdbcTemplate.queryForObject(sqlQuery, (resultSet, rows) -> resultSet.getInt("counter"));
    }
}
