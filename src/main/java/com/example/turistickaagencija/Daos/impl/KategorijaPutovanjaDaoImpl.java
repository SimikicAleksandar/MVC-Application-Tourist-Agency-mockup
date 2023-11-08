package com.example.turistickaagencija.Daos.impl;

import com.example.turistickaagencija.Daos.KategorijaPutovanjaDao;
import com.example.turistickaagencija.Daos.KorisnikDao;
import com.example.turistickaagencija.Models.KategorijaPutovanja;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.processing.Generated;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class KategorijaPutovanjaDaoImpl implements KategorijaPutovanjaDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private class KategorijaPutovanjaRowCallBackHandler implements RowCallbackHandler{
        private final Map<Long, KategorijaPutovanja> kategorijePutovanja = new LinkedHashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            int index = 1 ;
            Long id = rs.getLong(index++);
            String nazivKategorije = rs.getString(index++);
            String opis = rs.getString(index++);

            KategorijaPutovanja kategorijaPutovanja =kategorijePutovanja.get(id);
            if(kategorijaPutovanja == null){
                kategorijaPutovanja = new KategorijaPutovanja(id,nazivKategorije,opis);
                kategorijePutovanja.put(kategorijaPutovanja.getId(), kategorijaPutovanja);
            }
        }
        public List<KategorijaPutovanja> getKategorijePutovanja() {return new ArrayList<>(kategorijePutovanja.values());}
    }

    @Override
    public KategorijaPutovanja findOne(Long id) {
        String sql = "SELECT k.kategorijaPutovanjaId, k.nazivKategorije, k.opis FROM KategorijaPutovanja k " +
                "WHERE k.kategorijaPutovanjaId = ? " +
                "ORDER BY k.kategorijaPutovanjaId";
        KategorijaPutovanjaRowCallBackHandler rowCallBackHandler = new KategorijaPutovanjaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, id);

        if(rowCallBackHandler.getKategorijePutovanja().size() == 0){
            return null;
        }
        return rowCallBackHandler.getKategorijePutovanja().get(0);
    }

    @Override
    public KategorijaPutovanja findOne(Long id, String nazivKategorije) {
        String sql = "SELECT k.kategorijaPutovanjaId, k.nazivKategorije, k.opis FROM KategorijaPutovanja k " +
                "WHERE k.kategorijaPutovanjaId = ? AND k.nazivKategorije = ? " ;
        KategorijaPutovanjaRowCallBackHandler kategorijaPutovanjaRowCallBackHandler = new KategorijaPutovanjaRowCallBackHandler();
        jdbcTemplate.query(sql, kategorijaPutovanjaRowCallBackHandler, id, nazivKategorije);

        List<KategorijaPutovanja> kategorijePutovanja = kategorijaPutovanjaRowCallBackHandler.getKategorijePutovanja();
        return kategorijePutovanja.isEmpty() ? null : kategorijePutovanja.get(0);
    }

    @Override
    public KategorijaPutovanja findKategorijaPutovanjaById(Long id) {
        String sql = "SELECT k.kategorijaPutovanjaId, k.nazivKategorije, k.opis FROM KategorijaPutovanja k " +
                "WHERE k.kategorijaPutovanjaId = ? " +
                "ORDER BY k.kategorijaPutovanjaId";
        KategorijaPutovanjaRowCallBackHandler rowCallBackHandler = new KategorijaPutovanjaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler, id);

        if(rowCallBackHandler.getKategorijePutovanja().size() == 0){
            return null;
        }
        return rowCallBackHandler.getKategorijePutovanja().get(0);
    }

    @Override
    public List<KategorijaPutovanja> findAll() {
        String sql =
                "SELECT k.kategorijaPutovanjaId, k.nazivKategorije, k.opis FROM KategorijaPutovanja k " +
                        "ORDER BY k.kategorijaPutovanjaId ";
        KategorijaPutovanjaRowCallBackHandler rowCallBackHandler = new KategorijaPutovanjaRowCallBackHandler();
        jdbcTemplate.query(sql, rowCallBackHandler);

        return rowCallBackHandler.getKategorijePutovanja();
    }

    @Override
    public int save(KategorijaPutovanja kategorijaPutovanja) {
        PreparedStatementCreator preparedStatementCreator = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                String sql = "INSERT INTO KategorijaPutovanja (nazivKategorije, opis) " +
                        "VALUES(?,?) " ;

                PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                int index = 1 ;
                preparedStatement.setString(index++, kategorijaPutovanja.getNazivKategorije());
                preparedStatement.setString(index++, kategorijaPutovanja.getOpis());
                return preparedStatement;

            }
        };
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        boolean uspeh = jdbcTemplate.update(preparedStatementCreator, keyHolder) == 1 ;
        return uspeh ? 1 : 0;
    }

    @Transactional
    @Override
    public int update(KategorijaPutovanja kategorijaPutovanja) {
        String sql = "UPDATE KategorijaPutovanja SET nazivKategorije = ?, opis = ? WHERE kategorijaPutovanjaId = ? ";
        boolean uspeh = jdbcTemplate.update(sql, kategorijaPutovanja.getNazivKategorije(), kategorijaPutovanja.getOpis(), kategorijaPutovanja.getId())== 1;
        return uspeh ? 1 : 0;
    }

    @Transactional
    @Override
    public int delete(Long id) {
        String sql = "DELETE FROM KategorijaPutovanja WHERE kategorijaPutovanjaId = ? ";
        return jdbcTemplate.update(sql, id);
    }
}
