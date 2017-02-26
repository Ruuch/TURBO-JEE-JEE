/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Viesti;

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer ketjuId = rs.getInt("ketjuId");
        String aikaleima = rs.getString("aikaleima");
        String sisalto = rs.getString("sisalto");

        Viesti v = new Viesti(id, ketjuId, aikaleima, sisalto);

        rs.close();
        stmt.close();
        connection.close();

        return v;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer ketjuId = rs.getInt("ketjuId");
            String aikaleima = rs.getString("aikaleima");
            String sisalto = rs.getString("sisalto");

            viestit.add(new Viesti(id, ketjuId, aikaleima, sisalto));
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    public List<Viesti> ketjunViestit(Integer ketju_id) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE ketjuId = ?");
        stmt.setObject(1, ketju_id);

        ResultSet rs = stmt.executeQuery();
        List<Viesti> lista = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer ketjuId = rs.getInt("ketjuId");
            String aikaleima = rs.getString("aikaleima");
            String sisalto = rs.getString("sisalto");

            lista.add(new Viesti(id, ketjuId, aikaleima, sisalto));
        }

        rs.close();
        stmt.close();
        connection.close();

        return lista;

    }

    public Integer generateId() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti ORDER BY id DESC LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        Integer id = rs.getInt("id");
        Integer ketjuId = rs.getInt("ketjuId");
        String aikaleima = rs.getString("aikaleima");
        String sisalto = rs.getString("sisalto");

        Viesti v = new Viesti(id, ketjuId, aikaleima, sisalto);
        int newId = v.getId() + 1;

        rs.close();
        stmt.close();
        connection.close();

        return newId;
    }

    public void save(Integer id, Integer ketjuId, String sisalto) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        String sDate = sdf.format(date);
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viesti (id, ketjuId, aikaleima, sisalto) VALUES (?, ?, ?, ?);");
        stmt.setObject(1, id);
        stmt.setObject(2, ketjuId);
        stmt.setObject(3, sDate);
        stmt.setObject(4, sisalto);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
}
