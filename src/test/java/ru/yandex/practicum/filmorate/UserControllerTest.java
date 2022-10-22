package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController uc;

    @BeforeEach
    public void afterEach() {
        uc = new UserController(new UserService(new InMemoryUserStorage()));
    }

    private User getUser() {
        return User.builder()
                .birthday(LocalDate.of(2002, 1, 1))
                .email("mail@mail.ru")
                .name("testName")
                .login("test")
                .build();
    }

    @Test
    public void shouldThrowValidationException_whenGetUnknownId() {
        User user = getUser();
        uc.add(user);
        User updatedUser = getUser();
        updatedUser.setId(0);
        assertThrows(ValidationException.class, () -> uc.update(updatedUser));
    }

    @Test
    public void shouldThrowValidationException_whenGetWrongEmail(){
        User user = getUser();
        uc.add(user);
        user.setEmail("mail.mail");
        assertThrows(ValidationException.class,()->uc.update(user));
        user.setEmail(null);
        assertThrows(ValidationException.class,()->uc.update(user));
    }

    @Test
    public void shouldUseLoginAsName_ifNameIsEmpty(){
        User user = getUser();
        uc.add(user);
        user.setName("");
        assertEquals("test",uc.update(user).getName());
    }

    @Test void shouldThrowValidationException_whenBirthdayIsInTheFuture(){
        User user = getUser();
        uc.add(user);
        user.setBirthday(LocalDate.of(2023,1,1));
        assertThrows(ValidationException.class,()->uc.update(user));
    }

    @Test
    public void shouldThrowValidationException_whenLoginIsEmptyOrContainsSpaces(){
        User user = getUser();
        uc.add(user);
        user.setLogin("mamma mia");
        assertThrows(ValidationException.class,()->uc.update(user));
        user.setLogin(null);
        assertThrows(ValidationException.class,()->uc.update(user));
    }
}
