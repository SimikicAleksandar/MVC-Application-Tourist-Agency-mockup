package com.example.turistickaagencija.Services.impl;

import com.example.turistickaagencija.Daos.PutovanjeDao;
import com.example.turistickaagencija.Daos.RezervacijaDao;
import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Putovanje;
import com.example.turistickaagencija.Models.Rezervacija;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.PutovanjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring5.processor.SpringInputPasswordFieldTagProcessor;

import javax.servlet.http.Cookie;
import java.util.List;
@Service
public class DatabasePutovanjeServiceImpl implements PutovanjeService {
    @Autowired
    private PutovanjeDao putovanjeDao;

    @Autowired
    private RezervacijaDao rezervacijaDao;

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

    @Override
    public List<Putovanje> searchPutovanje(String query){
        return putovanjeDao.searchPutovanje(query);
    }

    @Override
    public List<Putovanje> searchPutovanjeByAmountRange(Long minCena, Long maxCena){
        return putovanjeDao.searchPutovanjeByAmountRange(minCena, maxCena);
    }

    @Override
    public List<Putovanje> findSortedPutovanje(String sort, String pravac){
        return putovanjeDao.findSortedPutovanja(sort, pravac);
    }

}
