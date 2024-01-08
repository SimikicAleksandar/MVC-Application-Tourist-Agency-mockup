package com.example.turistickaagencija.Services;

import com.example.turistickaagencija.Daos.RezervacijaDao;
import com.example.turistickaagencija.Models.Rezervacija;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RezervacijaService {
    private final RezervacijaDao rezervacijaDao;

    public RezervacijaService(RezervacijaDao rezervacijaDao){ this.rezervacijaDao = rezervacijaDao; }

    public List<Rezervacija> findAll(){return rezervacijaDao.findAll(); }

    public void save(Rezervacija rezervacija){ rezervacijaDao.save(rezervacija);}

    public void update(Rezervacija rezervacija){ rezervacijaDao.update(rezervacija);}

    public Rezervacija get(Long id){ return rezervacijaDao.findOne(id);}

    public void delete(Long id){ rezervacijaDao.delete(id); }

    public void deleteByKupac(Long kupacId, Long id){
        rezervacijaDao.deleteByKupac(kupacId, id);
    }
    public List<Rezervacija> pretraziRezervacije(String upit) {return rezervacijaDao.pretraziRezervacije(upit); }

}
