package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adabv2.Room.DiscussDatabase;
import com.example.adabv2.Room.SearchDatabase;
import com.example.adabv2.Room.SessionDatabase;
import com.example.adabv2.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {
    private TextView name ;
    private ImageView buttonBack;
    private Button changePasswordBtn;
    private Button logOutBtn;
    private SessionDatabase dbSession;
    private SearchDatabase dbClassSearch;
    private DiscussDatabase dbDiscuss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingBinding binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = binding.userName;
        buttonBack = binding.buttonBack;
        changePasswordBtn = binding.buttonChangePassword;
        logOutBtn = binding.buttonLogout;

        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        name.setText(userPreferences.getUserName());

        buttonBack.setOnClickListener(v -> finish());

        logOutBtn.setOnClickListener(v -> {
            dbSession = Room.databaseBuilder(getApplicationContext(), SessionDatabase.class,"session-database").allowMainThreadQueries().build();
            dbClassSearch = Room.databaseBuilder(getApplicationContext(), SearchDatabase.class, "search-database").allowMainThreadQueries().build();
            dbDiscuss = Room.databaseBuilder(getApplicationContext(), DiscussDatabase.class, "searchdiscuss-database").allowMainThreadQueries().build();
            dbSession.sessionDAO().deleteAll();
            dbClassSearch.searchDAO().deleteAllSearch();
            dbDiscuss.discussWithMember().deleteAllSMember();
            startActivity(new Intent(this, LoginActivity.class));
        });

        changePasswordBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        });
    }
}