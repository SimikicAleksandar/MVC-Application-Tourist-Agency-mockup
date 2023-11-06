package com.example.turistickaagencija.Models;

import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

public class Kupac {
    private Long korisnikId ;
   // private Long  rezervacije;
    private Korisnik korisnik ;

    public Kupac(Long korisnikId, Korisnik korisnik) {
        this.korisnikId = korisnikId;
        this.korisnik = korisnik;
    }


    public Kupac(Korisnik Korisnik) {
        this.korisnik = Korisnik;
        this.korisnikId = Korisnik.getId() ;
    }
    public Long getKorisnikId() {
        return korisnikId;
    }

    public void setKorisnikId(Long korisnikId) {
        this.korisnikId = korisnikId;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
