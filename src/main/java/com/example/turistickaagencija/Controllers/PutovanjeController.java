package com.example.turistickaagencija.Controllers;

import com.example.turistickaagencija.Daos.RezervacijaDao;
import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.KategorijaPutovanja;
import com.example.turistickaagencija.Models.Putovanje;
import com.example.turistickaagencija.Models.Rezervacija;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.KategorijaPutovanjaService;
import com.example.turistickaagencija.Services.KorisnikService;
import com.example.turistickaagencija.Services.PutovanjeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private RezervacijaDao rezervacijaDao;
    @Autowired
    private KorisnikService korisnikService;

    @GetMapping("/putovanja") // link na koji idu putovanja
    public String showPutovanjaList(Model model, HttpServletRequest request) throws UserNotFoundException {
        List<Putovanje> listPutovanja = putovanjeService.findAll();
        Cookie[] cookies = request.getCookies();
        if(korisnikService.checkCookies(cookies, Uloga.ADMINISTRATOR)){
            model.addAttribute("uloga", "admin");
        }
        else if(korisnikService.checkCookies(cookies, Uloga.MENADZER)){
            model.addAttribute("uloga", "menadzer");
        }
        model.addAttribute("list", listPutovanja); // prosledjivanje svih kategorija
        return "putovanja";

    }
    //PRIKAZ INDEXA ZA MENADZERA
    @GetMapping("/indexZaMenadzera") // link na koji idu putovanja
    public String showMenadzerIndex(Model model, HttpServletRequest request,
                                    @RequestParam(name = "query", required = false) String query,
                                    @RequestParam(name = "order", required = false) String order,
                                    @RequestParam(name = "orderBy", required = false) String orderBy,
                                    @RequestParam(name = "minCena", required = false) Long minCena,
                                    @RequestParam(name = "maxCena", required = false) Long maxCena,
                                    RedirectAttributes ra) throws UserNotFoundException {

        if (order == null) {
            order = "id";
        }
        if (orderBy == null) {
            orderBy = "asc";
        }

        List<Putovanje> list;
        if (query != null && !query.isEmpty()) {
            list = putovanjeService.searchPutovanje(query);
        } else if (minCena != null && maxCena != null) {
            list = putovanjeService.searchPutovanjeByAmountRange(minCena, maxCena);
        } else {
            list = putovanjeService.findSortedPutovanje(order, orderBy);
        }

        model.addAttribute("listPutovanja", list);
        model.addAttribute("newOrderBy", orderBy.equals("asc") ? "desc" : "asc");

        if (ra.getFlashAttributes().containsKey("message")) {
            String message = (String) ra.getFlashAttributes().get("message");
            model.addAttribute("message", message);
        }

        Cookie[] cookies = request.getCookies();
        if (korisnikService.checkCookies(cookies, Uloga.MENADZER)) {
            return "indexZaMenadzera";
        }
        return "odbijen_pristup";
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
      try {
          putovanje.setKategorijaPutovanja(kategorijaPutovanjaService.findKategorijaPutovanjaById(putovanje.getKategorijaPutovanjaId()));

          // Calculate discounted price only if action fields are filled
          if (putovanje.getProcenatPopusta() != null && putovanje.getPocetakAkcije() != null && putovanje.getKrajAkcije() != null) {
              double cena = putovanje.getCenaAranzmana();
              double procenatPopusta = putovanje.getProcenatPopusta();

              if (procenatPopusta > 0 && procenatPopusta <= 100) {
                  double discountAmount = (cena * procenatPopusta) / 100;
                  double discountedPrice = cena - discountAmount;

                  // Store the discounted price in the Putovanje object
                  putovanje.setSnizenaCena(discountedPrice);

                  // Log the discounted price for debugging
                  System.out.println("Discounted price: " + discountedPrice);
              } else {
                  // Handle invalid discount percentage
                  redirectAttributes.addFlashAttribute("message", "Putovanje je sacuvano, ali procenat popusta nije validan.");
                  return "redirect:/putovanja";
              }
          }

          // Save or update Putovanje based on your existing logic
          if (putovanje.getId() == null) {
              putovanjeService.save(putovanje);
              redirectAttributes.addFlashAttribute("message", "Putovanje je sacuvano");
          } else {
              putovanjeService.update(putovanje);
              redirectAttributes.addFlashAttribute("message", "Putovanje je azurirano");
          }

      } catch (Exception e) {
          // Handle exceptions
          e.printStackTrace();
          redirectAttributes.addFlashAttribute("error", "Error occurred while saving the Putovanje.");
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
    public String deletePutovanje(@PathVariable("id") Long id, RedirectAttributes ra, HttpServletRequest request){
        try {
            // Check if the user has the role of a manager
            Cookie[] cookies = request.getCookies();
            if (!korisnikService.checkCookies(cookies, Uloga.MENADZER)) {
                ra.addFlashAttribute("message", "Nemate ovlašćenje za brisanje putovanja");
                return "redirect:/putovanja";
            }
            List<Rezervacija> rezervacije = rezervacijaDao.findByPutovanjeId(id);
            if (rezervacije.isEmpty()) {
                // If there are no reservations, delete the putovanje
                putovanjeService.delete(id);
                ra.addFlashAttribute("message", "Putovanje je obrisano!");
            } else {
                ra.addFlashAttribute("message", "Putovanje ne može biti obrisano jer postoje rezervacije vezane za njega.");
            }

            return "redirect:/putovanja";
        } catch (Exception e) {
            ra.addFlashAttribute("message", "Došlo je do greške prilikom brisanja putovanja");
            return "redirect:/putovanja";
        }
//        putovanjeService.delete(id);
//        ra.addFlashAttribute("message", "Putovanje je obrisano!");
//        return "redirect:/putovanja";
    }

    @GetMapping("/putovanja/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes ra, HttpServletRequest request) {

        try{
            Cookie[] cookies = request.getCookies();
            if(korisnikService.checkCookies(cookies, Uloga.ADMINISTRATOR)){
                model.addAttribute("uloga", "admin");
            }
            else if(korisnikService.checkCookies(cookies, Uloga.MENADZER)){
                model.addAttribute("uloga", "menadzer");
            }
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
