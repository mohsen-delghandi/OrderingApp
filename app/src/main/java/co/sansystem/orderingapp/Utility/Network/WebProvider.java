package co.sansystem.orderingapp.Utility.Network;


import co.sansystem.orderingapp.Utility.Font.Font;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebProvider {

    private WebService mTService;
    private Retrofit mRetrofitClient;

    public WebProvider() {
        OkHttpClient httpClient = new OkHttpClient();

        AppPreferenceTools appPreferenceTools = new AppPreferenceTools(Font.context);

        mRetrofitClient = new Retrofit.Builder()
                .baseUrl("http://" + appPreferenceTools.getDomainName() + "/api/home/")
//                .baseUrl("http://192.168.1.6/api/home/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mTService = mRetrofitClient.create(WebService.class);
    }

    public WebService getTService() {
        return mTService;
    }

    public Retrofit getRetrofitClient() {
        return mRetrofitClient;
    }
}
