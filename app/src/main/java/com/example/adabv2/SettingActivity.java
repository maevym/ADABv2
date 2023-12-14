package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adabv2.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingBinding binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView name = binding.userName;
        ImageView buttonBack = binding.buttonBack;
        Button changePasswordBtn = binding.buttonChangePassword;
        Button logOutBtn = binding.buttonLogout;

        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        name.setText(userPreferences.getUserName());

        buttonBack.setOnClickListener(v -> finish());

        logOutBtn.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        changePasswordBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        });
    }
}