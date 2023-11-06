package com.example.turistickaagencija.Daos.impl;

import com.example.turistickaagencija.Daos.KorisnikDao;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Uloga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Repository
public class KorisnikDaoImpl implements KorisnikDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class KorisnikRowCallBackHandler implements RowCallbackHandler {
        private final Map<Long, Korisnik> korisnici = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Long id = resultSet.getLong(index++);
            String emailAdresa = resultSet.getString(index++);
            String lozinka = resultSet.getString(index++);
            String ime = resultSet.getString(index++);
            String prezime = resultSet.getString(index++);
            Date datumRodjenja = resultSet.getDate(index++);
            String jmbg = resultSet.getString(index++);
            String adresa = resultSet.getString(index++);
            String brojTelefona = resultSet.getString(index++);
            LocalDateTime datumIVremeRegistracije = resultSet.getTimestamp(index++).toLocalDateTime();
            Uloga uloga = Uloga.valueOf(resultSet.getString(index++));

            Korisnik korisnik = korisnici.get(id);
            if (korisnik == null) {
                korisnik = new Korisnik(id, emailAdresa, lozinka, ime, prezime, datumRodjenja, jmbg, adresa, brojTelefona, datumIVremeRegistracije, uloga);
                korisnici.put(korisnik.getId(), korisnik);
            }
        }

        public List<Korisnik> getKorisnici() {
            return new ArrayList<>(korisnici.values());
        }
    }

    @Override
    public Korisnik findOne(String emailAdresa) {
        String sql = "SELECT a.id, a.emailAdresa, a.lozinka, a.ime, a.prezime, a.datumRodjenja, a.jmbg, a.adresa, a.brojTelefona, a.datumIVremeRegistracije, a.uloga FROM korisnici a " +
                "WHERE a.emailAdresa = ? " +
                "ORDER BY a.id";
        KorisnikRowCallBackHandler rowCallBackHandler = new KorisnikRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, emailAdresa);

        if(rowCallBackHandler.getKorisnici().size() == 0){
            return null ;
        }
        return rowCallBackHandler.getKorisnici().get(0);
    }

    @Override
    public Korisnik findOne(String emailAdresa, String lozinka) {
        String sql = "SELECT a.id, a.emailAdresa, a.lozinka, a.ime, a.prezime, a.datumRodjenja, a.jmbg, a.adresa, a.brojTelefona, a.datumIVremeRegistracije, a.uloga FROM korisnici a " +
                "WHERE a.emailAdresa = ? AND " +
                "a.lozinka = ? " +
                "ORDER BY a.id";
        KorisnikRowCallBackHandler rowCallBackHandler = new KorisnikRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, emailAdresa, lozinka);

        if (rowCallBackHandler.getKorisnici().size() == 0){
            return null;
        }
        return rowCallBackHandler.getKorisnici().get(0);
    }

    @Override
    public Korisnik findOne(Long id) {
        String sql =
                "SELECT a.id, a.emailAdresa, a.lozinka, a.ime, a.prezime, a.datumRodjenja, a.jmbg, a.adresa, a.brojTelefona, a.datumIVremeRegistracije, a.uloga FROM korisnici a " +
                        "WHERE a.id = ? " +
                        "ORDER BY a.id";

        KorisnikRowCallBackHandler rowCallbackHandler = new KorisnikRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, id);

        List<Korisnik> korisnici = rowCallbackHandler.getKorisnici();
        return korisnici.isEmpty() ? null : korisnici.get(0);
    }

    @Override
    public Korisnik findKorisnikByEmail(String email) {
        String sql =
                "SELECT a.id, a.emailAdresa, a.lozinka, a.ime, a.prezime, a.datumRodjenja, a.jmbg, a.adresa, a.brojTelefona, a.datumIVremeRegistracije, a.uloga FROM korisnici a " +
                        "WHERE a.emailAdresa = ? " +
                        "ORDER BY a.id";

        KorisnikRowCallBackHandler rowCallbackHandler = new KorisnikRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email);

        List<Korisnik> korisnici = rowCallbackHandler.getKorisnici();
        return korisnici.isEmpty() ? null : korisnici.get(0);
    }

    @Override
    public Korisnik findKorisnikByEmailAndPassword(String email, String lozinka) {
        String sql =
                "SELECT a.id, a.emailAdresa, a.lozinka, a.ime, a.prezime, a.datumRodjenja, a.jmbg, a.adresa, a.brojTelefona, a.datumIVremeRegistracije, a.uloga FROM korisnici a " +
                        "WHERE a.emailAdresa = ? AND a.lozinka = ? " +
                        "ORDER BY a.id";

        KorisnikRowCallBackHandler rowCallbackHandler = new KorisnikRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, email, lozinka);

        List<Korisnik> korisnici = rowCallbackHandler.getKorisnici();
        return korisnici.isEmpty() ? null : korisnici.get(0);
    }

    @Override
    public List<Korisnik> findAll() {
        String sql =
                "SELECT a.id, a.emailAdresa, a.lozinka, a.ime, a.prezime, a.datumRodjenja, a.jmbg, a.adresa, a.brojTelefona, a.datumIVremeRegistracije, a.uloga FROM korisnici a " +
                        "ORDER BY a.id";

        KorisnikRowCallBackHandler rowCallbackHandler = new KorisnikRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        return rowCallbackHandler.getKorisnici();
    }

    @Transactional
    @Override
    public int save(Korisnik korisnik) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                String sql = "INSERT INTO korisnici (emailAdresa, lozinka, ime, prezime, datumRodjenja, jmbg, adresa, brojTelefona, datumIVremeRegistracije, uloga) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setString(index++, korisnik.getEmailAdresa());
                preparedStatement.setString(index++, korisnik.getLozinka());
                preparedStatement.setString(index++, korisnik.getIme());
                preparedStatement.setString(index++, korisnik.getPrezime());
                preparedStatement.setDate(index++, new java.sql.Date(korisnik.getDatumRodjenja().getTime()));
                preparedStatement.setString(index++, korisnik.getJmbg());
                preparedStatement.setString(index++, korisnik.getAdresa());
                preparedStatement.setString(index++, korisnik.getBrojTelefona());


                LocalDateTime datumIVremeRegistracije = korisnik.getDatumIVremeRegistracije();
                if (datumIVremeRegistracije == null) {
                    preparedStatement.setTimestamp(index++, Timestamp.valueOf(LocalDateTime.now()));
                } else {
                    preparedStatement.setTimestamp(index++, Timestamp.valueOf(datumIVremeRegistracije));
                }
                preparedStatement.setString(index++, korisnik.getUloga().toString());
                return preparedStatement;
            }
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return uspeh ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Korisnik korisnik) {
        String sql = "UPDATE korisnici SET ime = ?, prezime = ? WHERE id = ?";
        boolean uspeh = jdbcTemplate.update(sql, korisnik.getIme(), korisnik.getPrezime(), korisnik.getId()) == 1;
        return uspeh ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM korisnici WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
