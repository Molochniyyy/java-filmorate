package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.MpaDao;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public MPA findMpaById(Integer id){
        MPA mpa = mpaDao.findMpaById(id);
        log.info("MPA с id = {} найден", id);
        return mpa;
    }

    public List<MPA> findAllMpa(){
        List<MPA> mpas = mpaDao.findAllMpa();
        log.info("Найдены все MPA");
        return mpas;
    }
}
