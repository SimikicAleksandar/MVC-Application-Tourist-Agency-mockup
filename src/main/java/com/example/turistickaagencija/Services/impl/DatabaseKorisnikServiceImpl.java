package com.example.turistickaagencija.Services.impl;

import com.example.turistickaagencija.Daos.KorisnikDao;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Uloga;
import com.example.turistickaagencija.Services.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.List;
@Service
public class DatabaseKorisnikServiceImpl implements KorisnikService {
    @Autowired
    private KorisnikDao korisnikDao;
    @Override
    public Korisnik findOne(Long id) {

        return korisnikDao.findOne(id);
    }

    @Override
    public List<Korisnik> findAll() {
        return korisnikDao.findAll();
    }

    @Override
    public Korisnik findKorisnikByEmail(String email) {
        return korisnikDao.findKorisnikByEmail(email);
    }

    @Override
    public Korisnik findKorisnikByEmailAndPassword(String email, String lozinka) {
        return korisnikDao.findKorisnikByEmailAndPassword(email, lozinka);
    }

    public List<Korisnik> listAll(){
        return (List<Korisnik>) korisnikDao.findAll();
    }

    @Override
    public Korisnik save(Korisnik korisnik) {
        korisnikDao.save(korisnik);
        return korisnik;
    }

    @Override
    public Korisnik update(Korisnik korisnik) {
        korisnikDao.update(korisnik);
        return korisnik;
    }

    @Override
    public Korisnik delete(Long id) {
        Korisnik korisnik = findOne(id) ;
        if(korisnik != null){
            korisnikDao.delete(id);
        }
        return korisnik;
    }

    public Korisnik get(Long id) {
        return korisnikDao.findOne(id);
    }

    public Korisnik get(String emailAdresa,String lozinka) {
        return korisnikDao.findOne(emailAdresa,lozinka);
    }

    public Korisnik get(String emailAdresa){
        return korisnikDao.findOne(emailAdresa);
    }

    public boolean checkCookies(Cookie[] cookies, Uloga uloga) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                    Korisnik temp = this.get(cookie.getValue());
                    return temp.getUloga().equals(uloga);
                }
            }
        }
        return false;
    }

    public Korisnik checkCookieUser(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(cookie.getValue().contains("@")){
                    return this.get(cookie.getValue());
                }
            }
        }
        return null;
    }

}
