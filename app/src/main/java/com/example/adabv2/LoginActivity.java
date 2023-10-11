package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.LoginRequest;
import com.example.adabv2.Model.LoginResponse;
import com.example.adabv2.Model.ValueLoginResponse;
import com.example.adabv2.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        userPreferences = new UserPreferences(getApplicationContext());

        binding.buttonRegisterInLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        binding.buttonLogin.setOnClickListener(v -> {
            validasi();
        });


    }

    private void validasi(){
        String email = binding.emailLogin.getText().toString();
        String password = binding.passwordLogin.getText().toString();

        if(email.isEmpty()){
            binding.emailLogin.setError("username must field");
            binding.emailLogin.requestFocus();
            return;
        }
        else if(password.isEmpty()){
            binding.passwordLogin.setError("Password must field");
            binding.passwordLogin.requestFocus();
            return;
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUser_email(email);
        loginRequest.setUser_password(password);
        Call<LoginResponse> callLogin = ApiClient.request().loginUser(loginRequest);
        callLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse != null && loginResponse.getStatus() == 200) {
                    String message = loginResponse.getMessage();
                    ValueLoginResponse valueLoginResponse = loginResponse.getValues();
                    if (valueLoginResponse != null) {
                        String userSecret = valueLoginResponse.getUser_secret();
                        String name = valueLoginResponse.getName();
                        String userType = valueLoginResponse.getUser_type();
                        userPreferences.setUserSecret(userSecret);
                        userPreferences.setUserName(name);
                        userPreferences.setUserType(userType);
                        Toast.makeText(LoginActivity.this, "success login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    if(response.code() == 401){
                        Toast.makeText(LoginActivity.this, "Email or password invalid", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
//        Call<ResponseBody> callLogin = ApiClient.request().loginUser(loginRequest);
//        callLogin.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()){
//                    ResponseBody responseBody = response.body();
//                    Toast.makeText(LoginActivity.this,"Login Successful", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                } else {
//                    if(response.code() == 401){
//                        Toast.makeText(LoginActivity.this, "Email or password invalid", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }

//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(LoginActivity.this,"Login Failed", Toast.LENGTH_LONG).show();
//            }
//        });


    }
}