package com.example.turistickaagencija.Controllers;

import com.example.turistickaagencija.Exceptions.UserNotFoundException;
import com.example.turistickaagencija.Models.Putovanje;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.KorisnikService;
import com.example.turistickaagencija.Services.PutovanjeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class IndexController {
    private final KorisnikService korisnikService;
    private final PutovanjeService putovanjeService;
    public IndexController(KorisnikService korisnikService, PutovanjeService putovanjeService) {
        this.korisnikService = korisnikService;
        this.putovanjeService = putovanjeService;
    }

    @GetMapping({"/", "/index"})
    public String showIndex(Model model, HttpServletRequest request,
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
            // Filter out putovanja where brojSlobodnihMesta is not greater than 0
            list = list.stream()
                    .filter(putovanje -> putovanje.getBrojSlobodnihMesta() != null && putovanje.getBrojSlobodnihMesta() > 0)
                    .collect(Collectors.toList());
        }

        model.addAttribute("listPutovanja", list);
        model.addAttribute("newOrderBy", orderBy.equals("asc") ? "desc" : "asc");

        if (ra.getFlashAttributes().containsKey("message")) {
            String message = (String) ra.getFlashAttributes().get("message");
            model.addAttribute("message", message);
        }

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
