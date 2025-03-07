package com.example.turistickaagencija.Daos;

import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Putovanje;

import java.util.List;

public interface PutovanjeDao {
    Putovanje findOne(String nazivDestinacije,Long brojNocenja);
    public Putovanje findOne(Long id);
    public List<Putovanje> findAll();
    public int save(Putovanje putovanje);
    public int update(Putovanje putovanje);
    public int delete(Long id);
    //Opet kaem ispod su funkcije za ovu pretragu. Pomozi Boze da li rade :)
    List<Putovanje> searchPutovanje(String query);
    public List<Putovanje> searchPutovanjeByAmountRange(Long minCena, Long maxCena);
    public List<Putovanje> findSortedPutovanja(String sort, String pravac) ;

}
