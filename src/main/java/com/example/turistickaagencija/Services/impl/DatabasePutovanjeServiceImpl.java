package com.example.turistickaagencija.Services.impl;

import com.example.turistickaagencija.Daos.PutovanjeDao;
import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Putovanje;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.PutovanjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.List;
@Service
public class DatabasePutovanjeServiceImpl implements PutovanjeService {
    @Autowired
    private PutovanjeDao putovanjeDao;


    @Override
    public Putovanje get(Long id) {
        return putovanjeDao.findOne(id);
    }

    @Override
    public Putovanje get(String nazivDestinacije, Long brojNocenja) {
        return putovanjeDao.findOne(nazivDestinacije, brojNocenja);
    }

    @Override
    public Putovanje findOne(Long id) throws UserNotFoundException {
        return putovanjeDao.findOne(id);
    }

    @Override
    public List<Putovanje> findAll() {
        return putovanjeDao.findAll();
    }

    @Override
    public Putovanje save(Putovanje putovanje) {
        putovanjeDao.save(putovanje);
        return putovanje;
    }

    @Override
    public Putovanje update(Putovanje putovanje) {
        putovanjeDao.update(putovanje);
        return putovanje;
    }

    @Override
    public Putovanje delete(Long id) {
        Putovanje putovanje = get(id);
        if(putovanje != null){
            putovanjeDao.delete(id);
        }
        return putovanje;
    }

}
