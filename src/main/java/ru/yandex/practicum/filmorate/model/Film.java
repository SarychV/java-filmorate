package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder(toBuilder = true)
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;

    public void validate() throws ValidationException {
        if (name.isEmpty())
            throw new ValidationException("Название фильма не может быть пустым.");
        if (description.length() > 200)
            throw new ValidationException("Максимальная длина описания фильма-200 символов.");
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Дата релиза не может быть раньше 28.12.1895.");
        if (duration < 0)
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
    }
}
