package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDao;

import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre findGenreById(Integer id){
        Genre genre = genreDao.findGenreById(id);
        log.info("Найден Genre c id = {} ", id);
        return genre;
    }

    public List<Genre> findAllGenres(){
        List<Genre> genres = genreDao.findAllGenres();
        log.info("Все Genres найдены");
        return genres;
    }

    public List<Genre> findGenresByFilmId(int id){
        return genreDao.findGenresByFilmId(id);
    }
}
