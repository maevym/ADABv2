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


    private Socket socket;
    {
        try {
            socket = IO.socket("https://adab.arutala.dev/");
        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        classId = getIntent().getIntExtra("classId", 0);
        classType = getIntent().getStringExtra("classType");
        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        username = userPreferences.getUserName();
        Log.wtf("success get username", username);
        roomId = "CL" + classId + classType;

        Log.wtf("success roomId", roomId);
        nameGroupChat.setText(roomId);

        if (!hasConnection){
            socket.on("join_room", args -> {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        String roomId = args[0].toString();
                        try {
                            JSONObject object = new JSONObject(roomId);
                            roomId = object.getString("roomId");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.wtf("gagal dalam join room", roomId);
                        }
                        Chat chat = new Chat(null, null,null, roomId);
                        chatRoomAdapter.add(chat);
                        listViewChat.smoothScrollToPosition(0);
                        listViewChat.scrollTo(0, chatRoomAdapter.getCount()-1);
                        socket.connect();
                        socket.emit("join_room", roomId);
                    }
                });
            });
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
                    messageEditText.setText(""); // Menghapus teks setelah mengirim pesan
                }
            }
        });


        recordBtn.setOnClickListener(v -> {
            // record message

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
            data.put("username", username);
            data.put("message", message);
            data.put("roomId", roomId);
            data.put("timestamp", dateChat);

            Log.wtf("success", String.valueOf(data));
            Chat chat = new Chat(username, message, dateChat, roomId);
            chatRoomAdapter.add(chat);
            listViewChat.smoothScrollToPosition(0);
            listViewChat.scrollTo(0, chatRoomAdapter.getCount()-1);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.wtf("failed send data", String.valueOf(data));
        }
        socket.connect();
        socket.emit("chatroom_message", (data));
    }



}