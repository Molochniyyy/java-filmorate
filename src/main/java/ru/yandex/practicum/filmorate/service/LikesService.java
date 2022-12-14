package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.LikesDao;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikesService {

    private final LikesDao likesDao;

    public void addLike(long filmId, long userId) {
        log.info("Лайк добавлен к фильму с id = {} от пользователя с id = {}", filmId, userId);
        likesDao.addLike(filmId, userId);
    }

    public void removeLike(long filmId, long userId) {
        log.info("Лайк удален у фильма с id = {} от пользователя с id = {}", filmId, userId);
        likesDao.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count){
        log.info("Получен топ фильмов по лайкам, count = {}",count);
        return likesDao.getPopularFilms(count);
    }
}
