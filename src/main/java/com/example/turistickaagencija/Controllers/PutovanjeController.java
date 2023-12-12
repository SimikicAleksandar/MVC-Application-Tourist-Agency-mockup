package com.example.turistickaagencija.Controllers;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.KategorijaPutovanja;
import com.example.turistickaagencija.Models.Putovanje;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.KategorijaPutovanjaService;
import com.example.turistickaagencija.Services.KorisnikService;
import com.example.turistickaagencija.Services.PutovanjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PutovanjeController {
    @Autowired
    private PutovanjeService putovanjeService;
    @Autowired
    private KategorijaPutovanjaService kategorijaPutovanjaService;

    @Autowired
    private KorisnikService korisnikService;

    @GetMapping("/putovanja") // link na koji idu putovanja
    public String showPutovanjaList(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<Putovanje> listPutovanja = putovanjeService.findAll();
        model.addAttribute("list", listPutovanja); // prosledjivanje svih kategorija
        return "putovanja";

    }
    //PRIKAZ INDEXA ZA MENADZERA
    @GetMapping("/indexZaMenadzera") // link na koji idu putovanja
    public String showMenadzerIndex(Model model, HttpServletRequest request) throws UserNotFoundException {
        Cookie[] cookies = request.getCookies();
        List<Putovanje> listPutovanja = putovanjeService.findAll();
          if(korisnikService.checkCookies(cookies, Uloga.MENADZER)){
            model.addAttribute("uloga", "menadzer");
        }
        model.addAttribute("list", listPutovanja); // prosledjivanje svih
        return "indexZaMenadzera";

    }
    @GetMapping("/putovanja/{src}")
    public String showNewForm(@PathVariable("src") String src, Model model){
        if(src.equals("new")){
            model.addAttribute("putovanje", new Putovanje());
            model.addAttribute("redirect","/turistickaAgencija/putovanja");
        }
        List<KategorijaPutovanja> kategorijePutovanja = kategorijaPutovanjaService.findAll();
        model.addAttribute("putovanje", new Putovanje());
        model.addAttribute("method", "/putovanja/save");
        model.addAttribute("kategorijePutovanja", kategorijePutovanja);

        return "dodaj_putovanje";
    }

  /*  @PostMapping("/putovanja/save")
    public String savePutovanje(Putovanje putovanje, RedirectAttributes ra) throws UserNotFoundException {
        putovanjeService.save(putovanje);
        Putovanje novoPutovanje = putovanjeService.get(putovanje.getId());

        ra.addFlashAttribute("message", "Putovanje je sacuvano");
        return "redirect:/putovanja";
    }*/
  @PostMapping("/putovanja/save")
  public String savePutovanje(@ModelAttribute("putovanje") Putovanje putovanje, RedirectAttributes redirectAttributes) throws UserNotFoundException {
      if (putovanje.getId() == null) {
          putovanje.setKategorijaPutovanja(kategorijaPutovanjaService.findKategorijaPutovanjaById(putovanje.getKategorijaPutovanjaId()));
          putovanjeService.save(putovanje);
          redirectAttributes.addFlashAttribute("message", "Putovanje je sacuvano");
      } else {
          putovanje.setKategorijaPutovanja(kategorijaPutovanjaService.findKategorijaPutovanjaById(putovanje.getKategorijaPutovanjaId()));
          putovanjeService.update(putovanje);
          redirectAttributes.addFlashAttribute("message", "Putovanje je azurirano");
      }
      return "redirect:/putovanja";
  }


    @PostMapping ("/putovanja/update")
    public String updatePutovanja(Putovanje putovanje, RedirectAttributes ra) throws UserNotFoundException{
        Putovanje staroPutovanje = putovanjeService.get(putovanje.getId());

        putovanje.setKategorijaPutovanja(staroPutovanje.getKategorijaPutovanja());
        putovanje.setSmestajnaJedinica(staroPutovanje.getSmestajnaJedinica());

        putovanjeService.update(putovanje);
        ra.addFlashAttribute("message", "Putovanje je izmenjeno");
        return "redirect:/putovanja";
    }


    @GetMapping("/putovanja/delete/{id}")
    public String deletePutovanje(@PathVariable("id") Long id, RedirectAttributes ra){
        putovanjeService.delete(id);
        ra.addFlashAttribute("message", "Putovanje je obrisano!");
        return "redirect:/putovanja";
    }

    @GetMapping("/putovanja/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try{
            Putovanje putovanje = putovanjeService.findOne(id);
            model.addAttribute("putovanje", putovanje);
            model.addAttribute("method", "/putovanja/update");
            model.addAttribute("title", "Izmeni Putovanje (Naziv: " + putovanje.getNazivDestinacije() + ")");
            return "izmeni_putovanje";
        } catch (UserNotFoundException exception){
            ra.addFlashAttribute("message", "Kategorija nije izmenjena");
            return "redirect:/putovanja";
        }
    }






}
