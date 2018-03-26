package co.sansystem.orderingapp.Models;

/**
 * Created by Mohsen on 2018-03-26.
 */


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