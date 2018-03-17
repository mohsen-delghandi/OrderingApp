package co.sansystem.orderingapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohsen on 2018-03-17.
 */

public class MiniFactorModel {

    public String getFactor_Number() {
        return Factor_Number;
    }

    public void setFactor_Number(String factor_Number) {
        Factor_Number = factor_Number;
    }

    public String getFish_Number() {
        return Fish_Number;
    }

    public void setFish_Number(String fish_Number) {
        Fish_Number = fish_Number;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getTable_Number() {
        return Table_Number;
    }

    public void setTable_Number(String table_Number) {
        Table_Number = table_Number;
    }

    @SerializedName("ForooshKalaParent_SerialSanad")
    @Expose
    private String Sanad_Number;

    public String getSanad_Number() {
        return Sanad_Number;
    }

    public void setSanad_Number(String sanad_Number) {
        Sanad_Number = sanad_Number;
    }

    @SerializedName("ForooshKalaParent_ID")

    @Expose
    private String Factor_Number;

    @SerializedName("ForooshKalaParent_ShomareFish")
    @Expose
    private String Fish_Number;

    @SerializedName("ForooshKalaParent_TypeFact")
    @Expose
    private String Vaziat_Sefaresh;

    public String getVaziat_Sefaresh() {
        return Vaziat_Sefaresh;
    }

    public void setVaziat_Sefaresh(String vaziat_Sefaresh) {
        Vaziat_Sefaresh = vaziat_Sefaresh;
    }

    @SerializedName("ForooshKalaParent_Tahvilgirande")
    @Expose
    private String Customer_Name;

    public String getFactor_Time() {
        return Factor_Time;
    }

    public void setFactor_Time(String factor_Time) {
        Factor_Time = factor_Time;
    }

    @SerializedName("ForooshKalaParent_ShomareMiz")

    @Expose
    private String Table_Number;

    @SerializedName("ForooshKalaParent_Time")
    @Expose
    private String Factor_Time;
}
