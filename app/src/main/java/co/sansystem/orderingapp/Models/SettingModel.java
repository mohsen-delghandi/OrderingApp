package co.sansystem.orderingapp.Models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingModel {

    @SerializedName("Setting_DarsadMaliyat")
    @Expose
    private String settingDarsadMaliyat;
    @SerializedName("Setting_DardadTakhfif")
    @Expose
    private String settingDardadTakhfif;
    @SerializedName("Setting_MablaghTakhfif")
    @Expose
    private String settingMablaghTakhfif;
    @SerializedName("Setting_DarsadService")
    @Expose
    private String settingDarsadService;
    @SerializedName("Setting_MablaghService")
    @Expose
    private String settingMablaghService;

    public String getSettingDarsadMaliyat() {
        return settingDarsadMaliyat;
    }

    public void setSettingDarsadMaliyat(String settingDarsadMaliyat) {
        this.settingDarsadMaliyat = settingDarsadMaliyat;
    }

    public String getSettingDardadTakhfif() {
        return settingDardadTakhfif;
    }

    public void setSettingDardadTakhfif(String settingDardadTakhfif) {
        this.settingDardadTakhfif = settingDardadTakhfif;
    }

    public String getSettingMablaghTakhfif() {
        return settingMablaghTakhfif;
    }

    public void setSettingMablaghTakhfif(String settingMablaghTakhfif) {
        this.settingMablaghTakhfif = settingMablaghTakhfif;
    }

    public String getSettingDarsadService() {
        return settingDarsadService;
    }

    public void setSettingDarsadService(String settingDarsadService) {
        this.settingDarsadService = settingDarsadService;
    }

    public String getSettingMablaghService() {
        return settingMablaghService;
    }

    public void setSettingMablaghService(String settingMablaghService) {
        this.settingMablaghService = settingMablaghService;
    }

}