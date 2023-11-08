package com.example.turistickaagencija.Models;

import java.time.LocalDateTime;

public class Putovanje {
    private Long id;
    private PrevoznoSredstvo prevoznoSredstvo;
    private SmestajnaJedinica smestajnaJedinica;
    private String nazivDestinacije ;
    //slika lokacije ako ti se da da radis za sedmicu
    private KategorijaPutovanja kategorijaPutovanja;
    private LocalDateTime datumIVremePolaska;
    private LocalDateTime datumIVremePovratka;
    private Long brojNocenja;
    private Long cenaAranzmana;
    private Long ukupanBrojMesta;
    private Long brojSlobodnihMesta;

}
