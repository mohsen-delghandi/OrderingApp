package co.sansystem.orderingapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportModel {

    @SerializedName("SumKolFact")
    @Expose
    private String sumKolFact;
    @SerializedName("JamBirunbar")
    @Expose
    private String jamBirunbar;
    @SerializedName("Jampeik")
    @Expose
    private String jampeik;
    @SerializedName("JaminSalon")
    @Expose
    private String jaminSalon;
    @SerializedName("JamSumCancleFact")
    @Expose
    private String jamSumCancleFact;
    @SerializedName("mablagkhales")
    @Expose
    private String mablagkhales;

    public String getSumKolFact() {
        return sumKolFact;
    }

    public void setSumKolFact(String sumKolFact) {
        this.sumKolFact = sumKolFact;
    }

    public String getJamBirunbar() {
        return jamBirunbar;
    }

    public void setJamBirunbar(String jamBirunbar) {
        this.jamBirunbar = jamBirunbar;
    }

    public String getJampeik() {
        return jampeik;
    }

    public void setJampeik(String jampeik) {
        this.jampeik = jampeik;
    }

    public String getJaminSalon() {
        return jaminSalon;
    }

    public void setJaminSalon(String jaminSalon) {
        this.jaminSalon = jaminSalon;
    }

    public String getJamSumCancleFact() {
        return jamSumCancleFact;
    }

    public void setJamSumCancleFact(String jamSumCancleFact) {
        this.jamSumCancleFact = jamSumCancleFact;
    }

    public String getMablagkhales() {
        return mablagkhales;
    }

    public void setMablagkhales(String mablagkhales) {
        this.mablagkhales = mablagkhales;
    }

}