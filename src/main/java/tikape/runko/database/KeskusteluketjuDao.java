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
import tikape.runko.domain.Keskusteluketju;

public class KeskusteluketjuDao implements Dao<Keskusteluketju, Integer> {

    private Database database;

    public KeskusteluketjuDao(Database database) {
        this.database = database;
    }

    @Override
    public Keskusteluketju findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskusteluketju WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer aihealueId = rs.getInt("aihealue_id");
        Integer viestienMaara = rs.getInt("viestienMaara");
        String otsikko = rs.getString("otsikko");
        String aikaleima = rs.getString("aikaleima");

        Keskusteluketju k = new Keskusteluketju(id, aihealueId, viestienMaara, otsikko, aikaleima);

        rs.close();
        stmt.close();
        connection.close();

        return k;
    }

    @Override
    public List<Keskusteluketju> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskusteluketju");

        ResultSet rs = stmt.executeQuery();
        List<Keskusteluketju> keskusteluketjut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer aihealueId = rs.getInt("aihealue_id");
            Integer viestienMaara = rs.getInt("viestienMaara");
            String otsikko = rs.getString("otsikko");
            String aikaleima = rs.getString("aikaleima");

            keskusteluketjut.add(new Keskusteluketju(id, aihealueId, viestienMaara, otsikko, aikaleima));
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskusteluketjut;
    }

    public List<Keskusteluketju> alueenKetjut(Integer aihealueId) throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskusteluketju WHERE aihealue_id = ?");
        stmt.setObject(1, aihealueId);

        ResultSet rs = stmt.executeQuery();
        List<Keskusteluketju> lista = new ArrayList<>();

        while (rs.next()) {
            Integer id = rs.getInt("id");
            Integer aihealue_id = rs.getInt("aihealue_id");
            Integer viestienMaara = rs.getInt("viestienMaara");
            String otsikko = rs.getString("otsikko");
            String aikaleima = rs.getString("aikaleima");

            lista.add(new Keskusteluketju(id, aihealue_id, viestienMaara, otsikko, aikaleima));
        }

        rs.close();
        stmt.close();
        connection.close();

        return lista;

    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }

    public void save(Integer ketjuId, Integer aihealue_id, String otsikko) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        String sDate = sdf.format(date);
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskusteluketju (id, aihealue_id, viestienMaara, otsikko, aikaleima) VALUES (?, ?," + "0," + "?,?);");
        stmt.setObject(1, ketjuId);
        stmt.setObject(2, aihealue_id);
        stmt.setObject(3, otsikko);
        stmt.setObject(4, sDate);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    public Integer generateId() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskusteluketju ORDER BY id DESC LIMIT 1");

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        Integer aihealueId = rs.getInt("aihealue_id");
        Integer viestienMaara = rs.getInt("viestienMaara");
        String otsikko = rs.getString("otsikko");
        String aikaleima = rs.getString("aikaleima");

        Keskusteluketju k = new Keskusteluketju(id, aihealueId, viestienMaara, otsikko, aikaleima);
        int newId = k.getId() + 1;

        rs.close();
        stmt.close();
        connection.close();

        return newId;
    }

    public void paivitaViestienMaara(int ketjuId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("UPDATE Keskusteluketju SET viestienMaara = viestienMaara+1 WHERE id = ?;");
        stmt.setObject(1, ketjuId);
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

//    public String getViimeisimmanViestinAikaleima(int ketjuId) throws SQLException {
//        Connection connection = database.getConnection();
//        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE ketjuId = ? ORDER BY aikaleima DESC;");
//        stmt.setObject(1, ketjuId);
//        ResultSet rs = stmt.executeQuery();
//        boolean hasOne = rs.next();
//        if (!hasOne) {
//            return null;
//        }
//        String aikaleima = rs.getString("aikaleima");
//        rs.close();
//        stmt.close();
//        connection.close();
//        return aikaleima;
//    }
}
