package com.example.turistickaagencija.Daos.impl;

import com.example.turistickaagencija.Daos.KupacDao;
import com.example.turistickaagencija.Daos.PutovanjeDao;
import com.example.turistickaagencija.Daos.RezervacijaDao;
import com.example.turistickaagencija.Models.Kupac;
import com.example.turistickaagencija.Models.Putovanje;
import com.example.turistickaagencija.Models.Rezervacija;
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
public class RezervacijaDaoImpl implements RezervacijaDao {
    private final JdbcTemplate jdbcTemplate;
    private final PutovanjeDao putovanjeDao;
    private final KupacDao kupacDao;
    public RezervacijaDaoImpl(JdbcTemplate jdbcTemplate, PutovanjeDao putovanjeDao, KupacDao kupacDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.putovanjeDao = putovanjeDao;
        this.kupacDao = kupacDao;
    }

    private class RezervacijaRowCallBackHandler implements RowCallbackHandler {
        private final Map<Long, Rezervacija> RezervacijaMap = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            LocalDateTime datumIVremeRezervacije = rs.getTimestamp(index++).toLocalDateTime();
            Long putovanjeId = rs.getLong(index++);
            Long kupacId = rs.getLong(index++);

            Putovanje putovanje = putovanjeDao.findOne(putovanjeId);
            Kupac kupac = kupacDao.findOne(kupacId);

            Rezervacija rezervacija = RezervacijaMap.get(id);
            if (rezervacija == null) {
                rezervacija = new Rezervacija(id, datumIVremeRezervacije, putovanje, kupac);
                RezervacijaMap.put(rezervacija.getId(), rezervacija);
            }
        }

        public List<Rezervacija> getRezervacije() {
            return new ArrayList<>(RezervacijaMap.values());
        }
    }

    @Override
    public Rezervacija findOne(Long id) {
        String sql = "SELECT r.id, r.datumIVremeRezervacije, putovanjeId, kupacId "
                + "FROM rezervacije r " +
                "WHERE r.id = ? " + "ORDER BY r.id ";
        RezervacijaDaoImpl.RezervacijaRowCallBackHandler rowCallBackHandler = new RezervacijaDaoImpl.RezervacijaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, id);

        if(rowCallBackHandler.getRezervacije().size() == 0){
            return null;
        }
        return rowCallBackHandler.getRezervacije().get(0);
    }

    @Override
    public List<Rezervacija> findAll(){
        String sql =
                "SELECT r.id, r.datumIVremeRezervacije, putovanjeId, kupacId "
                        + "FROM rezervacije r " + "ORDER BY r.id ";

        RezervacijaDaoImpl.RezervacijaRowCallBackHandler rowCallBackHandler = new RezervacijaDaoImpl.RezervacijaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler);

        if(rowCallBackHandler.getRezervacije().size() == 0){
            return null;
        }
        return rowCallBackHandler.getRezervacije();
    }

    @Transactional
    @Override
    public int save(Rezervacija rezervacija){
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO rezervacije (datumIVremeRezervacije, putovanjeId, kupacId) VALUES (?, ?, ?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                Timestamp timestamp = Timestamp.valueOf(rezervacija.getDatumIVremeRezervacije());
                preparedStatement.setString(index++, timestamp.toString());
                preparedStatement.setLong(index++, rezervacija.getPutovanjeId());
                preparedStatement.setLong(index++, rezervacija.getKupacId());
                return preparedStatement;
            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return uspeh ? 1 : 0;
    }

    @Override
    public int update(Rezervacija rezervacija) {
        String sql =
                "UPDATE rezervacije SET datumIVremeRezervacije = ?, putovanjeId = ?, kupacId = ? WHERE id = ? ";
        boolean uspeh = jdbcTemplate.update(sql, Timestamp.valueOf(rezervacija.getDatumIVremeRezervacije()).toString(),
                rezervacija.getPutovanjeId(), rezervacija.getKupacId(), rezervacija.getId())== 1;
        return uspeh ? 1 : 0 ;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        String sql = " DELETE FROM rezervacije WHERE id = ? ";
        return jdbcTemplate.update(sql, id);
    }

    @Transactional
    @Override
    public void deleteByKupac(Long kupacId, Long id) {
        String sql = " DELETE FROM rezervacije WHERE kupacId = ? AND id <> ? ";

        jdbcTemplate.update(sql, kupacId, id);
    }

    @Override
    public List<Rezervacija> pretraziRezervacije(String upit){
        String sql = "SELECT r.id, r.datumIVremeRezervacije, r.putovanjeId, r.kupacId " +
                "FROM rezervacije r " +
                "JOIN korisnici k ON r.kupacId = k.id " +
                "WHERE k.ime LIKE ? OR k.prezime LIKE ? or k.jmbg LIKE ? " +
                "ORDER BY r.id ";
        RezervacijaDaoImpl.RezervacijaRowCallBackHandler rowCallBackHandler = new RezervacijaDaoImpl.RezervacijaRowCallBackHandler();
        String upitU = "%" + upit + "%";
        jdbcTemplate.query(sql, rowCallBackHandler, upitU, upitU, upitU);

        return rowCallBackHandler.getRezervacije();
    }

}
