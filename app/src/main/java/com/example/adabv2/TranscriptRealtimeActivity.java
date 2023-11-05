package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ObbInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.TranscriptHistory;
import com.example.adabv2.Model.TranscriptRequest;
import com.example.adabv2.databinding.ActivityHomeBinding;
import com.example.adabv2.databinding.ActivityTranscriptRealtimeBinding;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;

public class TranscriptRealtimeActivity extends AppCompatActivity {

    private ActivityTranscriptRealtimeBinding binding;
    private ImageView buttonBack;
    private TextView textRealTimeTV;
    private ScrollView scrollView;
    private Socket socket;
    private Integer sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranscriptRealtimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getTranscriptHistory();
        buttonOnClick();
        connectSocket();
    }

    private void init() {
        buttonBack = binding.buttonBack;
        TextView sessionNameTV = binding.sessionName;
        textRealTimeTV = binding.textRealTime;
        scrollView = binding.scrollView;

        // get data from intent
        sessionId = getIntent().getIntExtra("sessionID", 0);
        String sessionName = getIntent().getStringExtra("sessionName");

        sessionNameTV.setText(sessionName);
    }

    private void getTranscriptHistory() {
        TranscriptRequest transcriptRequest = new TranscriptRequest();
        transcriptRequest.setSession_id(sessionId.toString());

        Call<Response<TranscriptHistory>> transcriptResponseCall = ApiClient.request().saveTranscriptHistory(transcriptRequest);

        transcriptResponseCall.enqueue(new Callback<Response<TranscriptHistory>>() {
            @Override
            public void onResponse(Call<Response<TranscriptHistory>> call, retrofit2.Response<Response<TranscriptHistory>> response) {
                if (response.isSuccessful()) {
                    Response<TranscriptHistory> transcriptHistoryResponse = response.body();
                    assert transcriptHistoryResponse != null;
                    List<TranscriptHistory> transcriptHistories = transcriptHistoryResponse.getValues();
                    TranscriptHistory transcriptHistory = transcriptHistories.get(0);
                    textRealTimeTV.setText(transcriptHistory.getMessage());
                }
                else {
                    Log.e("Api Error", "Failed to Fetch Data");
                }
            }

            @Override
            public void onFailure(Call<Response<TranscriptHistory>> call, Throwable t) {
                Log.wtf("responses", "Failed " + t.getLocalizedMessage());
            }
        });
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