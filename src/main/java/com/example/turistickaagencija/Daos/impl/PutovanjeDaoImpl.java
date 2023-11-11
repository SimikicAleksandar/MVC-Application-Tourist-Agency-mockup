package com.example.turistickaagencija.Daos.impl;

import com.example.turistickaagencija.Daos.PutovanjeDao;
import com.example.turistickaagencija.Models.*;
import com.example.turistickaagencija.Services.KategorijaPutovanjaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PutovanjeDaoImpl implements PutovanjeDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private KategorijaPutovanjaService kategorijaPutovanjaService;

    private class PutovanjeRowCallBackHandler implements RowCallbackHandler {
        private final Map<Long, Putovanje> putovanja = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1;
            Long id = rs.getLong(index++);
            PrevoznoSredstvo prevoznoSredstvo = PrevoznoSredstvo.valueOf(rs.getString(index++));
            SmestajnaJedinica smestajnaJedinica = SmestajnaJedinica.valueOf(rs.getString(index++));
            String nazivDestinacije = rs.getString(index++);
            Long kategorijaPutovanjaId = rs.getLong(index++);
            KategorijaPutovanja kategorijaPutovanja = kategorijaPutovanjaService.get(kategorijaPutovanjaId);
            LocalDateTime datumIVremePolaska = rs.getTimestamp(index++).toLocalDateTime();
            LocalDateTime datumIVremePovratka = rs.getTimestamp(index++).toLocalDateTime();
            Long brojNocenja = rs.getLong(index++);
            Long cenaAranzmana = rs.getLong(index++);
            Long ukupanBrojMesta = rs.getLong(index++);
            Long brojSlobodnihMesta = rs.getLong(index++);

            Putovanje putovanje = putovanja.get(id);
            if (putovanje == null) {
                putovanje = new Putovanje(id, prevoznoSredstvo, smestajnaJedinica, nazivDestinacije, kategorijaPutovanja, datumIVremePolaska, datumIVremePovratka, brojNocenja, cenaAranzmana, ukupanBrojMesta, brojSlobodnihMesta);
                putovanja.put(putovanje.getId(), putovanje);
            }
        }

        public List<Putovanje> getPutovanja() { return new ArrayList<>(putovanja.values());}
    }



    @Override
    public Putovanje findOne(String nazivDestinacije, Long brojNocenja) {
        String sql = "SELECT p.id, p.prevoznoSredstvo, p.smestajnaJedinica, p.nazivDestinacije, p.kategorijaPutovanjaId, p.datumIVremePolaska, p.datumIVremePovratka, p.brojNocenja, p.cenaAranzmana, p.ukupanBrojMesta, p.brojSlobodnihMesta FROM putovanja p " +
                "WHERE p.nazivDestinacije = ? AND p.brojNocenja = ? " +
                "ORDER BY p.id";
        PutovanjeDaoImpl.PutovanjeRowCallBackHandler rowCallBackHandler = new PutovanjeDaoImpl.PutovanjeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, nazivDestinacije, brojNocenja);

        if(rowCallBackHandler.getPutovanja().size() == 0){
            return null ;
        }
        return rowCallBackHandler.getPutovanja().get(0);
    }

    @Override
    public Putovanje findOne(Long id) {
        String sql = "SELECT p.id, p.prevoznoSredstvo, p.smestajnaJedinica, p.nazivDestinacije, p.kategorijaPutovanjaId, p.datumIVremePolaska, p.datumIVremePovratka, p.brojNocenja, p.cenaAranzmana, p.ukupanBrojMesta, p.brojSlobodnihMesta FROM putovanja p " +
                "WHERE p.id = ? " +
                "ORDER BY p.id";
        PutovanjeDaoImpl.PutovanjeRowCallBackHandler rowCallBackHandler = new PutovanjeDaoImpl.PutovanjeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, id);

        if(rowCallBackHandler.getPutovanja().size() == 0){
            return null ;
        }
        return rowCallBackHandler.getPutovanja().get(0);
    }

    @Override
    public List<Putovanje> findAll() {
        String sql = "SELECT p.id, p.prevoznoSredstvo, p.smestajnaJedinica, p.nazivDestinacije, p.kategorijaPutovanjaId, p.datumIVremePolaska, p.datumIVremePovratka, p.brojNocenja, p.cenaAranzmana, p.ukupanBrojMesta, p.brojSlobodnihMesta FROM putovanja p " +
                     "ORDER BY p.id";
        PutovanjeDaoImpl.PutovanjeRowCallBackHandler rowCallBackHandler = new PutovanjeDaoImpl.PutovanjeRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler);

        return rowCallBackHandler.getPutovanja();
    }
    @Transactional
    @Override
    public int save(Putovanje putovanje) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO putovanja (prevoznoSredstvo, smestajnaJedinica, nazivDestinacije, kategorijaPutovanjaId, datumIVremePolaska, datumIVremePovratka, brojNocenja, cenaAranzmana, ukupanBrojMesta, brojSlobodnihMesta) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1 ;
                preparedStatement.setString(index++, putovanje.getPrevoznoSredstvo().toString());
                preparedStatement.setString(index++, putovanje.getSmestajnaJedinica().toString());
                preparedStatement.setString(index++, putovanje.getNazivDestinacije());
                //KATEGORIJA PUTOVANJA, VALJDA OVAKO TREBA
                preparedStatement.setLong(index++, putovanje.getKategorijaPutovanja().getId());
                LocalDateTime datumIVremePolaska = putovanje.getDatumIVremePolaska();
                if (datumIVremePolaska == null) {
                    preparedStatement.setTimestamp(index++, Timestamp.valueOf(LocalDateTime.now()));
                } else {
                    preparedStatement.setTimestamp(index++, Timestamp.valueOf(datumIVremePolaska));
                }

                LocalDateTime datumIVremePovratka = putovanje.getDatumIVremePovratka();
                if(datumIVremePovratka == null) {
                    preparedStatement.setTimestamp(index++, Timestamp.valueOf(LocalDateTime.now()));
                } else {
                    preparedStatement.setTimestamp(index++, Timestamp.valueOf(datumIVremePovratka));
                }

                preparedStatement.setLong(index++, putovanje.getBrojNocenja());
                preparedStatement.setLong(index++, putovanje.getCenaAranzmana());
                preparedStatement.setLong(index++, putovanje.getUkupanBrojMesta());
                preparedStatement.setLong(index++, putovanje.getBrojSlobodnihMesta());

                return preparedStatement;

            }
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return uspeh ? 1 : 0;
    }


    @Transactional
    @Override
    public int update(Putovanje putovanje) {
        String sql = "UPDATE putovanja SET  nazivDestinacije = ?, datumIVremePolaska = ?," +
                " datumIVremePovratka = ?, brojNocenja = ?,  cenaAranzmana = ?, ukupanBrojMesta = ?, " +
                " brojSlobodnihMesta = ? WHERE id = ? ";

        boolean success = jdbcTemplate.update(
                sql,
                putovanje.getNazivDestinacije(),
                putovanje.getDatumIVremePolaska(),
                putovanje.getDatumIVremePovratka(),
                putovanje.getBrojNocenja(),
                putovanje.getCenaAranzmana(),
                putovanje.getUkupanBrojMesta(),
                putovanje.getBrojSlobodnihMesta(),
                putovanje.getId()
        ) == 1;

        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM putovanja WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
