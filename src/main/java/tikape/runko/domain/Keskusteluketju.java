package tikape.runko.domain;

public class Keskusteluketju {

    private Integer id;
    private Integer aihealueId;
    private Integer viestienMaara;
    private String aikaleima;
    private String otsikko;

    public Keskusteluketju(Integer id, Integer aihealueId, Integer viestienMaara, String otsikko, String aikaleima) {
        this.id = id;
        this.aihealueId = aihealueId;
        this.aikaleima = aikaleima;
        this.otsikko = otsikko;
        this.viestienMaara = viestienMaara;
    }

    public Integer getId() {
        return id;
    }

    public String getOtsikko() {
        return otsikko;
    }

    public Integer getAihealueId() {
        return aihealueId;
    }

    public Integer getViestienMaara() {
        return viestienMaara;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public void setAihealueId(Integer aihealueId) {
        this.aihealueId = aihealueId;
    }

    public void setViestienMaara(Integer viestienMaara) {
        this.viestienMaara = viestienMaara;
    }
    

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAikaleima() {
        return aikaleima;
    }


    public void setAikaleima(String aikaleima) {
        this.aikaleima = aikaleima;
    }



}
