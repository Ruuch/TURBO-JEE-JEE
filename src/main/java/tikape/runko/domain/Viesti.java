package tikape.runko.domain;

public class Viesti {

    private Integer id;
    private Integer ketjuId;
    private String aikaleima;
    private String sisalto;

    public Viesti(Integer id, Integer ketjuId, String aikaleima, String sisalto) {
        this.id = id;
        this.ketjuId = ketjuId;
        this.aikaleima = aikaleima;
        this.sisalto = sisalto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAikaleima() {
        return aikaleima;
    }

    public Integer getKetjuId() {
        return ketjuId;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setAikaleima(String aikaleima) {
        this.aikaleima = aikaleima;
    }

    public void setKetjuId(Integer ketjuId) {
        this.ketjuId = ketjuId;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }

}
