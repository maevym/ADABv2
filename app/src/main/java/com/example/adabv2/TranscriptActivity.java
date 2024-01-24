package com.example.adabv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.TranscriptHistory;
import com.example.adabv2.Model.TranscriptRequest;
import com.example.adabv2.databinding.ActivityTranscriptBinding;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;

public class TranscriptActivity extends AppCompatActivity {
    private ActivityTranscriptBinding binding;
    private ImageView buttonBack;
    private TextView textRealTimeTV;
    private LinearLayout noTranscript;
    private ScrollView scrollView;
    private Socket socket;
    private Integer sessionId;
    private Boolean sessionHasPassed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranscriptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getTranscriptHistory();
        buttonBack.setOnClickListener(v -> onBackPressed());
        if(!sessionHasPassed) {
            connectSocket();
        }
    }

    private void init() {
        buttonBack = binding.buttonBack;
        TextView sessionNameTV = binding.sessionName;
        textRealTimeTV = binding.textRealTime;
        scrollView = binding.scrollView;
        noTranscript = binding.noTranscriptView;

        // get data from intent
        sessionId = getIntent().getIntExtra("sessionID", 0);
        String sessionName = getIntent().getStringExtra("sessionName");
        sessionHasPassed = getIntent().getBooleanExtra("sessionHasPassed", true);
        sessionNameTV.setText(sessionName);
    }

    private void getTranscriptHistory() {
        TranscriptRequest transcriptRequest = new TranscriptRequest();
        transcriptRequest.setSession_id(sessionId.toString());

        Call<Response<TranscriptHistory>> transcriptResponseCall = ApiClient.request().saveTranscriptHistory(transcriptRequest);

        transcriptResponseCall.enqueue(new Callback<Response<TranscriptHistory>>() {
            @Override
            public void onResponse(@NonNull Call<Response<TranscriptHistory>> call, @NonNull retrofit2.Response<Response<TranscriptHistory>> response) {
                if (response.isSuccessful()) {
                    Response<TranscriptHistory> transcriptHistoryResponse = response.body();
                    assert transcriptHistoryResponse != null;
                    List<TranscriptHistory> transcriptHistories = transcriptHistoryResponse.getValues();
                    TranscriptHistory transcriptHistory = transcriptHistories.get(0);
                    if (transcriptHistory.getMessage().isEmpty()) {
                        noTranscript.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.INVISIBLE);
                    } else {
                        noTranscript.setVisibility(View.INVISIBLE);
                        scrollView.setVisibility(View.VISIBLE);
                        textRealTimeTV.setText(transcriptHistory.getMessage());
                    }
                }
                else {
                    Log.e("Api Error", "Failed to Fetch Data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Response<TranscriptHistory>> call, @NonNull Throwable t) {
                Log.wtf("responses", "Failed " + t.getLocalizedMessage());
            }
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

        socket.on("message", args ->
            runOnUiThread(() -> {
                String prevText = String.valueOf(textRealTimeTV.getText());
                noTranscript.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                textRealTimeTV.setText(prevText + "\n" + args[0].toString());
                scrollView.fullScroll(View.FOCUS_DOWN);
            })
        );
    }
}