package co.sansystem.orderingapp;


import java.util.List;

import retrofit2.Call;
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

    @GET("SaveFactor/{jsonString}")
    Call<Object> saveFactor(@Path("jsonString") String jsonString);
}
