package co.sansystem.orderingapp.Models;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class FavoriteModel {

    @SerializedName("IDKala")
    @Expose
    private String iDKala;
    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Name_Kala")
    @Expose
    private String nameKala;
    @SerializedName("GheymatForoshAsli")
    @Expose
    private Integer gheymatForoshAsli;
    @SerializedName("Fk_GroupKala")
    @Expose
    private Integer fkGroupKala;
    @SerializedName("Picture")
    @Expose
    private String picture;

    public String getIDKala() {
        return iDKala;
    }

    public void setIDKala(String iDKala) {
        this.iDKala = iDKala;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNameKala() {
        return nameKala;
    }

    public void setNameKala(String nameKala) {
        this.nameKala = nameKala;
    }

    public Integer getGheymatForoshAsli() {
        return gheymatForoshAsli;
    }

    public void setGheymatForoshAsli(Integer gheymatForoshAsli) {
        this.gheymatForoshAsli = gheymatForoshAsli;
    }

    public Integer getFkGroupKala() {
        return fkGroupKala;
    }

    public void setFkGroupKala(Integer fkGroupKala) {
        this.fkGroupKala = fkGroupKala;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}