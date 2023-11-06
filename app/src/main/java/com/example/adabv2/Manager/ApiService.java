package com.example.adabv2.Manager;

import com.example.adabv2.Model.DataUser;
import com.example.adabv2.Model.Discuss;
import com.example.adabv2.Model.DiscussRequest;
import com.example.adabv2.Model.LoginRequest;
import com.example.adabv2.Model.LoginResponse;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Session;
import com.example.adabv2.Model.SessionRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("user/sessions")
    Call<Response<Session>> saveSession(@Body SessionRequest sessionRequest);

    @POST("register")
    Call<ResponseBody> saveUser(@Body DataUser dataUser);

    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

//    @POST("login")
//    Call<ResponseBody> loginUser(@Body LoginRequest loginRequest);

    @POST("user/getAllMember")
    Call<Response<Discuss>> discussSearch(@Body DiscussRequest discussRequest);
}

