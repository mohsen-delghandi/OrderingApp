package co.sansystem.orderingapp.Models;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class FoodModel {

    @SerializedName("ID_Kala")
    @Expose
    private String iDKala;
    @SerializedName("Name_Kala")
    @Expose
    private String nameKala;
    @SerializedName("Fk_GroupKala")
    @Expose
    private String fkGroupKala;
    @SerializedName("GheymatForoshAsli")
    @Expose
    private String gheymatForoshAsli;
    @SerializedName("Picture")
    @Expose
    private String picture;

    public String getIDKala() {
        return iDKala;
    }

    public void setIDKala(String iDKala) {
        this.iDKala = iDKala;
    }

    public String getNameKala() {
        return nameKala;
    }

    public void setNameKala(String nameKala) {
        this.nameKala = nameKala;
    }

    public String getFkGroupKala() {
        return fkGroupKala;
    }

    public void setFkGroupKala(String fkGroupKala) {
        this.fkGroupKala = fkGroupKala;
    }

    public String getGheymatForoshAsli() {
        return gheymatForoshAsli;
    }

    public void setGheymatForoshAsli(String gheymatForoshAsli) {
        this.gheymatForoshAsli = gheymatForoshAsli;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}