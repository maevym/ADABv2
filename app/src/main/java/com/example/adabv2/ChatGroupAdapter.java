package com.example.adabv2;

import android.graphics.Bitmap;
import android.telephony.TelephonyCallback;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adabv2.Model.Chat;
import com.example.adabv2.Model.Search;
import com.example.adabv2.databinding.ActivityChatGroupBinding;
import com.example.adabv2.databinding.DiscussItemBinding;
import com.example.adabv2.databinding.ItemReceivedMessageBinding;
import com.example.adabv2.databinding.ItemSendMessageBinding;
import com.example.adabv2.databinding.SearchItemBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChatGroupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER_CONNECTED = 0;
    private static final int VIEW_TYPE_SEND_MESSAGE = 1;
    private static final int VIEW_TYPE_RECEIVED_MESSAGE = 2;

    private List<Chat> chatList;
    private String roomId;

    public ChatGroupAdapter(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_USER_CONNECTED:
                View userConnectedView = inflater.inflate(R.layout.user_connected, parent, false);
                return new UserConnectedViewHolder(userConnectedView);

            case VIEW_TYPE_SEND_MESSAGE:
                View sendMessageView = inflater.inflate(R.layout.item_send_message, parent, false);
                return new SendMessageViewHolder(sendMessageView);

            case VIEW_TYPE_RECEIVED_MESSAGE:
                View receivedMessageView = inflater.inflate(R.layout.item_received_message, parent, false);
                return new ReceivedMessageViewHolder(receivedMessageView);

            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chatText = chatList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER_CONNECTED:
                ((UserConnectedViewHolder) holder).bind(chatText);
                break;

            case VIEW_TYPE_SEND_MESSAGE:
                ((SendMessageViewHolder) holder).bind(chatText);
                break;

            case VIEW_TYPE_RECEIVED_MESSAGE:
                ((ReceivedMessageViewHolder) holder).bind(chatText);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Chat chatText = chatList.get(position);

        if (TextUtils.isEmpty(chatText.getMessage())) {
            return VIEW_TYPE_USER_CONNECTED;
        } else if (chatText.getRoomId().equals(ChatGroupActivity.roomId)) {
            return VIEW_TYPE_SEND_MESSAGE;
        } else {
            return VIEW_TYPE_RECEIVED_MESSAGE;
        }
    }

    // View holders

    private static class UserConnectedViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        UserConnectedViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_body);
        }

        void bind(Chat chatText) {
            String userConnected = chatText.getUsername();
            messageText.setText(userConnected);
        }
    }

    private static class SendMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        SendMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.fieldSendTextMessage);
          //  timeText = itemView.findViewById(R.id.timeSendView);
        }

        void bind(Chat chatText) {
            messageText.setText(chatText.getMessage());
            //timeText.setText(chatText.getTime());
        }
    }

    private static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView usernameText;
        TextView timeText;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.fieldReceivedTextMessage);
            usernameText = itemView.findViewById(R.id.nameReceivedChat);
            timeText = itemView.findViewById(R.id.timeReceived);
        }

        void bind(Chat chatText) {
            messageText.setText(chatText.getMessage());
            usernameText.setText(chatText.getUsername());
            timeText.setText(chatText.getTime());
        }
    }
}
