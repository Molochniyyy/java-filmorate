package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    private String email;
    @Pattern(regexp = "^\\\\S*$")
    private String login;
    private String name;
    private LocalDate birthday;
}
