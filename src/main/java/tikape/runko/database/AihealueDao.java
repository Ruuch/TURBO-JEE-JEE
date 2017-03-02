package tikape.runko.database;

import java.sql.Connection;
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
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue ORDER BY upper(aihe)");

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

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO Aihealue "
                    + "(id,aihe,viestejaYhteensa, viimeViestinAikaleima)"
                    + " VALUES ( ? , ? , 0, ?)");

            stmt.setObject(1, id);
            stmt.setObject(2, otsikko);
            stmt.setObject(3, "-");

            stmt.executeUpdate();
            stmt.close();
            st.close();

        } catch (Throwable t) {
            System.out.println("Error >> " + t.getMessage());
        }

    }

    public Integer generateId() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue ORDER BY id DESC LIMIT 1");
        ResultSet rs = stmt.executeQuery();
        if (!rs.next()) {
            return null;
        }
        Integer id = rs.getInt("id");
        String aihe = rs.getString("aihe");
        Integer viestejaYhteensa = rs.getInt("viestejaYhteensa");
        String viimeViestinAikaleima = rs.getString("viimeViestinAikaleima");

        Aihealue a = new Aihealue(id, aihe, viestejaYhteensa, viimeViestinAikaleima);
        int newId = a.getId() + 1;

        rs.close();
        stmt.close();
        connection.close();

        return newId;
    }

    public void paivitaViimeisinViestiAikaleima(int aihealue_id, int ketju_id) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt1 = connection.prepareStatement("SELECT * FROM Viesti WHERE ketjuId = ? ORDER BY aikaleima DESC;");
        stmt1.setObject(1, ketju_id);
        ResultSet rs = stmt1.executeQuery();
        String aika = "-";
        if (rs.next()) {
            aika = rs.getString("aikaleima");
        }

        PreparedStatement stmt2 = connection.prepareStatement("UPDATE Aihealue SET viimeViestinAikaleima = ? WHERE id = ?;");
        stmt2.setObject(1, aika);
        stmt2.setObject(2, aihealue_id);
        stmt2.executeUpdate();
        stmt2.close();
        connection.close();
    }

    public int viestienMaaraAlueella(int AiheId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt1 = connection.prepareStatement("SELECT Viesti.id, COUNT(Viesti.id) AS lkm FROM Viesti, Keskusteluketju, Aihealue WHERE Aihealue.id = ? AND Aihealue.id = Keskusteluketju.aihealue_id AND Keskusteluketju.id = Viesti.ketjuId;");
        stmt1.setObject(1, AiheId);
        ResultSet rs = stmt1.executeQuery();
        int maara = 0;
        if (rs.next()) {
            maara = rs.getInt("lkm");
        }
        PreparedStatement stmt2 = connection.prepareStatement("UPDATE Aihealue SET viestejaYhteensa = ? WHERE id = ?;");
        stmt2.setObject(1, maara);
        stmt2.setObject(2, AiheId);
        stmt2.executeUpdate();
        stmt2.close();
        connection.close();

        return maara;
    }
}
