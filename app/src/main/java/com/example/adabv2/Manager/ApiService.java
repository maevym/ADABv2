package com.example.adabv2.Manager;

import com.example.adabv2.Model.ClassSession;
import com.example.adabv2.Model.ClassSessionRequest;
import com.example.adabv2.Model.DataUser;
import com.example.adabv2.Model.Discuss;
import com.example.adabv2.Model.DiscussRequest;
import com.example.adabv2.Model.LoginRequest;
import com.example.adabv2.Model.LoginResponse;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Search;
import com.example.adabv2.Model.SearchRequest;
import com.example.adabv2.Model.Session;
import com.example.adabv2.Model.SessionRequest;
import com.example.adabv2.Model.TranscriptHistory;
import com.example.adabv2.Model.TranscriptMessageHistory;
import com.example.adabv2.Model.TranscriptMessageHistoryRequest;
import com.example.adabv2.Model.TranscriptRequest;
import com.example.adabv2.Model.UpdatePasswordRequest;

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

    @POST("user/getAllClass")
    Call<Response<Search>> searchClass(@Body SearchRequest searchRequest);

    @POST("user/getAllSessions")
    Call<Response<ClassSession>> classSessionAPI(@Body ClassSessionRequest classSessionRequest);

    @POST("user/getAllClass")
    Call<Response<Discuss>> discussSearch(@Body DiscussRequest discussRequest);

    @POST("user/getTranscriptHistory")
    Call<Response<TranscriptHistory>> saveTranscriptHistory(@Body TranscriptRequest transcriptRequest);

    @POST("updatePassword")
    Call<ResponseBody> updatePassword(@Body UpdatePasswordRequest updatePasswordRequest);

    @POST("user/getMessageHistory")
    Call<Response<TranscriptMessageHistory>> saveTranscriptMessageHistory(@Body TranscriptMessageHistoryRequest transcriptMessageHistoryRequest);


}

