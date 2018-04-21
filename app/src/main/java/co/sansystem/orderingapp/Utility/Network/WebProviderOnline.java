package co.sansystem.orderingapp.Utility.Network;


import co.sansystem.orderingapp.Utility.Font.Font;
import co.sansystem.orderingapp.Utility.Utility.AppPreferenceTools;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebProviderOnline {

    private final WebService mTService;
    private final Retrofit mRetrofitClient;

    public WebProviderOnline() {
        OkHttpClient httpClient = new OkHttpClient();

        AppPreferenceTools appPreferenceTools = new AppPreferenceTools(Font.context);

        mRetrofitClient = new Retrofit.Builder()
                .baseUrl("http://sansystem.co/api/api/")
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
