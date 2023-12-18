package com.example.adabv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adabv2.Model.Chat;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.ActivityChatGroupBinding;
import com.example.adabv2.databinding.ActivityViewClassBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatGroupActivity extends AppCompatActivity {
    private TextView nameGroupChat;
    private ListView listViewChat;
    private ImageView recordBtn, sendBtn, backBtn;
    private EditText messageEditText;
    private ActivityChatGroupBinding binding;
    private ChatRoomAdapter chatRoomAdapter;
    public static String roomId;
    private int classId;
    private String classType;
    private String username;
    private Boolean hasConnection = false;
    private Thread tread2;
    public static final String TAG  = "ChatGroupActivity";
    private String dateChat;


    public static final Integer PERMISSION_RECORD_AUDIO_REQUEST = 1;
    private SpeechRecognizer speechRecognizer;
    private Boolean isStop = false;
    final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    private String chosenLanguage;

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
        Log.wtf("masuk", "init");
        classId = getIntent().getIntExtra("classId", 0);
        classType = getIntent().getStringExtra("classType");
        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        username = userPreferences.getUserName();
        Log.wtf("success get username", username);
        roomId = "CL" + classId + classType;
        Log.wtf("masuk", "intent");

        Log.wtf("success roomId", roomId);
        nameGroupChat.setText(roomId);

        socket.on("numClients", args -> {
            runOnUiThread(() -> {
                int numClients = (int) args[0];
                Log.wtf("masuk", "num client socket");
            });
        });

        socket.emit("join_chatroom", roomId);
        Log.wtf("masuk", "socket emit join chatroom");

        receivedMessageFromServer();
        JSONObject userId = new JSONObject();
        try {
            userId.put("user_id", username);
            socket.emit("chatroom_message", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // ke save chatnya jadi history
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    dateChat = DateFormatter.DateToStringChat(new Date());
                    sendMessageToServer(message);
                    messageEditText.setText("");
                    Log.wtf("masuk", "button send");
                }
            }
        });


        recordBtn.setOnClickListener(v -> {
            // record message
            connectRecordSpeechToText();
        });


    }

    private void init(){
        nameGroupChat = binding.nameGroup;
        listViewChat = binding.recyclerViewChatGroup;
        recordBtn = binding.recordButton;
        sendBtn = binding.sendBtn;
        backBtn = binding.buttonBackChat;
        messageEditText = binding.messageChatEditText;
        List<Chat> chatList = new ArrayList<>();
        chatRoomAdapter = new ChatRoomAdapter(this, R.layout.item_message,chatList);
        listViewChat.setAdapter(chatRoomAdapter);

    }


    private void sendMessageToServer(String message) {
        JSONObject data = new JSONObject();
        try {
            data.put("connectedRoomId", roomId);
            data.put("msg", message);
            data.put("user_id", username);
            data.put("timestamp", dateChat);

            Log.wtf("masuk", "socket send message send to messsage");

            socket.connect();
            Log.wtf("masuk", "socket connect");
            socket.emit("chatroom_message", data);

            Log.wtf("masuk", "socket emit chatroom");

            Log.wtf("success", String.valueOf(data));
            Chat chat = new Chat(username, message, dateChat, roomId);
            chatRoomAdapter.add(chat);
//            listViewChat.smoothScrollToPosition(0);
//            listViewChat.scrollTo(0, chatRoomAdapter.getCount()-1);

            Log.wtf("masuk", "socket success sendMessageTo Server");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.wtf("failed send data", String.valueOf(data));
        }

    }

    private void receivedMessageFromServer(){
        try {
            JSONObject data = new JSONObject();
            String receivedRoomId = data.getString("connectedRoomId");
            String messageText = data.getString("msg");
            String username = data.getString("user_id");
            String timestamp = data.getString("timestamp");

            Log.wtf("masuk", "socket on message chat room message");
                Chat chat = new Chat(username, messageText, timestamp, roomId);
                chatRoomAdapter.add(chat);
//                listViewChat.smoothScrollToPosition(0);
//                listViewChat.scrollTo(0, chatRoomAdapter.getCount() - 1);
                Log.wtf("masuk", "socket on message chat id room message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void connectRecordSpeechToText(){
        socket.connected();
        if (socket.connected()) {
            if (isPermissionGranted()) {
                requestPermission();
            }
            speechToText();
        } else {
            Log.d("Socket.io", "error");
        }

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_RECORD_AUDIO_REQUEST);
    }

    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED;
    }


    private void speechToText() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.startListening(speechRecognizerIntent);

        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
//        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
            }

            @Override
            public void onBeginningOfSpeech() {
//                socket.emit("start_talking");
            }

            @Override
            public void onRmsChanged(float v) {
            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
//                socket.emit("stop_talking");
            }

            @Override
            public void onError(int i) {
                // starts listening again
//                if (i == 7 && !isStop) {
//                    speechRecognizer.startListening(speechRecognizerIntent);
//                }
            }

            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null) {
                    sendMessageToServer(data.get(0));
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


}