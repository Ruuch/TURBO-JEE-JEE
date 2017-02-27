package tikape.runko;

//import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KeskusteluketjuDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.database.AihealueDao;
import tikape.runko.database.KayttajaDao;
import tikape.runko.domain.Aihealue;
import tikape.runko.domain.Kayttaja;
import tikape.runko.domain.Keskusteluketju;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:database");
//        database.init();

        ViestiDao viestiDao = new ViestiDao(database);
        KeskusteluketjuDao keskusteluketjuDao = new KeskusteluketjuDao(database);
        AihealueDao aihealueDao = new AihealueDao(database);
        KayttajaDao kayttajaDao = new KayttajaDao(database);

        // Listaa kaikki keskustelualueet, sisältää linkit /alueen_ketjut/:id
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("otsikko", "Alueet");
            map.put("alueet", aihealueDao.findAll());
            map.put("aihealue", aihealueDao);

            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());

        //  ottaa vastaan sivulta etusivulta annettavan luotavan keskustelualueen
        //  nimen ja luo uuden alueen. Tämän jälkeen palauttaa etusivun uusilla alueilla.
        post("/", (req, res) -> {
            String otsikko = req.queryParams("aihe");
            aihealueDao.save(otsikko);

            HashMap map = new HashMap<>();
            map.put("otsikko", "Alueet");
            map.put("alueet", aihealueDao.findAll());

            return new ModelAndView(map, "alueet");
        }, new ThymeleafTemplateEngine());

        //  Listaa kaikki ketjut, sivu sisältää linkit /keskusteluketjut/:id
        get("/keskusteluketjut", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("keskusteluketjut", keskusteluketjuDao.findAll());

            return new ModelAndView(map, "keskusteluketjut");
        }, new ThymeleafTemplateEngine());

        // Tämän oli alunperin tarkoitus hoitaa samaa asiaa kuin alueen_ketjut, 
        // päällekkäisyys siis alunperin
        //  Kesken?
        get("/keskusteluketjut/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", viestiDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());

        //  Tulostetaan osoitteesta /alue?ID=i kaikki kyseisien id:n alueen
        //  ketjut.
        get("/alueen_ketjut/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Aihealue alue = aihealueDao.findOne(Integer.parseInt(req.params("id")));

            map.put("otsikko", alue.getAihe());
            map.put("lista", keskusteluketjuDao.alueenKetjut(alue.getId()));
            map.put("keskusteluketju", keskusteluketjuDao);

            return new ModelAndView(map, "alueen_ketjut");
        }, new ThymeleafTemplateEngine());

        //lisää uuden keskusteluketjun ja siihen ensimmäisen viestin
        Spark.post("/alueen_ketjut/:id", (req, res) -> {
            int ketjuId = keskusteluketjuDao.generateId();

            String nimi = req.queryParams("nimi");
            Kayttaja kayttaja = kayttajaDao.findOne(nimi);

            //  Jos kayttajaa tällä nimellä ei ole, luodaan uusi.
            if (kayttaja == null) {
                //  Lisättävä attribuutit
                kayttajaDao.save(nimi);
            }

            //  Vaihtoehtoisesti voidaan asettaa save() palauttamaan luotu kayttaja.
            nimi = req.queryParams("nimi");
            kayttaja = kayttajaDao.findOne(nimi);

            //  Luodaan uusi viesti kayttajan idllä.
            int viestiId = kayttaja.getId();

            keskusteluketjuDao.save(ketjuId, Integer.parseInt(req.params(":id")),
                    req.queryParams("otsikko"));
            viestiDao.save(viestiId, ketjuId, req.queryParams("viesti"));
            keskusteluketjuDao.viestienMaaraKetjussa(ketjuId);
            aihealueDao.paivitaViimeisinViestiAikaleima(Integer.parseInt(req.params(":id")), ketjuId);
            res.redirect("/alueen_ketjut/" + req.params(":id"));
            return "ok";
        });

        //  Tulostaa kaikki tietyn ketjun viestit tietokannasta.
        get("/viestit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Keskusteluketju kk = keskusteluketjuDao.findOne(Integer.parseInt(req.params("id")));
            map.put("kayttaja", kayttajaDao);
            map.put("Viesti", "heipähei");
            map.put("lista", viestiDao.ketjunViestit(kk.getId()));

            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());

        //  lisää uuden viestin, ketjun id:n mukaiseen sijaintiin
        Spark.post("/viestit/:id", (req, res) -> {
            //  Kayttajan tarkistaminen ja tarvittaessa uuden luominen, viesti id vaihdetaan tähän.
            String nimi = req.queryParams("nimi");
            Kayttaja kayttaja = kayttajaDao.findOne(nimi);

            //  Jos kayttajaa tällä nimellä ei ole, luodaan uusi.
            if (kayttaja == null) {
                //  Lisättävä attribuutit
                kayttajaDao.save(nimi);
            }

            //  Vaihtoehtoisesti voidaan asettaa save() palauttamaan luotu kayttaja.
            nimi = req.queryParams("nimi");
            kayttaja = kayttajaDao.findOne(nimi);

            //  Luodaan uusi viesti kayttajan idllä.
            int id = kayttaja.getId();
            viestiDao.save(id, Integer.parseInt(req.params(":id")), req.queryParams("viesti"));
            keskusteluketjuDao.viestienMaaraKetjussa(Integer.parseInt(req.params(":id")));
            aihealueDao.paivitaViimeisinViestiAikaleima(keskusteluketjuDao.findOne(Integer.parseInt(req.params(":id")))
                    .getAihealueId(), Integer.parseInt(req.params(":id")));
            res.redirect("/viestit/" + req.params(":id"));
            return "ok";
        });
    }
}
