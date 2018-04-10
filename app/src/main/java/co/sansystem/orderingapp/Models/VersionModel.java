package co.sansystem.orderingapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mohsen on 2018-01-31.
 */

public class VersionModel {

    @SerializedName("new_version")
    @Expose
    public boolean new_version;

    @SerializedName("force_dl")
    @Expose
    public boolean force_dl;

    @SerializedName("download_url")
    @Expose
    public String download_url;

    @SerializedName("download_optional_url")
    @Expose
    public String download_optional_url;
}
