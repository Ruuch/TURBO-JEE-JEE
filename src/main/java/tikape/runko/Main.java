package tikape.runko;

import java.util.HashMap;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KeskusteluketjuDao;
import tikape.runko.database.ViestiDao;
import tikape.runko.database.AihealueDao;
import tikape.runko.domain.Aihealue;
import tikape.runko.domain.Keskusteluketju;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:database");
//        database.init();

        ViestiDao viestiDao = new ViestiDao(database);
        KeskusteluketjuDao keskusteluketjuDao = new KeskusteluketjuDao(database);
        AihealueDao aihealueDao = new AihealueDao(database);

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("viesti", "tervehdys");

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/keskusteluketjut", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("keskusteluketjut", keskusteluketjuDao.findAll());

            return new ModelAndView(map, "keskusteluketjut");
        }, new ThymeleafTemplateEngine());

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

            return new ModelAndView(map, "alueen_ketjut");
        }, new ThymeleafTemplateEngine());

        //  Tulostaa kaikki tietyn ketjun viestit tietokannasta.
        get("/viestit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Keskusteluketju kk = keskusteluketjuDao.findOne(Integer.parseInt(req.params("id")));
            map.put("Viesti", "heipähei");
            map.put("lista", viestiDao.ketjunViestit(kk.getId()));

            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());
    }
}
