package com.example.turistickaagencija.Services.impl;

import com.example.turistickaagencija.Daos.KategorijaPutovanjaDao;
import com.example.turistickaagencija.Models.KategorijaPutovanja;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Services.KategorijaPutovanjaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseKategorijaPutovanjaServiceImpl implements KategorijaPutovanjaService {
    @Autowired
    private KategorijaPutovanjaDao kategorijaPutovanjaDao;

    @Override
    public KategorijaPutovanja get(Long id) {
        return kategorijaPutovanjaDao.findOne(id);
    }

    @Override
    public KategorijaPutovanja get(Long id, String nazivKategorije) {
        return kategorijaPutovanjaDao.findOne(id,nazivKategorije);
    }

    @Override
    public KategorijaPutovanja findKategorijaPutovanjaById(Long id) {
        return kategorijaPutovanjaDao.findKategorijaPutovanjaById(id);
    }

    @Override
    public List<KategorijaPutovanja> findAll() {
        return kategorijaPutovanjaDao.findAll();
    }

    @Override
    public KategorijaPutovanja save(KategorijaPutovanja kategorijaPutovanja) {
        kategorijaPutovanjaDao.save(kategorijaPutovanja);
        return kategorijaPutovanja;
    }

    @Override
    public KategorijaPutovanja update(KategorijaPutovanja kategorijaPutovanja) {
        kategorijaPutovanjaDao.update(kategorijaPutovanja);
        return kategorijaPutovanja;
    }

    @Override
    public KategorijaPutovanja delete(Long id) {
        KategorijaPutovanja kategorijaPutovanja = findKategorijaPutovanjaById(id);
        if(kategorijaPutovanja != null){
            kategorijaPutovanjaDao.delete(id);
        }
        return kategorijaPutovanja;
    }
}
