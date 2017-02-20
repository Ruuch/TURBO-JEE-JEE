package tikape.runko.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private String databaseAddress;

    public Database(String databaseAddress) throws ClassNotFoundException {
        this.databaseAddress = databaseAddress;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(databaseAddress);
    }

    public void init() {
        List<String> lauseet = sqliteLauseet();

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        lista.add("DROP TABLE Kayttaja;");
        lista.add("DROP TABLE Aihealue;");
        lista.add("DROP TABLE Keskusteluketju;");
        lista.add("DROP TABLE Viesti;");
        
        
        lista.add("CREATE TABLE Kayttaja (\n" +
            "    id integer PRIMARY KEY,\n" +
            "    nimi varchar(20),\n" +
            "    viestienMaara integer,\n" +
            "    ekaViesti date\n" +
            ");");
        
        lista.add("CREATE TABLE Aihealue (\n" +
            "    id integer PRIMARY KEY,\n" +
            "    aihe varchar(50),\n" +
            "    viestejaYhteensa integer,\n" +
            "    viimeViestinAikaleima date\n" +
            ");");
        
        lista.add("CREATE TABLE Keskusteluketju (\n" +
            "    id integer PRIMARY KEY,\n" +
            "    aihealue_id integer,\n" +
            "    viestienMaara integer,\n" +
            "    otsikko varchar(100),\n" +
            "    aikaleima date,\n" +
            "    FOREIGN KEY(aihealue_id) REFERENCES Aihealue(id)\n" +
            ");");
        
        lista.add("CREATE TABLE Viesti (\n" +
            "     id integer,\n" +
            "     ketjuId integer,\n" +
            "    aikaleima date,\n" +
            "    sisalto varchar(500),\n" +
            "    FOREIGN KEY(id) REFERENCES Kayttaja(id),\n" +
            "    FOREIGN KEY(ketjuId) REFERENCES Keskusteluketju(id)\n" +
            ");");
        
        //Testidataa
        lista.add("INSERT INTO Kayttaja (id,nimi,viestienMaara,ekaViesti) VALUES (1,'Pertti','20','now')");
        lista.add("INSERT INTO Kayttaja (id,nimi,viestienMaara,ekaViesti) VALUES (2,'Matti','21','now')");
        lista.add("INSERT INTO Kayttaja (id,nimi,viestienMaara,ekaViesti) VALUES (3,'Katri','23','now')");
        lista.add("INSERT INTO Kayttaja (id,nimi,viestienMaara,ekaViesti) VALUES (4,'Maisa','24','now')");
        
        lista.add("INSERT INTO Aihealue (id,aihe,viestejaYhteensa, viimeViestinAikaleima) VALUES (1,'ohjelmointi',32,'now')");
        lista.add("INSERT INTO Aihealue (id,aihe,viestejaYhteensa, viimeViestinAikaleima) VALUES (3,'kissat',90,'now')");
        
        lista.add("INSERT INTO Keskusteluketju (id,aihealue_id,viestienMaara,otsikko,aikaleima) VALUES (11,3,15,'karvinen','now')");
        lista.add("INSERT INTO Keskusteluketju (id,aihealue_id,viestienMaara,otsikko,aikaleima) VALUES (12,3,40,'leopardi','now')");
        lista.add("INSERT INTO Keskusteluketju (id,aihealue_id,viestienMaara,otsikko,aikaleima) VALUES (13,3,10,'höhöhöö','now')");
        lista.add("INSERT INTO Keskusteluketju (id,aihealue_id,viestienMaara,otsikko,aikaleima) VALUES (14,1,10,'höhöhöö','now')");
        
        lista.add("INSERT INTO Viesti (id,ketjuId,aikaleima,sisalto) VALUES (1,11,'now','teksia t pertti')");
        lista.add("INSERT INTO Viesti (id,ketjuId,aikaleima,sisalto) VALUES (1,12,'now','teksia t pertti')");
        lista.add("INSERT INTO Viesti (id,ketjuId,aikaleima,sisalto) VALUES (1,13,'now','teksia t pertti')");
        lista.add("INSERT INTO Viesti (id,ketjuId,aikaleima,sisalto) VALUES (1,14,'now','teksia t pertti')");
        lista.add("INSERT INTO Viesti (id,ketjuId,aikaleima,sisalto) VALUES (2,11,'now','teksia t matti')");
        lista.add("INSERT INTO Viesti (id,ketjuId,aikaleima,sisalto) VALUES (2,13,'now','teksia t matti')");
        lista.add("INSERT INTO Viesti (id,ketjuId,aikaleima,sisalto) VALUES (4,11,'now','teksia t maisa')");

        return lista;
    }
}
