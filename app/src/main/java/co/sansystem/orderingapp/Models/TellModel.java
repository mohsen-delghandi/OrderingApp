package co.sansystem.orderingapp.Models;

/**
 * Created by Mohsen on 2018-03-26.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TellModel {

    @SerializedName("Tell_Contact")
    @Expose
    private String tellContact;

    public String getTellContact() {
        return tellContact;
    }

    public void setTellContact(String tellContact) {
        this.tellContact = tellContact;
    }

}