package tikape.runko;

//import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Spark;
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

        // Listaa kaikki keskustelualueet, sisältää linkit /alueen_ketjut/:id
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("otsikko", "Alueet");
            map.put("alueet", aihealueDao.findAll());

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
        // Viimeisimpien viestien aikaleimat voidaan lähettää tätä kautta jos 
        // ei lisätä tietokantaan uutta saraketta.
        get("/alueen_ketjut/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Aihealue alue = aihealueDao.findOne(Integer.parseInt(req.params("id")));
            List<Keskusteluketju> keskusteluketjut = keskusteluketjuDao.alueenKetjut(alue.getId());
            map.put("otsikko", alue.getAihe());
            map.put("lista", keskusteluketjut);
//            List<String> aikaleimat = new ArrayList<>();
//            for (Keskusteluketju kk : keskusteluketjut) {
//                String aikaleima = keskusteluketjuDao.getViimeisimmanViestinAikaleima(kk.getId());
//                if (aikaleima == null) {
//                    aikaleimat.add("Ei viestejä");
//                }
//                aikaleimat.add(aikaleima);
//            }
//            map.put("aikaleimat", aikaleimat);

            return new ModelAndView(map, "alueen_ketjut");
        }, new ThymeleafTemplateEngine());

        //lisää uuden keskusteluketjun ja siihen ensimmäisen viestin
        Spark.post("/alueen_ketjut/:id", (req, res) -> {
            int ketjuId = keskusteluketjuDao.generateId();
            int viestiId = viestiDao.generateId();
            keskusteluketjuDao.save(ketjuId, Integer.parseInt(req.params(":id")),
                    req.queryParams("otsikko"));
            viestiDao.save(viestiId, ketjuId, req.queryParams("viesti"));
            keskusteluketjuDao.paivitaViestienMaara(ketjuId);
            aihealueDao.paivitaViimeisinViesti(viestiId, req.params(":id"));
            res.redirect("/alueen_ketjut/" + req.params(":id"));
            return "ok";
        });

        //  Tulostaa kaikki tietyn ketjun viestit tietokannasta.
        get("/viestit/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            Keskusteluketju kk = keskusteluketjuDao.findOne(Integer.parseInt(req.params("id")));
            map.put("Viesti", "heipähei");
            map.put("lista", viestiDao.ketjunViestit(kk.getId()));

            return new ModelAndView(map, "viestit");
        }, new ThymeleafTemplateEngine());

        //lisää uuden viestin, ketjun id:n mukaiseen sijaintiin
        Spark.post("/viestit/:id", (req, res) -> {
            int id = viestiDao.generateId();
            viestiDao.save(id, Integer.parseInt(req.params(":id")), req.queryParams("viesti"));
            keskusteluketjuDao.paivitaViestienMaara(Integer.parseInt(req.params(":id")));
            aihealueDao.paivitaViimeisinViesti(keskusteluketjuDao.findOne(Integer.parseInt(req.params(":id"))).getAihealueId(), viestiDao.findOne(id).getAikaleima());
            res.redirect("/viestit/" + req.params(":id"));
            return "ok";
        });
    }
}
