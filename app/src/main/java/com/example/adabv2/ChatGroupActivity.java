package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Chat;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.TranscriptMessageHistory;
import com.example.adabv2.Model.TranscriptMessageHistoryRequest;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.ActivityChatGroupBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatGroupActivity extends AppCompatActivity {
    private ListView listViewChat;
    private ImageView recordBtn, sendBtn, backBtn;
    private EditText messageEditText;
    private ActivityChatGroupBinding binding;
    private ChatRoomAdapter chatRoomAdapter;
    public static String roomId;
    private String username;
    public static final Integer PERMISSION_RECORD_AUDIO_REQUEST = 1;
    final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    private Boolean isStop = true;
    private SpeechRecognizer speechRecognizer;
    List<Chat> chatList = new ArrayList<>();

    private Socket socket;
    {
        try {
            socket = IO.socket("https://adab.arutala.dev/");
            Log.wtf("masuk", "link socket");

        } catch (URISyntaxException e) {
            Log.wtf("error socket", e.getMessage());

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();


        chatRoomAdapter = new ChatRoomAdapter(this, R.layout.item_message,chatList);
        listViewChat.setAdapter(chatRoomAdapter);


        getTranscriptMessageHistory();
        socket.emit("join_chatroom", roomId);
        receiveMessageFromServer();
        socket.connect();
        getTranscriptMessageHistory();

        backBtn.setOnClickListener(v -> {
//            getTranscriptMessageHistory();
            onBackPressed();
        });

        sendBtn.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(message)) {
                sendMessageToServer(message);
                messageEditText.setText("");
                Log.wtf("masuk", "button send");
            }
        });

        recordBtn.setOnClickListener(v -> {
            if (socket.connected()) {
                final boolean isPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
                if (isPermissionGranted) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO_REQUEST);
                }
                else {
                    if (isStop) {
                        recordBtn.setColorFilter(ContextCompat.getColor(this, R.color.gray), android.graphics.PorterDuff.Mode.SRC_IN);
                        speechToText();
                    }
                    else {
                        recordBtn.setColorFilter(ContextCompat.getColor(this, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                        speechRecognizer.stopListening();
                    }
                    isStop = !isStop;
                }
            }
        });
    }

    private void init() {
        TextView nameGroupChat = binding.nameGroup;
        listViewChat = binding.recyclerViewChatGroup;
        recordBtn = binding.recordButton;
        sendBtn = binding.sendBtn;
        backBtn = binding.buttonBackChat;
        messageEditText = binding.messageChatEditText;

//        List<Chat> chatList = new ArrayList<>();
//        chatRoomAdapter = new ChatRoomAdapter(this, R.layout.item_message,chatList);
//        listViewChat.setAdapter(chatRoomAdapter);

        int classId = getIntent().getIntExtra("classId", 0);
        String className = getIntent().getStringExtra("className");
        String classType = getIntent().getStringExtra("classType");

        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        username = userPreferences.getUserName();
        roomId = "CL" + classId + classType;
        nameGroupChat.setText(className);
    }

    private void receiveMessageFromServer() {
        socket.on("chatroom_message", args ->
                runOnUiThread(() -> {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String messageText = data.getString("msg");
                        String username = data.getString("user_id");
                        Date date = DateFormatter.stringToDateMillisecond(data.getString("timestamp"));
                        String timestamp = DateFormatter.dateToTime(date);

                        Chat chat = new Chat(username, messageText, timestamp, roomId + "s");
                        chatRoomAdapter.add(chat);
                        chatRoomAdapter.notifyDataSetChanged();
//                        listViewChat.smoothScrollToPosition(0);
//                        listViewChat.scrollTo(0, chatRoomAdapter.getCount() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                })
        );
    }

    private void sendMessageToServer(String message) {
        JSONObject data = new JSONObject();
        try {
            Date date = new Date();
            data.put("connectedRoomId", roomId);
            data.put("msg", message);
            data.put("user_id", username);
            data.put("timestamp", DateFormatter.dateToStringChat(date));

            socket.connect();
            socket.emit("chatroom_message", data);

            String timestamp = DateFormatter.dateToTime(date);
            Chat chat = new Chat(username, message, timestamp, roomId);
            chatRoomAdapter.add(chat);
            chatRoomAdapter.notifyDataSetChanged();
//            listViewChat.smoothScrollToPosition(0);
//            listViewChat.scrollTo(0, chatRoomAdapter.getCount()-1);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.wtf("failed send data", String.valueOf(data));
        }
    }

    private void speechToText() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.startListening(speechRecognizerIntent);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int i) {
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null) {
                    recordBtn.setColorFilter(ContextCompat.getColor(ChatGroupActivity.this, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                    isStop = true;
                    sendMessageToServer(data.get(0));
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {
            }

            @Override
            public void onEvent(int i, Bundle bundle) {
            }
        });
    }

    private void getTranscriptMessageHistory(){
        TranscriptMessageHistoryRequest transcriptMessageHistoryRequest = new TranscriptMessageHistoryRequest();
        Log.wtf("masuk1", "init request");

        transcriptMessageHistoryRequest.setSession_id(roomId);
        Log.wtf("masuk2", "roomId request" +  roomId);

        Call<Response<TranscriptMessageHistory>> transcriptMessageResponseCall = ApiClient.request().saveTranscriptMessageHistory(transcriptMessageHistoryRequest);
        Log.wtf("masuk3", "call response");
        transcriptMessageResponseCall.enqueue(new Callback<Response<TranscriptMessageHistory>>() {
            @Override
            public void onResponse(Call<Response<TranscriptMessageHistory>> call, retrofit2.Response<Response<TranscriptMessageHistory>> response) {
                Log.wtf("masuk4", "on response");

                if(response.isSuccessful()){
                    chatList.clear();
                    Log.wtf("masuk5", "response success");
                    Response<TranscriptMessageHistory> transcriptHistoryMessageResponse = response.body();
                    Log.wtf("masuk6", "response history");

                    List<TranscriptMessageHistory>  messageHistoryList = transcriptHistoryMessageResponse.getValues();
                    for (TranscriptMessageHistory history : messageHistoryList) {
                        String messageUserId = history.getUser_id();
                        String messageText = history.getMessage_tr();
                        String timestamp = history.getTimestamp();


                        if (messageUserId != null && messageUserId.equals(username)) {
                            Chat chatSent = new Chat(username, messageText, timestamp, roomId);
                            chatList.add(chatSent);
                        } else {
                            Chat chatReceived = new Chat(messageUserId, messageText, timestamp, roomId + "s");
                            chatList.add(chatReceived);
                        }
                    }

                    //listViewChat.scrollTo(0, chatRoomAdapter.getCount()-1);
                    chatRoomAdapter.notifyDataSetChanged();
                    listViewChat.setSelection(chatRoomAdapter.getCount() - 1);
                    Log.wtf("masuk13", "response get Message");

                }
                else {
                    Log.wtf("masuk gagal", "response history list");
                    Toast.makeText(ChatGroupActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response<TranscriptMessageHistory>> call, Throwable t) {
                Toast.makeText(ChatGroupActivity.this, "Failed" + t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }


}