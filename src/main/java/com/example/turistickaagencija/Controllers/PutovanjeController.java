package com.example.turistickaagencija.Controllers;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.KategorijaPutovanja;
import com.example.turistickaagencija.Models.Putovanje;
import com.example.turistickaagencija.Services.PutovanjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class PutovanjeController {
    @Autowired
    private PutovanjeService putovanjeService;

    @GetMapping("/putovanja") // link na koji idu putovanja
    public String showPutovanjaList(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<Putovanje> listPutovanja = putovanjeService.findAll();
        model.addAttribute("list", listPutovanja); // prosledjivanje svih kategorija
        return "putovanja";

    }
    @GetMapping("/putovanja/{src}")
    public String showNewForm(@PathVariable("src") String src, Model model){
        if(src.equals("new")){
            model.addAttribute("putovanje", new Putovanje());
            model.addAttribute("redirect","/turistickaAgencija/putovanja");
        }
        model.addAttribute("putovanje", new KategorijaPutovanja());
        model.addAttribute("method", "/putovanja/save");

        return "dodaj_putovanje";
    }

    @PostMapping("/putovanja/save")
    public String savePutovanje(Putovanje putovanje, RedirectAttributes ra) throws UserNotFoundException {
        putovanjeService.save(putovanje);
        Putovanje novoPutovanje = putovanjeService.get(putovanje.getId());

        ra.addFlashAttribute("message", "Putovanje je sacuvano");
        return "redirect:/putovanja";
    }


    @PostMapping ("/putovanja/update")
    public String updatePutovanje(Putovanje putovanje, RedirectAttributes ra) throws UserNotFoundException{
        Putovanje staroPutovanje = putovanjeService.get(putovanje.getId());
        if(putovanje.getNazivDestinacije().isEmpty() || putovanje.getNazivDestinacije()==null){
            putovanje.setNazivDestinacije(staroPutovanje.getNazivDestinacije());
        }
        putovanjeService.update(putovanje);
        ra.addFlashAttribute("message", "putovanje je izmenjeno");
        return "redirect:/putovanja";
     /*   kategorijaPutovanjaService.update(kategorijaPutovanja) ;
        ra.addFlashAttribute("message", "Kategorija je izmenjena");
        return "redirect:/kategorije";
      */
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
            return "dodaj_putovanje";
        } catch (UserNotFoundException exception){
            ra.addFlashAttribute("message", "Kategorija nije izmenjena");
            return "redirect:/putovanja";
        }
    }






}
