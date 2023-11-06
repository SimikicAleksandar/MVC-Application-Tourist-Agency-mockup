package com.example.turistickaagencija.Models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

public class Korisnik {
    private Long id ;
    private String emailAdresa ;
    private String lozinka;
    private String ime;
    private String prezime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date datumRodjenja;
    private String jmbg ;
    private String adresa;
    private String brojTelefona;
    private LocalDateTime datumIVremeRegistracije;
    private Uloga uloga ;

    public Korisnik() {
        this.uloga = Uloga.KUPAC;
    }

    public Korisnik(Long id, String emailAdresa, String lozinka, String ime, String prezime, Date datumRodjenja, String jmbg, String adresa, String brojTelefona, LocalDateTime datumIVremeRegistracije, Uloga uloga) {
        this.id = id;
        this.emailAdresa = emailAdresa;
        this.lozinka = lozinka;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.jmbg = jmbg;
        this.adresa = adresa;
        this.brojTelefona = brojTelefona;
        this.datumIVremeRegistracije = datumIVremeRegistracije;
        this.uloga = uloga;
    }

    public String getEmailAdresa() {
        return emailAdresa;
    }

    public void setEmailAdresa(String emailAdresa) {
        this.emailAdresa = emailAdresa;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datumRodjenja) {
        this.datumRodjenja = datumRodjenja;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public void setBrojTelefona(String brojTelefona) {
        this.brojTelefona = brojTelefona;
    }

    public LocalDateTime getDatumIVremeRegistracije() {
        return datumIVremeRegistracije;
    }

    public void setDatumIVremeRegistracije(LocalDateTime datumIVremeRegistracije) {
        this.datumIVremeRegistracije = datumIVremeRegistracije;
    }

    public Uloga getUloga() {
        return uloga;
    }

    public void setUloga(Uloga uloga) {
        this.uloga = uloga;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", emailAdresa='" + emailAdresa + '\'' +
                ", lozinka='" + lozinka + '\'' +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", datumRodjenja=" + datumRodjenja +
                ", jmbg='" + jmbg + '\'' +
                ", adresa='" + adresa + '\'' +
                ", brojTelefona='" + brojTelefona + '\'' +
                ", datumIVremeRegistracije=" + datumIVremeRegistracije +
                ", uloga=" + uloga +
                '}';
    }
}
