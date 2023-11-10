package com.example.turistickaagencija.Services;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Putovanje;
import com.example.turistickaagencija.Models.Uloga;

import javax.servlet.http.Cookie;
import java.util.List;

public interface PutovanjeService {
    Putovanje get(Long id);
    Putovanje get(String nazivDestinacije,Long brojNocenja);
    Putovanje findOne(Long id) throws UserNotFoundException;
    List<Putovanje> findAll();
    Putovanje save(Putovanje putovanje);
    Putovanje update(Putovanje putovanje);
    Putovanje delete(Long id);
}
