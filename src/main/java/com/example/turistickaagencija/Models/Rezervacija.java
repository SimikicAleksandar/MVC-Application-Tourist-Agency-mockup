package com.example.turistickaagencija.Models;

import java.time.LocalDateTime;

public class Rezervacija {
    private Long id ;
    private LocalDateTime datumIVremeRezervacije;
    private Long putovanjeId;
    private Putovanje putovanje;
    private Long kupacId;
    private Kupac kupac;

    public Rezervacija(Long id, LocalDateTime datumIVremeRezervacije, Putovanje putovanje, Kupac kupac){
        this.id = id;
        this.datumIVremeRezervacije = datumIVremeRezervacije;
        this.putovanje = putovanje;
        this.kupac = kupac;
    }

    public Rezervacija(){
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatumIVremeRezervacije() {
        return datumIVremeRezervacije;
    }

    public void setDatumIVremeRezervacije(LocalDateTime datumIVremeRezervacije) {
        this.datumIVremeRezervacije = datumIVremeRezervacije;
    }

    public Long getPutovanjeId() {
        return putovanjeId;
    }

    public void setPutovanjeId(Long putovanjeId) {
        this.putovanjeId = putovanjeId;
    }

    public Putovanje getPutovanje() {
        return putovanje;
    }

    public void setPutovanje(Putovanje putovanje) {
        this.putovanje = putovanje;
        this.putovanjeId = putovanje.getId();
    }

    public Long getKupacId() {
        return kupacId;
    }

    public void setKupacId(Long kupacId) {
        this.kupacId = kupacId;
    }

    public Kupac getKupac() {
        return kupac;
    }

    public void setKupac(Kupac kupac) {
        this.kupac = kupac;
        this.kupacId = kupac.getKorisnikId();
    }
}
