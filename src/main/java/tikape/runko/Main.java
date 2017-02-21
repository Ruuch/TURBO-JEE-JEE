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

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:database");
//        database.init();

        ViestiDao viestiDao = new ViestiDao(database);
             KeskusteluketjuDao keskusteluketjuDao = new KeskusteluketjuDao(database);

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
        get("/alue", (req,res) -> {
            HashMap map = new HashMap<>();
            
            Integer id = Integer.parseInt(req.queryParams("ID"));
            Aihealue alue = aihedao.findOne(id);
            
            map.put("otsikko",alue.getAihe());
            map.put("lista",ketjudao.alueenKetjut(id));
            
            return new ModelAndView(map, "alueen_ketjut");
        }, new ThymeleafTemplateEngine());

        //  Tulostaa kaikki viestit tietokannasta.
        get("/testi", (req,res) -> {
            HashMap map = new HashMap<>();
            map.put("Viesti", "heipähei");
            map.put("lista",viestidao.findAll());
            
            return new ModelAndView(map,"testi");
        }, new ThymeleafTemplateEngine());
    }
}

