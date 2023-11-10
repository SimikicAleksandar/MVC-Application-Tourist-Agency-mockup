package com.example.turistickaagencija.Models;

import java.io.Serializable;

public class KategorijaPutovanja implements Serializable {
    private Long id ;
    private String nazivKategorije ;
    private String opis ;

    public KategorijaPutovanja(Long id, String nazivKategorije, String opis) {
        this.id = id;
        this.nazivKategorije = nazivKategorije;
        this.opis = opis;
    }

    public KategorijaPutovanja() {
    }

    public KategorijaPutovanja(KategorijaPutovanja novaKategorija) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazivKategorije() {
        return nazivKategorije;
    }

    public void setNazivKategorije(String nazivKategorije) {
        this.nazivKategorije = nazivKategorije;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }


}
