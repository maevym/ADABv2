package com.example.adabv2;

import android.graphics.Bitmap;
import android.telephony.TelephonyCallback;
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

import java.util.ArrayList;
import java.util.List;


public class ChatGroupAdapter extends RecyclerView.Adapter {

    private List<Chat> chatList = new ArrayList<>();
    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;

    public ChatGroupAdapter(List<Chat> chatList){
        this.chatList = chatList;
    }

    private class SendChatHolder extends RecyclerView.ViewHolder {
        private TextView textSendMessage;
        private TextView timeSend;

        public SendChatHolder(ItemSendMessageBinding itemSendMessageBinding) {
            super(itemSendMessageBinding.getRoot());
            textSendMessage = itemSendMessageBinding.fieldSendTextMessage;
            timeSend = itemSendMessageBinding.timeSendView;

        }
    }


    private class ReceivedChatHolder extends RecyclerView.ViewHolder {
        private TextView nameReceived;
        private TextView textReceivedMessage;
        private TextView timeReceived;


        public ReceivedChatHolder(ItemReceivedMessageBinding itemReceivedMessageBinding) {
            super(itemReceivedMessageBinding.getRoot());
            nameReceived = itemReceivedMessageBinding.nameReceivedChat;
            textReceivedMessage = itemReceivedMessageBinding.fieldReceivedTextMessage;
            timeReceived = itemReceivedMessageBinding.timeReceived;

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSendMessageBinding sendMessageBinding;
        ItemReceivedMessageBinding receivedMessageBinding;

        switch (viewType) {
            case TYPE_MESSAGE_SENT:
                sendMessageBinding = ItemSendMessageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new SendChatHolder(sendMessageBinding);

            case TYPE_MESSAGE_RECEIVED:
                receivedMessageBinding = ItemReceivedMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new ReceivedChatHolder(receivedMessageBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        Chat chat = chatList.get(position);
//        SendChatHolder sendChatHolder = (SendChatHolder) holder;
//        sendChatHolder.textSendMessage.setText(chat.getMessage());
//        sendChatHolder.timeSend.setText(chat.getSendTime());
//
//        ReceivedChatHolder receivedChatHolder = (ReceivedChatHolder) holder;
//        receivedChatHolder.nameReceived.setText(chat.getNameMember());
//        receivedChatHolder.textReceivedMessage.setText(chat.getMessage());
//        receivedChatHolder.timeReceived.setText(chat.getReceivedTime());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


}
