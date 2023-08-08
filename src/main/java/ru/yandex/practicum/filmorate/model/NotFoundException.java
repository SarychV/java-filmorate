package ru.yandex.practicum.filmorate.model;

public class NotFoundException extends RuntimeException{
    public NotFoundException() {
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
