package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ObbInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adabv2.databinding.ActivityHomeBinding;
import com.example.adabv2.databinding.ActivityTranscriptRealtimeBinding;

public class TranscriptRealtimeActivity extends AppCompatActivity {

    private ActivityTranscriptRealtimeBinding binding;
    private ImageView buttonBack;
    private TextView sessionName, textRealTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranscriptRealtimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        buttonOnClick();
    }

    private void init() {
        buttonBack = binding.buttonBack;
        sessionName = binding.sessionName;
        textRealTime = binding.textRealTime;
    }

    private void buttonOnClick() {
        buttonBack.setOnClickListener(v -> {
            finish();
        });
    }
}