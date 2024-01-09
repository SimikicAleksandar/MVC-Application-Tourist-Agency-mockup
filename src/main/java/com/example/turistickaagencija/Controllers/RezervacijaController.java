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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/rezervacije/new")
    public String saveRezervacija(@RequestParam("putovanjeId") long putovanjeId,
                                  @RequestParam("brojPutnika") long brojPutnika, HttpServletRequest request, RedirectAttributes redirectAttributes) throws UserNotFoundException{
        Putovanje putovanje = putovanjeService.findOne(putovanjeId);

        Cookie[] cookies = request.getCookies();
        Korisnik korisnik = korisnikService.checkCookieUser(cookies);

        if(korisnik.getUloga().equals(Uloga.KUPAC)){
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
                                     RedirectAttributes redirectAttributes) throws UserNotFoundException{
        List<Rezervacija> lista;
        if (upit != null && !upit.isEmpty()){
            lista = rezervacijaService.pretraziRezervacije(upit);
        }else {
            lista = rezervacijaService.findAll();
        }
        model.addAttribute("prikaziRezervacije", lista);

        if (redirectAttributes.getFlashAttributes().containsKey("message")){
            String message = (String) redirectAttributes.getFlashAttributes().get("message");
            model.addAttribute("message", message);
        }

        Cookie[] cookies = httpServletRequest.getCookies();
        if (korisnikService.checkCookies(cookies, Uloga.MENADZER)){
            return "rezervacije";
        }

        return "odbijen_pristup";
    }

    public boolean jeProslo(Integer minuti, LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(date.plusMinutes(minuti));
    }

    @PostMapping("rezervacije/confirm")
    public String potvrdiRezervaciju(@RequestParam("rezervacijaId") long id, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes)
        throws UserNotFoundException{
        Rezervacija rezervacija = rezervacijaService.get(id);

        Cookie[] cookies = httpServletRequest.getCookies();
        Korisnik korisnik = korisnikService.checkCookieUser(cookies);

        if (korisnik == null) {
            return "redirect:/login";
        }

        // Check if the user is a buyer (Kupac)
        if (korisnik.getUloga().equals(Uloga.KUPAC)) {
            // Assuming you have a form to collect the number of passengers, use @RequestParam to get the value
            // Replace "numberOfPassengers" with the actual parameter name in your HTML form
            // Example: <input type="text" name="numberOfPassengers" />
            long brojPutnika = Integer.parseInt(httpServletRequest.getParameter("brojPutnika"));

            // Update the reservation details
            rezervacija.setBrojPutnika(brojPutnika);
            // Set the reservation as confirmed
         //   rezervacija.setPotvrdjena(true); //////////////////////////////////////////////

            // Update the available seats for the corresponding trip
            Putovanje putovanje = rezervacija.getPutovanje();
            long brojSlobodnihMesta = putovanje.getBrojSlobodnihMesta();

            if (brojSlobodnihMesta >= brojPutnika) {
                putovanje.setBrojSlobodnihMesta(brojSlobodnihMesta - brojPutnika);

                // Update the 'jerezervisao' field in the Kupac entity
                Kupac kupac = (Kupac) korisnik;
                kupac.setRezervisao(true);

                putovanjeService.save(putovanje);
                rezervacijaService.save(rezervacija);

                redirectAttributes.addFlashAttribute("message", "Rezervacija potvrđena.");
            } else {
                redirectAttributes.addFlashAttribute("message", "Nema dovoljno dostupnih mesta.");
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Samo kupci mogu da rezervisu putovanje.");
        }

        return "redirect:/rezervacije";
    }








}
