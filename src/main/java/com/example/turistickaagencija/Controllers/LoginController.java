package com.example.turistickaagencija.Controllers;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.KorisnikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class LoginController {
    private final KorisnikService korisnikService;
    private final Map<String, String> ulogovaniKorisnici = new HashMap<>();

    public LoginController(KorisnikService korisnikService){
        this.korisnikService = korisnikService;
    }

    private boolean daLiJeKorisnikUlogovan(String emailAdresa) {
        return ulogovaniKorisnici.containsValue(emailAdresa);
    }


    private void setSessionCookie(HttpServletResponse response, String sessionID) {
        Cookie sessionCookie = new Cookie("sessionID", sessionID);
        sessionCookie.setMaxAge(3600);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
    }

    private String generateSessionID() {
        return UUID.randomUUID().toString();
    }

    private void setUlogovanKorisnik(String sessionID, String emailAdresa) {
        ulogovaniKorisnici.put(sessionID, emailAdresa);
    }

    @GetMapping("/login")
    public String prikaziLogin(){
        return "login";
    }

    @PostMapping("/login/save")
    public String login(@RequestParam("emailAdresa") String emailAdresa, @RequestParam("lozinka") String lozinka,
                        HttpServletResponse response, Model model,
                        @CookieValue(value = "sessionID", defaultValue = "") String sessionID)
            throws UserNotFoundException {
        Korisnik korisnik = korisnikService.get(emailAdresa, lozinka);
        if (korisnik != null){

            if(daLiJeKorisnikUlogovan(korisnik.getEmailAdresa())){
                model.addAttribute("error", "Korisnik je ulogovan");
                setSessionCookie(response, korisnik.getEmailAdresa());
                return "index";
            }

            if(sessionID.isEmpty()){
                sessionID = generateSessionID();
                setSessionCookie(response, korisnik.getEmailAdresa());
            }

            setUlogovanKorisnik(sessionID, korisnik.getEmailAdresa());
            if(korisnik.getUloga().equals(Uloga.ADMINISTRATOR)){
                return "redirect:/";
            } else if(korisnik.getUloga().equals(Uloga.MENADZER)){
                return "redirect:/";
            } else if(korisnik.getUloga().equals(Uloga.KUPAC)){
                return "redirect:/";
            }
        }
        model.addAttribute("error", "Pogresna Email Adresa ili Lozinka");
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, @CookieValue(value = "sessionID") String sessionID){

        ulogovaniKorisnici.remove(sessionID);
        Cookie sessionCookie = new Cookie("sessionID", null);
        sessionCookie.setMaxAge(0);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        return "redirect:/login";
    }

}
