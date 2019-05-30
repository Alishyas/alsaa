package com.e.onlineclothshopping;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("users/signup")
    Call<ResponseBody> createUser(@Body User user);

    @POST("users/login")
    Call<ResponseBody>validateUser(@Body User user);

    @GET("users/logout")
    Call<ResponseBody> logout(@Header("Cookie") String Cookie);
}
