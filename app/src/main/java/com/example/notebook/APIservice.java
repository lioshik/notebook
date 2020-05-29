package com.example.notebook;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIservice {
    @GET("getdata/{key}")
    Call<Homework> loadHomework(@Path("key") String key);

    @Multipart
    @POST("postdata/{json}")
    Call<UploadResponse> uploadHomework(@Part MultipartBody.Part[] images,
                                        @Path("json") String jsonBody);
}
