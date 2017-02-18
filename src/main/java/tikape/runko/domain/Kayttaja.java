package tikape.runko.domain;

public class Kayttaja {

    private Integer id;
    private String nimi;
    private String ekaViesti;
    private Integer viestienMaara;

    public Kayttaja(Integer id, String nimi, String ekaViesti, Integer viestienMaara) {
        this.id = id;
        this.nimi = nimi;
        this.ekaViesti = ekaViesti;
        this.viestienMaara = viestienMaara;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getEkaViesti() {
        return ekaViesti;
    }

    public Integer getViestienMaara() {
        return viestienMaara;
    }

    public void setEkaViesti(String ekaViesti) {
        this.ekaViesti = ekaViesti;
    }

    public void setViestienMaara(Integer viestienMaara) {
        this.viestienMaara = viestienMaara;
    }
    
}
