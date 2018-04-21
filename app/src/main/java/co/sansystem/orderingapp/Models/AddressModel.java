package co.sansystem.orderingapp.Models;


import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class AddressModel {

    @SerializedName("Adress")
    @Expose
    private String adress;

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

}