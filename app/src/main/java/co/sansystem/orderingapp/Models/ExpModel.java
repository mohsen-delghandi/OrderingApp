package co.sansystem.orderingapp.Models;

/**
 * Created by Mohsen on 2018-03-18.
 */
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ExpModel {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Mavad1")
    @Expose
    private String mavad1;
    @SerializedName("Mavad2")
    @Expose
    private String mavad2;
    @SerializedName("Mavad3")
    @Expose
    private String mavad3;
    @SerializedName("Mavad4")
    @Expose
    private String mavad4;
    @SerializedName("Mavad5")
    @Expose
    private String mavad5;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getMavad1() {
        return mavad1;
    }

    public void setMavad1(String mavad1) {
        this.mavad1 = mavad1;
    }

    public String getMavad2() {
        return mavad2;
    }

    public void setMavad2(String mavad2) {
        this.mavad2 = mavad2;
    }

    public String getMavad3() {
        return mavad3;
    }

    public void setMavad3(String mavad3) {
        this.mavad3 = mavad3;
    }

    public String getMavad4() {
        return mavad4;
    }

    public void setMavad4(String mavad4) {
        this.mavad4 = mavad4;
    }

    public String getMavad5() {
        return mavad5;
    }

    public void setMavad5(String mavad5) {
        this.mavad5 = mavad5;
    }

}