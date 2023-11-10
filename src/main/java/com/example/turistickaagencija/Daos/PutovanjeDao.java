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
}
