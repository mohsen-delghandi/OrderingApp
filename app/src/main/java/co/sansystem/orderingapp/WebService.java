package co.sansystem.orderingapp;


import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebService {

    @GET("LoginGarson/{username}/{password}")
    Call<String> loginGarson(@Path("username") String username, @Path("password") String password);

    @GET("GetExpAshpazkhane")
    Call<Object> getExpAshpazkhane();

    @GET("GetListContact")
    Call<Object> getListContact();

    @GET("GetGroupFood")
    Call<Object> getGroupFood();

    @GET("GetFood")
    Call<Object> getFood();

    @POST("SaveFactor")
    Call<Object> saveFactor(@Body List<FactorModel> factorModelList);

    @GET("CancleFactor/{factorNumber}/{sanadNumber}/{userId}")
    Call<Boolean> deleteFactor(@Path("factorNumber") String factorNumber,@Path("sanadNumber") String sanadNumber,@Path("userId") String userId);

    @GET("GetLastFactors")
    Call<List<MiniFactorModel>> getLastFactors();

    @GET("GetSefaresh/{factorNumber}")
    Call<List<FactorModel>> getSefaresh(@Path("factorNumber") String factorNumber);
}
