package com.example.adabv2;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adabv2.Model.Chat;

import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends ArrayAdapter<Chat> {


    public ChatRoomAdapter(@NonNull Context context, int resource, @NonNull List<Chat> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Chat chatText = getItem(position);
        if (TextUtils.isEmpty(chatText.getMessage())){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_connected,parent,false);
            TextView messageText = convertView.findViewById(R.id.message_body);
            String userConnected = chatText.getUsername();
            messageText.setText(userConnected);
        }
        else if(chatText.getRoomId().equals(ChatGroupActivity.roomId)){

            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_send_message,parent,false);
            TextView messageText = convertView.findViewById(R.id.fieldSendTextMessage);
            messageText.setText(chatText.getMessage());

        }
        else {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_received_message,parent,false);
            TextView messageText = convertView.findViewById(R.id.fieldReceivedTextMessage);
            TextView usernameText = (TextView) convertView.findViewById(R.id.nameReceivedChat);

            messageText.setVisibility(View.VISIBLE);
            usernameText.setVisibility(View.VISIBLE);

            messageText.setText(chatText.getMessage());
            usernameText.setText(chatText.getUsername());

        }

        return convertView;
      }

}
