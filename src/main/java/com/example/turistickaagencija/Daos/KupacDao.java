package com.example.turistickaagencija.Daos;

import com.example.turistickaagencija.Models.Kupac;

import java.util.List;

public interface KupacDao {
    public Kupac findOne(Long id);

    public List<Kupac> findAll();

    public int save(Kupac kupac);

    public int update(Kupac kupac);

    public int delete(Long id);
}
