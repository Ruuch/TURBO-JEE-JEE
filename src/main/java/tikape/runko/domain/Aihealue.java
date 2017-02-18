package tikape.runko.domain;

public class Aihealue {

    private Integer id;
    private String aihe;
    private Integer viestejaYhteensa;
    private String viimeViestinAikaleima;

    public Aihealue(Integer id, String aihe, Integer viestejaYhteensa, String viimeViestinAikaleima) {
        this.id = id;
        this.aihe = aihe;
        this.viimeViestinAikaleima = viimeViestinAikaleima;
        this.viestejaYhteensa = viestejaYhteensa;
    }

    public Integer getId() {
        return id;
    }

    public String getAihe() {
        return aihe;
    }

    public Integer getViestejaYhteensa() {
        return viestejaYhteensa;
    }

    public String getViimeViestinAikaleima() {
        return viimeViestinAikaleima;
    }

    public void setAihe(String aihe) {
        this.aihe = aihe;
    }

    public void setViestejaYhteensa(Integer viestejaYhteensa) {
        this.viestejaYhteensa = viestejaYhteensa;
    }

    public void setViimeViestinAikaleima(String viimeViestinAikaleima) {
        this.viimeViestinAikaleima = viimeViestinAikaleima;
    }

    public void setId(Integer id) {
        this.id = id;
    }





}
