package com.example.turistickaagencija.Controllers;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.Kupac;
import com.example.turistickaagencija.Services.KorisnikService;
import com.example.turistickaagencija.Services.KupacService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class KupacController {
    private final KupacService kupacService;

    private final KorisnikService korisnikService;

    public KupacController(KupacService kupacService, KorisnikService korisnikService){
        this.kupacService = kupacService;
        this.korisnikService = korisnikService;
    }

    @GetMapping("/kupci")
    public String showPacijentiList(Model model) throws UserNotFoundException {
        List<Kupac> list = kupacService.listAll();
        model.addAttribute("listKupci", list);
        return "kupci";
    }

    @PostMapping("/kupci/update")
    public String updatePacijent(Kupac kupac, RedirectAttributes redirectAttributes) {
        kupacService.update(kupac);
        redirectAttributes.addFlashAttribute("message", "Kupac je izmenjen");
        return "redirect:/kupci";
    }

    @GetMapping("/kupci/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model){
        Kupac kupac = kupacService.get(id);
        model.addAttribute("kupac", kupac);
        model.addAttribute("naslov",
                "Izmeni pacijenta (ime:" + kupac.getKorisnik().getIme() + ")");
        return "dodaj_kupca";
    }

    @GetMapping("/kupci/delete/{id}")
    public String deletePacijent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes){
        kupacService.delete(id);
        korisnikService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Kupac je obrisan");
        return "redirect:/kupci";
    }

}
