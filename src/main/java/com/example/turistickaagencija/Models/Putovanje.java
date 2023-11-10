package com.example.turistickaagencija.Models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Putovanje {
    private Long id;
    private PrevoznoSredstvo prevoznoSredstvo;
    private SmestajnaJedinica smestajnaJedinica;
    private String nazivDestinacije ;
    //slika lokacije ako ti se da da radis za sedmicu
    private KategorijaPutovanja kategorijaPutovanja;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datumIVremePolaska;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime datumIVremePovratka;
    private Long brojNocenja;
    private Long cenaAranzmana;
    private Long ukupanBrojMesta;
    private Long brojSlobodnihMesta;

    public Putovanje() {}

    public Putovanje(Long id, PrevoznoSredstvo prevoznoSredstvo, SmestajnaJedinica smestajnaJedinica, String nazivDestinacije, KategorijaPutovanja kategorijaPutovanja, LocalDateTime datumIVremePolaska, LocalDateTime datumIVremePovratka, Long brojNocenja, Long cenaAranzmana, Long ukupanBrojMesta, Long brojSlobodnihMesta) {
        this.id = id;
        this.prevoznoSredstvo = prevoznoSredstvo;
        this.smestajnaJedinica = smestajnaJedinica;
        this.nazivDestinacije = nazivDestinacije;
        this.kategorijaPutovanja = kategorijaPutovanja;
        this.datumIVremePolaska = datumIVremePolaska;
        this.datumIVremePovratka = datumIVremePovratka;
        this.brojNocenja = brojNocenja;
        this.cenaAranzmana = cenaAranzmana;
        this.ukupanBrojMesta = ukupanBrojMesta;
        this.brojSlobodnihMesta = brojSlobodnihMesta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrevoznoSredstvo getPrevoznoSredstvo() {
        return prevoznoSredstvo;
    }

    public void setPrevoznoSredstvo(PrevoznoSredstvo prevoznoSredstvo) {
        this.prevoznoSredstvo = prevoznoSredstvo;
    }

    public SmestajnaJedinica getSmestajnaJedinica() {
        return smestajnaJedinica;
    }

    public void setSmestajnaJedinica(SmestajnaJedinica smestajnaJedinica) {
        this.smestajnaJedinica = smestajnaJedinica;
    }

    public String getNazivDestinacije() {
        return nazivDestinacije;
    }

    public void setNazivDestinacije(String nazivDestinacije) {
        this.nazivDestinacije = nazivDestinacije;
    }

    public KategorijaPutovanja getKategorijaPutovanja() {
        return kategorijaPutovanja;
    }

    public void setKategorijaPutovanja(KategorijaPutovanja kategorijaPutovanja) {
        this.kategorijaPutovanja = kategorijaPutovanja;
    }

    public LocalDateTime getDatumIVremePolaska() {
        return datumIVremePolaska;
    }

    public void setDatumIVremePolaska(LocalDateTime datumIVremePolaska) {
        this.datumIVremePolaska = datumIVremePolaska;
    }

    public LocalDateTime getDatumIVremePovratka() {
        return datumIVremePovratka;
    }

    public void setDatumIVremePovratka(LocalDateTime datumIVremePovratka) {
        this.datumIVremePovratka = datumIVremePovratka;
    }

    public Long getBrojNocenja() {
        return brojNocenja;
    }

    public void setBrojNocenja(Long brojNocenja) {
        this.brojNocenja = brojNocenja;
    }

    public Long getCenaAranzmana() {
        return cenaAranzmana;
    }

    public void setCenaAranzmana(Long cenaAranzmana) {
        this.cenaAranzmana = cenaAranzmana;
    }

    public Long getUkupanBrojMesta() {
        return ukupanBrojMesta;
    }

    public void setUkupanBrojMesta(Long ukupanBrojMesta) {
        this.ukupanBrojMesta = ukupanBrojMesta;
    }

    public Long getBrojSlobodnihMesta() {
        return brojSlobodnihMesta;
    }

    public void setBrojSlobodnihMesta(Long brojSlobodnihMesta) {
        this.brojSlobodnihMesta = brojSlobodnihMesta;
    }
}
