package com.example.turistickaagencija.Controllers;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.KorisnikService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Controller
public class IndexController {
    private final KorisnikService korisnikService;
    public IndexController( KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    @GetMapping("/")
    public String showIndex(Model model, HttpServletRequest request) throws UserNotFoundException {

        Cookie[] cookies = request.getCookies();

        if(korisnikService.checkCookies(cookies, Uloga.ADMINISTRATOR)){
            model.addAttribute("uloga", "admin");
        }
        else if(korisnikService.checkCookies(cookies, Uloga.MENADZER)){
            model.addAttribute("uloga", "menadzer");
        }
        else if(korisnikService.checkCookies(cookies, Uloga.KUPAC)){
            model.addAttribute("uloga", "kupac");
        } else{
            model.addAttribute("uloga", "none");
        }
        return "index";
    }
}
