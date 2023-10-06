package com.example.adabv2;

import android.provider.ContactsContract;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register")
    Call<ResponseBody> saveUser(@Body DataUser dataUser);

    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

//    @POST("login")
//    Call<ResponseBody> loginUser(@Body LoginRequest loginRequest);

    @POST("user/sessions")
    Call<SessionResponse> saveSession(@Body SessionRequest sessionRequest);
}

