package co.sansystem.orderingapp.Utility.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.sansystem.orderingapp.R;


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

    public void saveDefaultCostumerCode(String code) {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.default_customer_code), code)
                .apply();
    }

    public void saveCurrency(String currency) {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.currency), currency)
                .apply();
    }

    public void saveDomainName(String domainName) {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.domain_name), domainName)
                .apply();
    }

    public void saveSettings(String darsadTakhfif,String mablaghTakhfif,String darsadService,String mablaghService,String darsadMaliyat) {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.darsad_takhfif), darsadTakhfif)
                .putString(this.mContext.getString(R.string.darsad_maliyat), darsadMaliyat)
                .putString(this.mContext.getString(R.string.darsad_service), darsadService)
                .putString(this.mContext.getString(R.string.mablagh_service), mablaghService)
                .putString(this.mContext.getString(R.string.mablagh_takhfif), mablaghTakhfif)
                .apply();
    }

    public void saveVaziateSefaresh(String code) {
        mPreference.edit()
                .putString(this.mContext.getString(R.string.vaziate_sefaresh), code)
                .apply();
    }

    public void printAfterConfirm(boolean b) {
        mPreference.edit()
                .putBoolean(this.mContext.getString(R.string.print_after_confirm), b)
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

    public String getUserName() {
        return mPreference.getString(this.mContext.getString(R.string.user_name), "");
    }

    public String getCurrency() {
        return mPreference.getString(this.mContext.getString(R.string.currency), "");
    }

    public String getDarsadTakhfif() {
        return mPreference.getString(this.mContext.getString(R.string.darsad_takhfif), "0");
    }

    public String getDarsadService() {
        return mPreference.getString(this.mContext.getString(R.string.darsad_service), "0");
    }

    public String getDarsadMaliyat() {
        return mPreference.getString(this.mContext.getString(R.string.darsad_maliyat), "0");
    }

    public String getMablaghService() {
        return mPreference.getString(this.mContext.getString(R.string.mablagh_service), "0");
    }

    public String getMablaghTakhfif() {
        return mPreference.getString(this.mContext.getString(R.string.mablagh_takhfif), "0");
    }

    public String getDefaultCostumerCode() {
        return mPreference.getString(this.mContext.getString(R.string.default_customer_code), "");
    }

    public String getVaziatSefaresh() {
        return mPreference.getString(this.mContext.getString(R.string.vaziate_sefaresh), "");
    }

    public String getDomainName() {
        return mPreference.getString(this.mContext.getString(R.string.domain_name), "http://192.168.1.1/");
    }

    public boolean getprintAfterConfirm() {
        return mPreference.getBoolean(this.mContext.getString(R.string.print_after_confirm), false);
    }

    public void removeAllPrefs() {
        mPreference.edit().clear().apply();
    }
}
