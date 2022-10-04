package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController uc;

    @BeforeEach
    public void afterEach() {
        uc = new UserController();
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
        uc.addUser(user);
        User updatedUser = getUser();
        updatedUser.setId(0);
        assertThrows(ValidationException.class, () -> uc.updateUser(updatedUser));
    }

    @Test
    public void shouldThrowValidationException_whenGetWrongEmail(){
        User user = getUser();
        uc.addUser(user);
        user.setEmail("mail.mail");
        assertThrows(ValidationException.class,()->uc.updateUser(user));
        user.setEmail(null);
        assertThrows(ValidationException.class,()->uc.updateUser(user));
    }

    @Test
    public void shouldUseLoginAsName_ifNameIsEmpty(){
        User user = getUser();
        uc.addUser(user);
        user.setName("");
        assertEquals("test",uc.updateUser(user).getName());
    }

    @Test void shouldThrowValidationException_whenBirthdayIsInTheFuture(){
        User user = getUser();
        uc.addUser(user);
        user.setBirthday(LocalDate.of(2023,1,1));
        assertThrows(ValidationException.class,()->uc.updateUser(user));
    }

    @Test
    public void shouldThrowValidationException_whenLoginIsEmptyOrContainsSpaces(){
        User user = getUser();
        uc.addUser(user);
        user.setLogin("mamma mia");
        assertThrows(ValidationException.class,()->uc.updateUser(user));
        user.setLogin(null);
        assertThrows(ValidationException.class,()->uc.updateUser(user));
    }
}
