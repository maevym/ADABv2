package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.adabv2.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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

        // cari data di database apakah ada atau tidak.
        // Kalo tidak maka muncul error, jika ada lanjut ke home

    }
}