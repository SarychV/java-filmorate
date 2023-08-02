package ru.yandex.practicum.filmorate.model;

public class ValidationException extends Exception {
    public ValidationException() {
    }

    public ValidationException(String msg) {
        super(msg);
    }
}
