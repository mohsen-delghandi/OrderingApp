package co.sansystem.orderingapp.Utility.Network;


import com.google.gson.JsonObject;

import java.util.List;

import co.sansystem.orderingapp.Models.ContactModel;
import co.sansystem.orderingapp.Models.ExpModel;
import co.sansystem.orderingapp.Models.FactorModel;
import co.sansystem.orderingapp.Models.FavoriteModel;
import co.sansystem.orderingapp.Models.FoodModel;
import co.sansystem.orderingapp.Models.GroupFoodModel;
import co.sansystem.orderingapp.Models.MiniFactorModel;
import co.sansystem.orderingapp.Models.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebService {

    @GET("LoginGarson/{username}/{password}")
    Call<List<UserModel>> loginGarson(@Path("username") String username, @Path("password") String password);

    @GET("GetExpAshpazkhane")
    Call<List<ExpModel>> getExpAshpazkhane();

    @GET("GetListContact")
    Call<List<ContactModel>> getListContact();

    @GET("GetGroupFood")
    Call<List<GroupFoodModel>> getGroupFood();

    @GET("GetFoodFavorite")
    Call<List<FavoriteModel>> getFoodFavorite();

    @GET("GetFood")
    Call<List<FoodModel>> getFood();

    @POST("SaveFactor/{factorNumber}/{fishNumber}")
    Call<Object> saveFactor(@Body List<FactorModel> factorModelList, @Path("factorNumber") String factorNumber, @Path("fishNumber") String fishNumber);

    @GET("CancleFactor/{factorNumber}/{sanadNumber}/{userId}")
    Call<Boolean> deleteFactor(@Path("factorNumber") String factorNumber,@Path("sanadNumber") String sanadNumber,@Path("userId") String userId);

    @GET("GetLastFactors")
    Call<List<MiniFactorModel>> getLastFactors();

    @GET("GetSefaresh/{factorNumber}")
    Call<List<FactorModel>> getSefaresh(@Path("factorNumber") String factorNumber);
}