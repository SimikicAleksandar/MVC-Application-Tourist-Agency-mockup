package com.example.turistickaagencija.Controllers;
    
import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.KategorijaPutovanja;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Kupac;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.KategorijaPutovanjaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class KategorijaPutovanjaController {
    @Autowired
    private KategorijaPutovanjaService kategorijaPutovanjaService;

    @GetMapping("/kategorije") // link na koji idu kategorije
    public String showKategorijeList(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<KategorijaPutovanja> listKategorije = kategorijaPutovanjaService.findAll();
        model.addAttribute("list", listKategorije); // prosledjivanje svih kategorija

            return "kategorije";

    }

    @GetMapping("/kategorije/{src}")
    public String showNewForm(@PathVariable("src") String src, Model model){
        if(src.equals("new")){
            model.addAttribute("kategorija", new KategorijaPutovanja());
            model.addAttribute("redirect","/turistickaAgencija/kategorije");
        }
        model.addAttribute("kategorija", new KategorijaPutovanja());
        model.addAttribute("method", "/kategorije/save");

        return "dodaj_kategorije";
    }

    @PostMapping("/kategorije/save")
    public String saveUser(KategorijaPutovanja kategorijaPutovanja, RedirectAttributes ra) throws UserNotFoundException {
        kategorijaPutovanjaService.save(kategorijaPutovanja);
        KategorijaPutovanja novaKategorija = kategorijaPutovanjaService.findKategorijaPutovanjaById(kategorijaPutovanja.getId());

        KategorijaPutovanja kategorijaPutovanja1 = new KategorijaPutovanja(novaKategorija);
        kategorijaPutovanjaService.save(kategorijaPutovanja1);

        ra.addFlashAttribute("message", "Kategorija Putovanja je sacuvana");
        return "redirect:/";
    }

    @PostMapping ("/kategorije/update")
    public String updateUser(KategorijaPutovanja kategorijaPutovanja, RedirectAttributes ra) throws UserNotFoundException{
        KategorijaPutovanja staraKategorija = kategorijaPutovanjaService.get(kategorijaPutovanja.getId());
        kategorijaPutovanjaService.update(kategorijaPutovanja) ;
        ra.addFlashAttribute("message", "Kategorija je izmenjena");
        return "redirect:/kategorije";
    }

    @GetMapping("/kategorije/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try{
            KategorijaPutovanja kategorijaPutovanja = kategorijaPutovanjaService.findKategorijaPutovanjaById(id);
            model.addAttribute("kategorija", kategorijaPutovanja);
            model.addAttribute("method", "/kategorije/update");
            model.addAttribute("title", "Izmeni Kategoriju (Naziv:" + kategorijaPutovanja.getNazivKategorije() + ")");
            return "dodaj_kategoriju";
        } catch (UserNotFoundException exception){
            ra.addFlashAttribute("message", "Kategorija nije izmenjena");
            return "redirect:/kategorije";
        }
    }


}
