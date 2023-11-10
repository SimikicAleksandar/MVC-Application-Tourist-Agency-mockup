package com.example.turistickaagencija.Daos.impl;

import com.example.turistickaagencija.Daos.KorisnikDao;
import com.example.turistickaagencija.Daos.KupacDao;
import com.example.turistickaagencija.Models.Korisnik;
import com.example.turistickaagencija.Models.Kupac;
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
public class KupacDaoImpl implements KupacDao {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private final KorisnikDao korisnikDao;

    public KupacDaoImpl(JdbcTemplate jdbcTemplate, KorisnikDao korisnikDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.korisnikDao = korisnikDao;
    }
    private class KupacRowCallbackHandler implements RowCallbackHandler {
        private final Map<Long, Kupac> Kupci = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet resultSet) throws SQLException {
            int index = 1;
            Long kupacId = resultSet.getLong(index++);

            Korisnik korisnik = korisnikDao.findOne(kupacId);

            Kupac kupac = Kupci.get(kupacId);
            if (kupac == null) {
                kupac = new Kupac(kupacId, korisnik);
                Kupci.put(kupac.getKorisnikId(), kupac);
            }
        }

        public List<Kupac> getKupci() { return new ArrayList<>(Kupci.values()); }
    }

    @Override
    public Kupac findOne(Long kupacID) {
        String sql =
                "SELECT p.korisnikId " +
                        "FROM kupci p " +
                        "WHERE p.korisnikId = ? " +
                        "ORDER BY p.korisnikId";

        KupacDaoImpl.KupacRowCallbackHandler rowCallbackHandler = new KupacDaoImpl.KupacRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler, kupacID);

        if(rowCallbackHandler.getKupci().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getKupci().get(0);
    }

    @Override
    public List<Kupac> findAll() {
        String sql =
                "SELECT p.korisnikId " +
                        "FROM kupci p " +
                        "ORDER BY p.korisnikId";

        KupacDaoImpl.KupacRowCallbackHandler rowCallbackHandler = new KupacDaoImpl.KupacRowCallbackHandler();
        jdbcTemplate.query(sql, rowCallbackHandler);

        if(rowCallbackHandler.getKupci().size() == 0) {
            return null;
        }
        return rowCallbackHandler.getKupci();
    }

    @Transactional
    @Override
    public int save(Kupac kupac) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO kupci (korisnikId) values (?)";
                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1;
                preparedStatement.setLong(index++, kupac.getKorisnikId());
                return preparedStatement;
            }
        };

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean success = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(Kupac kupac) {
        String sql = "UPDATE kupci SET  kupacId = ? WHERE korisnikId = ?";
        boolean success = jdbcTemplate.update(sql,
                kupac.getKorisnikId()) == 1;
        return success ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM kupci WHERE korisnikId = ?";
        return jdbcTemplate.update(sql, id);
    }
}
