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
import java.time.LocalDateTime;
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
    public void save(String otsikko) throws SQLException {
        //  Alustetaan id.
        int id = 0;
        
        //  Käydään läpi kaikki Aihealue oliot ja valitaan suurin id.
        //  Tämän voi muuttaa niin että valitsee ensinmäisen vaapana olevan id:n
        for (Aihealue alue : findAll()) {
            if (alue.getId() > id) {
                id = alue.getId();
            }
        }
        //  Lisätään tähän 1.
        id++;
        
        
        //vaihda nimeksi save, muuta tyyli samanlaiseksi kuin keskusteluketjuDaossa
        
        try (Connection connection = database.getConnection()) {
            Statement st = connection.createStatement();
            LocalDateTime timePoint = LocalDateTime.now();
            
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Aihealue "
                    + "(id,aihe,viestejaYhteensa, viimeViestinAikaleima)"
                    + " VALUES ( ? , ? , 0, ?)");

            stmt.setObject(1, id);
            stmt.setObject(2, otsikko);
            stmt.setObject(3, timePoint);
            
            stmt.executeUpdate(); 
            stmt.close();
            st.close();
           
        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }
        
    }

}
