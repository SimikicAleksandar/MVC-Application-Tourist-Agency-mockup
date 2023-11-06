package com.example.turistickaagencija.Services;

import com.example.turistickaagencija.Daos.KupacDao;
import com.example.turistickaagencija.Models.Kupac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KupacService {
    @Autowired
    private KupacDao kupacDao;

    public List<Kupac> listAll() {return (List<Kupac>) kupacDao.findAll(); }

    public void save(Kupac kupac){
        kupacDao.save(kupac);
    }

    public void update(Kupac kupac){
        kupacDao.update(kupac);
    }

    public Kupac get(Long id) {
        Kupac kupac = kupacDao.findOne(id);
        return kupac;
    }

    public void delete(Long id) { kupacDao.delete(id); }
}
