package com.example.turistickaagencija.Services;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.KategorijaPutovanja;
import com.example.turistickaagencija.Models.Korisnik;

import java.util.List;

public interface KategorijaPutovanjaService {
    KategorijaPutovanja get(Long id);
    KategorijaPutovanja get(Long id, String nazivKategorije);
    KategorijaPutovanja findKategorijaPutovanjaById(Long id) throws UserNotFoundException;
    List<KategorijaPutovanja> findAll();
    KategorijaPutovanja save(KategorijaPutovanja kategorijaPutovanja);
    KategorijaPutovanja update(KategorijaPutovanja kategorijaPutovanja);
    KategorijaPutovanja delete(Long id);
}
