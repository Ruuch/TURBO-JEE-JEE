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
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public void save(Integer id, Integer aihealue_id, String otsikko) throws SQLException {
        LocalDateTime timePoint = LocalDateTime.now();
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskusteluketju (id, aihealue_id, viestienMaara, otsikko, aikaleima) VALUES (?, ?," + "0," + "?,?);");
        stmt.setObject(1, id);
        stmt.setObject(2, aihealue_id);
        stmt.setObject(3, otsikko);
        stmt.setObject(4, timePoint.toString());
        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

}
