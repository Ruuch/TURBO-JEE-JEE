/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aihealue;

public class AihealueDao implements Dao<Aihealue, Integer> {

    private Database database;

    public AihealueDao(Database database) {
        this.database = database;
    }

    @Override
    public Aihealue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String aihe = rs.getString("aihe");
        Integer viestejaYhteensa = rs.getInt("viestejaYhteensa");
        String viimeViestinAikaleima = rs.getString("viimeViestinAikaleima");

        Aihealue a = new Aihealue(id, aihe, viestejaYhteensa, viimeViestinAikaleima);

        rs.close();
        stmt.close();
        connection.close();

        return a;
    }

    @Override
    public List<Aihealue> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue");

        ResultSet rs = stmt.executeQuery();
        List<Aihealue> aihealueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String aihe = rs.getString("aihe");
            Integer viestejaYhteensa = rs.getInt("viestejaYhteensa");
            String viimeViestinAikaleima = rs.getString("viimeViestinAikaleima");

            aihealueet.add(new Aihealue(id, aihe, viestejaYhteensa, viimeViestinAikaleima));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aihealueet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        // ei toteutettu
    }
    
    //  Lisää uuden esiintymän tauluun parametrein (id,aihe,0,0)
    public void add(String otsikko) throws SQLException {
        
        //  Koodi joka käy läpi kaikki aihealueet ja valitsee pienimmän id:n 
        //  joka ei ole käytössä
        Integer id = 0;
        
        try (Connection connection = database.getConnection()) {
            Statement st = connection.createStatement();
            
            String stat = "INSERT INTO Aihealue (id,aihe,viestejaYhteensa, viimeViestinAikaleima)"
                    + " VALUES (" + id + ",'" + otsikko + "',0,'now')";
            st.executeUpdate(stat);
            

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
        
    }

}
