package co.sansystem.orderingapp;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebProvider {

    private WebService mTService;
    private Retrofit mRetrofitClient;

    public WebProvider() {
        OkHttpClient httpClient = new OkHttpClient();

        mRetrofitClient = new Retrofit.Builder()
                .baseUrl(ClientConfigs.API_BASE_URL)
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
