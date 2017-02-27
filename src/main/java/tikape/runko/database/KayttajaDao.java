/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import tikape.runko.domain.Kayttaja;

public class KayttajaDao implements Dao<Kayttaja, Integer> {

    private Database database;

    public KayttajaDao(Database database) {
        this.database = database;
    }

    @Override
    public Kayttaja findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        String ekaViesti = rs.getString("ekaViesti");
        Integer viestienMaara = rs.getInt("viestienMaara");

        Kayttaja k = new Kayttaja(id, nimi, ekaViesti, viestienMaara);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }
    
    public Kayttaja findOne(String nimia) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja WHERE nimi = ?");
        stmt.setObject(1, nimia);
        
        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }
        
        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        String ekaViesti = rs.getString("ekaViesti");
        Integer viestienMaara = rs.getInt("viestienMaara");
        
        Kayttaja k = new Kayttaja(id, nimi, ekaViesti, viestienMaara);

        rs.close();
        stmt.close();
        connection.close();
        
        return k;
    }

    @Override
    public List<Kayttaja> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja");

        ResultSet rs = stmt.executeQuery();
        List<Kayttaja> kayttajat = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");
            String ekaViesti = rs.getString("ekaViesti");
            Integer viestienMaara = rs.getInt("viestienMaara");
            
            kayttajat.add(new Kayttaja(id, nimi, ekaViesti, viestienMaara));
        }

        rs.close();
        stmt.close();
        connection.close();

        return kayttajat;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
    public void save(String nimi) throws SQLException {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        String sDate = sdf.format(date);
        Integer id = generateId();
        Connection connection = database.getConnection();
        
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Kayttaja (id, nimi, viestienMaara, ekaViesti) "
                + "VALUES (?, ?," + "0," + "?);");
        
        stmt.setObject(1, id);
        stmt.setObject(2, nimi);
        stmt.setObject(3, sDate);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }
    
    public Integer generateId() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Kayttaja ORDER BY id DESC LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        Integer viestienMaara = rs.getInt("viestienMaara");
        String ekaViesti = rs.getString("ekaViesti");

        Kayttaja k = new Kayttaja(id, nimi, ekaViesti, viestienMaara);
        int newId = k.getId() + 1;

        rs.close();
        stmt.close();
        connection.close();

        return newId;
    }

}
