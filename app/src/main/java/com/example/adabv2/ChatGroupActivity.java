package com.example.adabv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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

public class ChatGroupActivity extends AppCompatActivity {

    private TextView nameGroupChat;
//    private RecyclerView recyclerViewChat;
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
        Log.wtf("berhasil get username", username);
        roomId = "CL" + classId + classType;

        Log.wtf("berhasil roomId", roomId);

        if (savedInstanceState != null){
            hasConnection = savedInstanceState.getBoolean("hasConnection");
        }

        if (!hasConnection){
            socket.connect();
            socket.on("join_room", onJoinUser);
//            socket.on("chatroom_message", chatRoomMessage);

            JSONObject userId = new JSONObject();

            try {
                userId.put("roomId", roomId + " Connected");
                socket.emit("join_room", userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void init(){
        nameGroupChat = binding.nameGroup;
//        recyclerViewChat = binding.recyclerViewChatGroup;
        listViewChat = binding.recyclerViewChatGroup;
        recordBtn = binding.recordButton;
        sendBtn = binding.sendBtn;
        backBtn = binding.buttonBackChat;
        messageEditText = binding.messageChatEditText;
        List<Chat> chatList = new ArrayList<>();
        chatRoomAdapter = new ChatRoomAdapter(this, R.layout.item_message,chatList);
        listViewChat.setAdapter(chatRoomAdapter);

//        recyclerViewChat.hasFixedSize();
//        recyclerViewChat.setItemViewCacheSize(20);
//        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewChat.setAdapter(chatRoomAdapter);
//        sendBtn.setEnabled(true);


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
//                sendChat();
                String message = messageEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    dateChat = DateFormatter.DateToStringChat(new Date());
                    sendMessageToServer(message);
                    messageEditText.setText(""); // Menghapus teks setelah mengirim pesan
                }
            }
        });
//        sendBtn.setOnClickListener(v->{
//            sendChat();
//        });

        recordBtn.setOnClickListener(v -> {
            // record message

        });


    }




//    private void connectSocket() {
//        try {
//            socket = IO.socket("https://adab.arutala.dev/");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        socket.connect();
//
//        while (!socket.connected()) {
//            Log.d("Socket.io", "connecting...");
//        }
//
//        socket.emit("join_room", roomId);
//        if (socket.connected()){
//            socket.on("chatroom_message", args -> {
//                JSONObject messageData = (JSONObject) args[0];
//                runOnUiThread (() -> {
//                    try {
//                        messageData.put("username", username);
//                        messageData.put("message", message);
//                        messageData.put("roomId", roomId);
//
//                        socket.emit("chatroom_message", messageData);
//
//                        // Tambahkan pesan ke adapter agar ditampilkan di chat
//                        Chat chat = new Chat(username, message, roomId);
//                        chatRoomAdapter.add(chat);
//                        listViewChat.smoothScrollToPosition(0);
//                        listViewChat.scrollTo(0, chatRoomAdapter.getCount()-1);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                });
//
//            });
//        }
//
//    }



    private void sendMessageToServer(String message) {
        JSONObject data = new JSONObject();
        try {
            data.put("username", username);
            data.put("message", message);
            data.put("roomId", roomId);
            data.put("timestamp", dateChat);

            // Kirim data ke server menggunakan socket
            Log.wtf("berhasil", String.valueOf(data));
            socket.emit("chatroom_message", data);

            // Tambahkan pesan ke adapter agar ditampilkan di chat
            Chat chat = new Chat(username, message, dateChat ,roomId);
            chatRoomAdapter.add(chat);
            listViewChat.smoothScrollToPosition(0);
            listViewChat.scrollTo(0, chatRoomAdapter.getCount()-1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("hasConnection", hasConnection);
    }

    Emitter.Listener onJoinUser = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    int length = args.length;
//
//                    if (length == 0){
//                        return;
//                    }
                    String roomId = args[0].toString();
                    try {
                        JSONObject object = new JSONObject(roomId);
                        roomId = object.getString("roomId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Chat chat = new Chat(null, null,null, roomId);
                    chatRoomAdapter.add(chat);
                    listViewChat.smoothScrollToPosition(0);
                    listViewChat.scrollTo(0, chatRoomAdapter.getCount()-1);
                }
            });
        }
    };


    Emitter.Listener chatRoomMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
           runOnUiThread(new Runnable() {
               @Override
               public void run() {
                   JSONObject data = (JSONObject) args[0];
                   String username;
                   String message;
                   String id;

                   try {
                       username = data.getString("username");
                       message = data.getString("message");
                       id = data.getString("roomId");

                       Chat chat = new Chat(username,message,dateChat, id);
                       chatRoomAdapter.add(chat);

                       listViewChat.smoothScrollToPosition(0);
                       listViewChat.scrollTo(0, chatRoomAdapter.getCount() - 1);

                   } catch (Exception e) {
                       return;
                   }
               }
           });
        }
    };

    Emitter.Listener disconnectChatRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject userId = new JSONObject();
            try {
                userId.put("username", username + " DisConnected");
                socket.emit("join_chatroom", roomId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            socket.disconnect();
            socket.off("chat message", chatRoomMessage);
            socket.off("join_chatroom", onJoinUser);
            username = "";
            chatRoomAdapter.clear();
        }
    };


    private void addMesssage(String username, String message) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.on("disconnect_chatroom", disconnectChatRoom);
    }
}