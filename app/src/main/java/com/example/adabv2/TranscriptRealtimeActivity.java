package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ObbInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.adabv2.databinding.ActivityHomeBinding;
import com.example.adabv2.databinding.ActivityTranscriptRealtimeBinding;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class TranscriptRealtimeActivity extends AppCompatActivity {

    private ActivityTranscriptRealtimeBinding binding;
    private ImageView buttonBack;
    private TextView sessionNameTV, textRealTimeTV;
    private ScrollView scrollView;
    private Socket socket;
    private Integer sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranscriptRealtimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        buttonOnClick();
        connectSocket();
    }

    private void init() {
        buttonBack = binding.buttonBack;
        sessionNameTV = binding.sessionName;
        textRealTimeTV = binding.textRealTime;
        scrollView = binding.scrollView;

        // get data from intent
        sessionId = getIntent().getIntExtra("sessionID", 0);
        String sessionName = getIntent().getStringExtra("sessionName");

        sessionNameTV.setText(sessionName);
    }

    private void buttonOnClick() {
        buttonBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void connectSocket() {
        try {
            socket = IO.socket("https://adab.arutala.dev/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();

        while (!socket.connected()) {
            Log.d("Socket.io", "connecting...");
        }

        socket.emit("join_room", String.valueOf(sessionId));

        socket.on("message", args -> {
            runOnUiThread(() -> {
                textRealTimeTV.setText(args[0].toString());
                scrollView.fullScroll(View.FOCUS_DOWN);
            });
        });
    }
}