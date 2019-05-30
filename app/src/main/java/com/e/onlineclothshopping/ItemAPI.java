package com.e.onlineclothshopping;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ItemAPI {

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Header ("Cookie") String cookie, @Part MultipartBody.Part img);

    @POST("items")
    Call<ResponseBody> addItem(@Header ("Cookie") String cookie, @Body Item item);

    @GET("items")
    Call<List<Item>> getAllItems(@Header ("Cookie") String cookie);
}
