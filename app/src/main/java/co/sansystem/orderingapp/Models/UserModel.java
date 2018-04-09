package co.sansystem.orderingapp.Models;

/**
 * Created by Mohsen on 2018-03-18.
 */
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("Login_ID")
    @Expose
    private String loginID;
    @SerializedName("Login_Name")
    @Expose
    private String loginName;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @SerializedName("CurrencySymbol")
    @Expose
    private String currency;

    public String getLoginID() {
        return loginID;
    }

    public void setLoginID(String loginID) {
        this.loginID = loginID;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

}