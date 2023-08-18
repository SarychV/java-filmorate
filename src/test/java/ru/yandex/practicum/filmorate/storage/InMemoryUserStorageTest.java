package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryUserStorageTest {
    UserStorage storage = new InMemoryUserStorage();

    @Test
    void shouldAddAndReadUserInStorage() {
        int userId = 1;
        User user = new User(1, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            storage.read(userId);
        });
        assertEquals(
                String.format("Пользователь id=%d не зарегистрирован.", user.getId()),
                ex.getMessage());

        storage.add(user);
        User returnedUser = storage.read(userId);
        assertEquals(user, returnedUser);

        storage.deleteAll();
    }

    @Test
    void shouldCalculateSizeAndDeleteAllUsersInStorage() {
        int sizeOfStorage = storage.size();
        User user1 = new User(1, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));
        User user2 = new User(2, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));
        User user3 = new User(3, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));
        storage.add(user1);
        storage.add(user2);
        storage.add(user3);

        assertEquals(sizeOfStorage + 3, storage.size());

        storage.deleteAll();
        assertEquals(0, storage.size());
    }

    @Test
    void shouldDeleteUser() {
        int userId = 2;
        User user = new User(1, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));

        storage.deleteAll();
        int sizeBefore = storage.size();
        storage.delete(user);
        assertEquals(sizeBefore, storage.size());

        storage.add(user);
        assertEquals(sizeBefore + 1, storage.size());
        storage.delete(user);
        assertEquals(sizeBefore, storage.size());

        storage.deleteAll();
    }

    @Test
    void shouldSelectAllUsersFromStorage() {
        assertEquals(0, storage.size());
        User user1 = new User(1, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));
        User user2 = new User(2, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));
        User user3 = new User(3, "user@localhost", "user1", "user#1", LocalDate.of(2002,03,19));
        storage.add(user1);
        storage.add(user2);
        storage.add(user3);

        Collection<User> returned = storage.selectAllUsers();
        assertEquals(3, returned.size());
    }
}

