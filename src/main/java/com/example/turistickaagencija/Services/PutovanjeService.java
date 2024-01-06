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
    //sledece su funkcije za pretragu. CHATGPT IDE BRRR !
    List<Putovanje> searchPutovanje(String query);
    public List<Putovanje> searchPutovanjeByAmountRange(Long minCena, Long maxCena);
    public List<Putovanje> findSortedPutovanje(String sort, String pravac) ;

}
