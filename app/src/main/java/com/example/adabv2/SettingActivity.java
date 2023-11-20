package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adabv2.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private ImageView buttonBack;
    private Button changeProfileBtn, logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        buttonOnClick();
    }

    private void init() {
        TextView name = binding.userName;
        buttonBack = binding.buttonBack;
        changeProfileBtn = binding.buttonChangeProfile;
        logOutBtn = binding.buttonLogout;

        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        name.setText(userPreferences.getUserName());
    }

    private void buttonOnClick() {
        buttonBack.setOnClickListener(v -> finish());

        logOutBtn.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        changeProfileBtn.setOnClickListener(v -> {
            // pindah ke change profile activity
        });
    }
}