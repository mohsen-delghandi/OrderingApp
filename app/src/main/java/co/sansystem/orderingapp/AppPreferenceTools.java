package co.sansystem.orderingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.sansystem.mohsen.orderingapp.R;


/**
 * for better management of preference in application
 * like authentication information
 */
public class AppPreferenceTools {

    private SharedPreferences mPreference;
    private Context mContext;
    public static final String STRING_PREF_UNAVAILABLE = "string preference unavailable";

    public AppPreferenceTools(Context context) {
        this.mContext = context;
        this.mPreference = this.mContext.getSharedPreferences("app_preference", Context.MODE_PRIVATE);
    }

    public void saveUserAuthenticationInfo(String name, String password, String id) {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.user_name), name)
                .putString(this.mContext.getString(R.string.user_password), password)
                .putString(this.mContext.getString(R.string.user_id), id)
                .apply();
    }

    public void loginOK() {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.registered), "registered")
                .apply();
    }

    public boolean isAuthorized() {
        return !mPreference.getString(this.mContext.getString(R.string.registered), STRING_PREF_UNAVAILABLE).equals(STRING_PREF_UNAVAILABLE);
    }

    public String getUserID() {
        return mPreference.getString(this.mContext.getString(R.string.user_id), "");
    }

    public void removeAllPrefs() {
        mPreference.edit().clear().apply();
    }
}
