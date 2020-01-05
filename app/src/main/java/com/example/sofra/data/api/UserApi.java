package com.example.sofra.data.api;

import com.example.sofra.data.model.restaurants.Categories;
import com.example.sofra.data.model.client.Client;
import com.example.sofra.data.model.notifications.Notifications;
import com.example.sofra.data.model.offers.Offers;
import com.example.sofra.data.model.orders.Orders;
import com.example.sofra.data.model.generalresponse.GeneralResponse;
import com.example.sofra.data.model.restaurants.RestaurantDetails;
import com.example.sofra.data.model.restaurants.Restaurants;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserApi {
    //TODO
    @POST("client/login")
    @FormUrlEncoded
    Call<Client> loginUser(@Field("email") String email, @Field(("password")) String password);

    //    //TODO
    @POST("client/sign-up")
    @Multipart
    Call<Client> registerUser(@Part("name") RequestBody name,
                              @Part("email") RequestBody email,
                              @Part("password") RequestBody password,
                              @Part("password_confirmation") RequestBody password_confirmation,
                              @Part("phone") RequestBody phone,
                              @Part("region_id") RequestBody region_id,
                              @Part MultipartBody.Part profile_image);

    //TODO
    @GET("cities")
    Call<GeneralResponse> getCities();

    //TODO
    @GET("regions")
    Call<GeneralResponse> getRegions(@Query("city_id") int city_id);

    @POST("client/reset-password")
    @FormUrlEncoded
    Call<GeneralResponse> resetPassword(@Field("email") String String);

    @POST("client/new-password")
    @FormUrlEncoded
    Call<GeneralResponse> newPassword(@Field("code") String code,
                                      @Field("password") String password,
                                      @Field("password_confirmation") String password_confirmation);

    //TODO
    @GET("restaurants")
    Call<Restaurants> getAllResturants(@Query("page") int page);

    //TODO
    @GET("offers?")
    Call<Offers> getOffers(@Query("restaurant_id") int restaurant_id);

    ///TODO
    @GET("restaurant/reviews")
    Call<Offers> getReviews(@Query("api_token") String api_token,
                            @Query("restaurant_id") int restaurant_id);

    //TODO
    @POST("client/restaurant/review")
    @FormUrlEncoded
    Call<GeneralResponse> rateResturant(@Field("rate") int rate,
                                        @Field("comment") String comment,
                                        @Field("restaurant_id") int restaurant_id,
                                        @Field("api_token") String api_token);

    //TODO
    @GET("client/my-orders")
    Call<Orders> getMyOrders(@Query("api_token") String api_token,
                             @Query("state") String state,
                             @Query("page") int page);

    //TODO
    @POST("restaurant/login")
    @FormUrlEncoded
    Call<Client> loginRestaurant(@Field("email") String email, @Field(("password")) String password);

    //TODO
    @POST("restaurant/new-category")
    @Multipart
    Call<GeneralResponse> addCategory(@Part("name") RequestBody name,
                                      @Part MultipartBody.Part profile_image,
                                      @Part("api_token") RequestBody api_token);

    //TODO
    @GET("restaurant/my-items")
    Call<Offers> getMyItems(@Query("api_token") String api_token,
                            @Query("category_id") int category_id);

    //TODO
    @GET("restaurant/my-orders")
    Call<Orders> getMyRestaurantOrders(@Query("api_token") String api_token,
                                       @Query("state") String state,
                                       @Query("page") int page);

    //TODO
//    @GET("new-offers")
//    Call<Offers> getMyOffers(@Query("api_token") String api_token,
//                             @Query("page") int page);
    //TODO
    @GET("settings")
    Call<GeneralResponse> getAppSetting();

    //TODO
    @GET("restaurant/my-offers")
    Call<Offers> getMyRestaurantOffers(@Query("api_token") String api_token,
                                       @Query("page") int page);

    //TODO
    @POST("restaurant/new-offer")
    @Multipart
    Call<GeneralResponse> addOffer(@Part("description") RequestBody description,
                                   @Part("price") RequestBody price,
                                   @Part("starting_at") RequestBody starting_at,
                                   @Part("name") RequestBody name,
                                   @Part MultipartBody.Part photo,
                                   @Part("ending_at") RequestBody ending_at,
                                   @Part("api_token") RequestBody api_token,
                                   @Part("offer_price") RequestBody offer_price);

    //TODO
    @POST("restaurant/delete-category")
    @FormUrlEncoded
    Call<GeneralResponse> deleteCategory(@Field("api_token") String api_token,
                                         @Field("category_id") int category_id
    );

    //TODO
    @POST("restaurant/update-category")
    @Multipart
    Call<GeneralResponse> updateCategory(@Part("name") RequestBody name,
                                         @Part MultipartBody.Part photo,
                                         @Part("api_token") RequestBody api_token,
                                         @Part("category_id") RequestBody category_id);

    @POST("restaurant/accept-order")
    @FormUrlEncoded
    Call<GeneralResponse> acceptOrder(@Field("api_token") String api_token,
                                      @Field("order_id") int order_id
    );

    @POST("restaurant/confirm-order")
    @FormUrlEncoded
    Call<GeneralResponse> confirmOrder(@Field("api_token") String api_token,
                                       @Field("order_id") int order_id
    );

    //TODO
    @POST("restaurant/reject-order")
    @FormUrlEncoded
    Call<GeneralResponse> rejectOrder(@Field("api_token") String api_token,
                                      @Field("order_id") int order_id
    );

    //TODO
    @POST("contact")
    @FormUrlEncoded
    Call<GeneralResponse> contactUs(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("phone") String phone,
                                    @Field("type") String type,
                                    @Field("content") String content
    );

    //TODO
    @GET("client/notifications")
    Call<Notifications> getNotifications(@Query("api_token") String api_token);

    //TODO
    @GET("restaurant/notifications")
    Call<Notifications> getRestNotifications(@Query("api_token") String api_token);

    //TODO
    @POST("client/change-password")
    @FormUrlEncoded
    Call<GeneralResponse> changeClientPassword(@Field("api_token") String api_token,
                                               @Field("old_password") String old_password,
                                               @Field("password") String password,
                                               @Field("password_confirmation") String password_confirmation);

    //TODO
    @POST("restaurant/change-password")
    @FormUrlEncoded
    Call<GeneralResponse> changeRestPassword(@Field("api_token") String api_token,
                                             @Field("old_password") String old_password,
                                             @Field("password") String password,
                                             @Field("password_confirmation") String password_confirmation);

    @POST("restaurant/delete-offer")
    @FormUrlEncoded
    Call<GeneralResponse> deleteOffer(@Field("offer_id") int offer_id,
                                      @Field("api_token") String api_token
    );

    //TODO
    @POST("restaurant/update-offer")
    @Multipart
    Call<GeneralResponse> updateOffer(@Part("description") RequestBody description,
                                      @Part("price") RequestBody price,
                                      @Part("starting_at") RequestBody starting_at,
                                      @Part("name") RequestBody name,
                                      @Part MultipartBody.Part photo,
                                      @Part("ending_at") RequestBody ending_at,
                                      @Part("offer_id") RequestBody offer_id,
                                      @Part("api_token") RequestBody api_token,
                                      @Part("offer_price") RequestBody offer_price);

    @GET("categories")
    Call<Categories> getCategories(@Query("restaurant_id") int restaurant_id);

    //TODO
    @GET("items")
    Call<Offers> getItems(@Query("restaurant_id") int restaurant_id,
                          @Query("category_id") int category_id);

    @POST("client/new-order")
    @FormUrlEncoded
    Call<GeneralResponse> addNewOrder(@Field("restaurant_id") int restaurant_id,
                                      @Field("note") String note,
                                      @Field("address") String address,
                                      @Field("payment_method_id") int payment_method_id,
                                      @Field("phone") String phone,
                                      @Field("name") String name,
                                      @Field("api_token") String api_token,
                                      @Field("items[]") List<Integer> items,
                                      @Field("quantities[]") List<Integer> quantities,
                                      @Field("notes[]") List<String> notes);

    @POST("restaurant/sign-up")
    @Multipart
    Call<GeneralResponse> registerRestaurant(@Part("name") RequestBody name,
                                             @Part("email") RequestBody email,
                                             @Part("password") RequestBody password,
                                             @Part("password_confirmation") RequestBody password_confirmation,
                                             @Part("phone") RequestBody phone,
                                             @Part("whatsapp") RequestBody whatsapp,
                                             @Part("region_id") RequestBody region_id,
                                             @Part("delivery_cost") RequestBody delivery_cost,
                                             @Part("minimum_charger") RequestBody minimum_charger,
                                             @Part MultipartBody.Part photo,
                                             @Part("delivery_time") RequestBody delivery_time);

    @POST("restaurant/change-state")
    @FormUrlEncoded
    Call<GeneralResponse> changeState(@Field("state") String state,
                                      @Field("api_token") String api_token);

    //    //TODO
    @POST("client/profile")
    @Multipart
    Call<GeneralResponse> editUserProfile(@Part("api_token") RequestBody api_token,
                                          @Part("name") RequestBody name,
                                          @Part("phone") RequestBody phone,
                                          @Part("email") RequestBody email,
                                          @Part("password") RequestBody password,
                                          @Part("password_confirmation") RequestBody password_confirmation,
                                          @Part("region_id") RequestBody region_id,
                                          @Part MultipartBody.Part profile_image);

    @POST("restaurant/new-item")
    @Multipart
    Call<GeneralResponse> addItem(@Part("description") RequestBody description,
                                  @Part("price") RequestBody price,
                                  @Part("name") RequestBody name,
                                  @Part MultipartBody.Part photo,
                                  @Part("api_token") RequestBody api_token,
                                  @Part("offer_price") RequestBody offer_price,
                                  @Part("category_id") RequestBody category_id);

    @POST("restaurant/delete-item")
    @FormUrlEncoded
    Call<GeneralResponse> deleteItem(@Field("item_id") int item_id,
                                     @Field("api_token") String api_token);

    @POST("restaurant/update-item")
    @Multipart
    Call<GeneralResponse> updateItem(@Part("description") RequestBody description,
                                     @Part("price") RequestBody price,
                                     @Part("category_id") RequestBody category_id,
                                     @Part("name") RequestBody name,
                                     @Part MultipartBody.Part photo,
                                     @Part("item_id") RequestBody item_id,
                                     @Part("api_token") RequestBody api_token,
                                     @Part("offer_price") RequestBody offer_price
    );

    @GET("restaurants")
    Call<Restaurants> filterRestaurants(@Query("keyword") String keyword,
                                        @Query("region_id") int region_id);

    @GET("restaurant")
    Call<RestaurantDetails> getDetails(@Query("restaurant_id") int restaurant_id);

    @POST("client/decline-order")
    @FormUrlEncoded
    Call<GeneralResponse> declineClientOrder(@Field("api_token") String api_token,
                                             @Field("order_id") int order_id
    );
    //TODO
    @GET("restaurant/commissions")
    Call<RestaurantDetails> getCommission(@Query("api_token") String api_token);
    @GET("offers")
    Call<Offers> getNewOffers(@Query("restaurant_id") int restaurant_id);
    @POST("client/confirm-order")
    @FormUrlEncoded
    Call<GeneralResponse> confimUserOrder(@Field("api_token") String api_token,
                                       @Field("order_id") int order_id);

}
