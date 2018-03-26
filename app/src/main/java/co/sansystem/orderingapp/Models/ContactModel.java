package co.sansystem.orderingapp.Models;

/**
 * Created by Mohsen on 2018-03-18.
 */
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class ContactModel {

    @SerializedName("FullName")
    @Expose
    private String fullName;

    @SerializedName("Contacts_ID")
    @Expose
    private String contactsID;

    public String getContactsID() {
        return contactsID;
    }

    public void setContactsID(String contactsID) {
        this.contactsID = contactsID;
    }

    @SerializedName("Tafzili_ID")
    @Expose
    private String tafziliID;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTafziliID() {
        return tafziliID;
    }

    public void setTafziliID(String tafziliID) {
        this.tafziliID = tafziliID;
    }

}
