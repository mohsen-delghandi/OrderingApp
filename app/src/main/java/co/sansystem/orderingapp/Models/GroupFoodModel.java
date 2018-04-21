package co.sansystem.orderingapp.Models;

import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

public class GroupFoodModel {

    @SerializedName("ID_Group")
    @Expose
    private String iDGroup;
    @SerializedName("NameGroup")
    @Expose
    private String nameGroup;
    @SerializedName("TypeGroup")
    @Expose
    private String typeGroup;
    @SerializedName("ImageGroup")
    @Expose
    private String imageGroup;

    public String getIDGroup() {
        return iDGroup;
    }

    public void setIDGroup(String iDGroup) {
        this.iDGroup = iDGroup;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public String getTypeGroup() {
        return typeGroup;
    }

    public void setTypeGroup(String typeGroup) {
        this.typeGroup = typeGroup;
    }

    public String getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(String imageGroup) {
        this.imageGroup = imageGroup;
    }

}