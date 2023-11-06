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
import com.example.adabv2.databinding.ActivityTranscriptHistoryBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TranscriptHistoryActivity extends AppCompatActivity {

    private ActivityTranscriptHistoryBinding binding;
    private ImageView buttonBack;
    private TextView textTranscript;
    private LinearLayout noTranscript;
    private ScrollView scrollView;
    private Integer sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTranscriptHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        getTranscriptHistory();
        buttonOnClick();
    }

    private void init() {
        buttonBack = binding.buttonBack;
        TextView sessionNameTV = binding.sessionName;
        textTranscript = binding.transcriptText;
        scrollView = binding.scrollView;
        noTranscript = binding.noTranscriptView;

        sessionId = getIntent().getIntExtra("sessionID", 0);
        String sessionName = getIntent().getStringExtra("sessionName");
        sessionNameTV.setText(sessionName);
    }

    private void buttonOnClick() {
        buttonBack.setOnClickListener(v -> finish());
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
                        textTranscript.setText(transcriptHistory.getMessage());
                    }
                }
                else {
                    noTranscript.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.INVISIBLE);
                    Log.e("Api Error", "Failed to Fetch Data");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Response<TranscriptHistory>> call, @NonNull Throwable t) {
                Log.wtf("responses", "Failed " + t.getLocalizedMessage());
            }
        });
    }
}