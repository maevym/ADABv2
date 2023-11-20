package com.example.adabv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Session;
import com.example.adabv2.Model.SessionRequest;
import com.example.adabv2.Model.TranscriptHistory;
import com.example.adabv2.Model.TranscriptRequest;
import com.example.adabv2.databinding.ActivityRecordRealtimeBinding;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;

public class RecordRealtimeActivity extends AppCompatActivity {

    private ActivityRecordRealtimeBinding binding;
    private ImageView backButton;
    private TextView realTimeTextTV;
    private Button stopButton;
    private ScrollView scrollView;
    private LinearLayout noTranscript;

    public static final Integer PERMISSION_RECORD_AUDIO_REQUEST = 1;
    private String chosenLanguage;
    private SpeechRecognizer speechRecognizer;
    private Socket socket;
    private Integer sessionId;
    private Boolean isStop = false;
    final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordRealtimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        connectSocket();
        buttonOnclick();
    }

    private void init() {
        backButton = binding.buttonBack;
        TextView sessionNameTV = binding.sessionName;
        realTimeTextTV = binding.textRealTime;
        stopButton = binding.buttonStop;
        scrollView = binding.scrollView;
        noTranscript = binding.noTranscriptView;

        // get data from intent
        sessionId = getIntent().getIntExtra("sessionID", 0);
        String sessionName = getIntent().getStringExtra("sessionName");
        chosenLanguage = getIntent().getStringExtra("chosenLanguage");

        sessionNameTV.setText(sessionName);
    }

    private void buttonOnclick() {
        // back button
        backButton.setOnClickListener(v -> {
            isStop = true;
            speechRecognizer.stopListening();
            stopConfirmation();
        });

        // stop record
        stopButton.setOnClickListener(v -> {
            isStop = true;
            speechRecognizer.stopListening();
            stopConfirmation();
        });
    }

    private void connectSocket() {
        getTranscriptHistory();
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

        if (socket.connected()) {
            if (isPermissionGranted()) {
                requestPermission();
            }
            if (realTimeTextTV.getText() == "") {
                socket.emit("edit","");
            }
            speechToText();
        } else {
            Log.d("Socket.io", "error");
        }
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
                    if (transcriptHistory.getMessage().isEmpty()) {
                        noTranscript.setVisibility(View.VISIBLE);
                    } else {
                        realTimeTextTV.setText(transcriptHistory.getMessage());
                    }
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

    private void speechToText() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.startListening(speechRecognizerIntent);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, chosenLanguage);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
                socket.emit("start_talking");
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                socket.emit("stop_talking");
            }

            @Override
            public void onError(int i) {
                // starts listening again
                if (i == 7 && !isStop) {
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String prevText = String.valueOf(realTimeTextTV.getText());
                if (data != null) {
                    if (!prevText.equals("")) {
                        socket.emit("history",data.get(0));
                        realTimeTextTV.setText(prevText + "\n" + data.get(0));
                        socket.emit("message", "\n" + data.get(0));
                    } else {
                        realTimeTextTV.setText(data.get(0));
                        socket.emit("message", data.get(0));
                    }
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
                speechRecognizer.startListening(speechRecognizerIntent);
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO_REQUEST);
    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
    }

    private void stopConfirmation() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RecordRealtimeActivity.this, R.style.CustomAlertDialog);
        alertBuilder.setTitle("Stop Recording?")
                .setMessage("Are you sure you want to stop recording?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        speechRecognizer.destroy();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        isStop = false;
                        speechRecognizer.startListening(speechRecognizerIntent);
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_RECORD_AUDIO_REQUEST && grantResults.length > 0) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }
}