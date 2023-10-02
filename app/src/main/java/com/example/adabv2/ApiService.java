package com.example.adabv2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("user/sessions")
    Call<SessionResponse> saveSession(@Body SessionRequest sessionRequest);
}

