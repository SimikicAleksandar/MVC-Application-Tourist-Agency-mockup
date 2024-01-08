package com.example.turistickaagencija.Models;

import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

public class Kupac extends Korisnik{
    private Long korisnikId ;
    private Korisnik korisnik ;
    private Boolean rezervisao ;

    public Kupac(Long korisnikId, Korisnik korisnik, Boolean rezervisao) {
        this.korisnikId = korisnikId;
        this.korisnik = korisnik;
        this.rezervisao = rezervisao;
    }


    public Kupac(Korisnik Korisnik) {
        this.korisnik = Korisnik;
        this.korisnikId = Korisnik.getId() ;
        this.rezervisao = false;
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

    public boolean isRezervisao() {
        return rezervisao ;
    }

    public void setRezervisao(boolean rezervisao) {
        this.rezervisao = rezervisao;
    }
}
