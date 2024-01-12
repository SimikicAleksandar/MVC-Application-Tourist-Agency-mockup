package com.example.turistickaagencija.Controllers;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.*;
import com.example.turistickaagencija.Services.KorisnikService;
import com.example.turistickaagencija.Services.KupacService;
import com.example.turistickaagencija.Services.PutovanjeService;
import com.example.turistickaagencija.Services.RezervacijaService;
import net.bytebuddy.asm.Advice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class RezervacijaController {
    private final KupacService kupacService;
    private final KorisnikService korisnikService;
    private final PutovanjeService putovanjeService;
    private final RezervacijaService rezervacijaService;

    public RezervacijaController(KupacService kupacService, KorisnikService korisnikService, PutovanjeService putovanjeService, RezervacijaService rezervacijaService) {
        this.kupacService = kupacService;
        this.korisnikService = korisnikService;
        this.putovanjeService = putovanjeService;
        this.rezervacijaService = rezervacijaService;
    }

    @PostMapping("/rezervacije/new/{id}")
    public String saveRezervacija(@PathVariable("id") Long putovanjeId,
                                  @RequestParam("brojPutnika") long brojPutnika, HttpServletRequest request, RedirectAttributes redirectAttributes) throws UserNotFoundException {
        Putovanje putovanje = putovanjeService.findOne(putovanjeId);

        Cookie[] cookies = request.getCookies();
        Korisnik korisnik = korisnikService.checkCookieUser(cookies);

        if (korisnik.getUloga().equals(Uloga.KUPAC)) {
            Kupac kupac = kupacService.get(korisnik.getId());
            Rezervacija rezervacija = new Rezervacija();
            rezervacija.setDatumIVremeRezervacije(LocalDateTime.now());
            rezervacija.setBrojPutnika(brojPutnika);
            rezervacija.setKupac(kupac);
            rezervacija.setPutovanje(putovanje);
            rezervacijaService.save(rezervacija);

            redirectAttributes.addFlashAttribute("message", "PODNELI STE ZAHTEV ZA REZERVACIJU");
        } else {
            redirectAttributes.addFlashAttribute("message", "ZAO NAM JE, NISTE USPELI DA PODNESETE ZAHTEV ZA REZERVACIJU");
        }
        return "redirect:/index"; //ovde vidi gde stvarno treba da te redirektuje
        //naknadni komentar (valjda treba na index neam pojma vise)
    }

    @GetMapping("rezervacije")
    public String prikaziRezervacije(Model model, HttpServletRequest httpServletRequest,
                                     @RequestParam(name = "upit", required = false) String upit,
                                     RedirectAttributes redirectAttributes) throws UserNotFoundException {
        List<Rezervacija> lista;
        if (upit != null && !upit.isEmpty()) {
            lista = rezervacijaService.pretraziRezervacije(upit);
        } else {
            lista = rezervacijaService.findAll();
        }
        model.addAttribute("prikaziRezervacije", lista);

        if (redirectAttributes.getFlashAttributes().containsKey("message")) {
            String message = (String) redirectAttributes.getFlashAttributes().get("message");
            model.addAttribute("message", message);
        }

        Cookie[] cookies = httpServletRequest.getCookies();
        if (korisnikService.checkCookies(cookies, Uloga.MENADZER)) {
            return "rezervacije";
        }

        return "odbijen_pristup";
    }

    public boolean jeProslo(Integer minuti, LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(date.plusMinutes(minuti));
    }

    @PostMapping("rezervacije/confirm")
    public String potvrdiRezervaciju(@RequestParam("rezervacijaId") long id, @RequestParam("brojPutnika") long brojPutnika,  @RequestParam("putovanjeId") long putovanjeId,HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes)
            throws UserNotFoundException {


        Cookie[] cookies = httpServletRequest.getCookies();
        Korisnik korisnik = korisnikService.checkCookieUser(cookies);

        if (korisnik == null) {
            return "redirect:/login";
        }
        try {
            Rezervacija rezervacija = rezervacijaService.get(id);
            Kupac kupac = rezervacija.getKupac();
            Putovanje putovanje = putovanjeService.findOne(putovanjeId);

            // Update Kupac's isReservisao value to true
            kupac.setRezervisao(true);
            kupacService.update(kupac);

            // Reduce the number of available seats for the Putovanje
            putovanje.setBrojSlobodnihMesta(putovanje.getBrojSlobodnihMesta() - brojPutnika);
            putovanjeService.update(putovanje);
            rezervacijaService.delete(id);
            // Your existing logic for displaying a message
            redirectAttributes.addFlashAttribute("message", "REZERVACIJA JE POTVRĐENA");

            // Optional: You may want to update the rezervacija entity as well, depending on your data model
            //rezervacija.setPotvrdjeno(true);
           // rezervacijaService.save(rezervacija);

        } catch (EntityNotFoundException e) {
            // Handle the case where the requested reservation or entities are not found
            redirectAttributes.addFlashAttribute("message", "GREŠKA PRILIKOM POTVRĐIVANJA REZERVACIJE");
        }

        return "redirect:/rezervacije";
    }


}
