package com.example.adabv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Session;
import com.example.adabv2.Model.SessionRequest;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.ActivityScheduleBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ScheduleActivity extends AppCompatActivity {
    private ActivityScheduleBinding binding;
    private CalendarView calendarView;
    private LinearLayout noClassView;
    private RecyclerView rv;
    private ExtendedFloatingActionButton fab, fab1, fab2, fab3;
    private CardView fab1Text, fab2Text, fab3Text;
    private TextView todayDate;
    private ImageView buttonBack;

    private String chosenDate;
    private String userSecret;
    private String role;
    private Animation fabOpen, fabClose;
    private SessionAdapter sessionAdapter;
    private boolean isOpen = false;
    private final List<Session> sessions = new ArrayList<>();
    private final Date currentDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        pickDate();
        menuOnClickListener();
        buttonOnClick();
    }

    private void init() {
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        sessionAdapter = new SessionAdapter(sessions, this);
        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        role = userPreferences.getUserType();
        userSecret = userPreferences.getUserSecret();
        sessionAdapter.setUserType(role);

        fab = binding.fab;
        fab1 = binding.fab1;
        fab2 = binding.fab2;
        fab3 = binding.fab3;
        todayDate = binding.todayDate;
        fab1Text = binding.fab1Text;
        fab2Text = binding.fab2Text;
        fab3Text = binding.fab3Text;
        rv = binding.recyclerView;
        noClassView = binding.noClassView;
        calendarView = binding.calendarView;
        buttonBack = binding.buttonBack;

        rv.hasFixedSize();
        rv.setItemViewCacheSize(20);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(sessionAdapter);

        String date = DateFormatter.DateToStringDate(currentDate);
        todayDate.setText(date);
        chosenDate = DateFormatter.DateToString(currentDate);
        getSessions();
    }

    private void pickDate() {
        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            if (i2 < 10) {
                chosenDate = i + "-" + (i1+1) + "-0" + i2;
            } else {
                chosenDate = i + "-" + (i1+1) + "-" + i2;
            }
            Date date = DateFormatter.StringToDate(chosenDate);
            todayDate.setText(DateFormatter.DateToStringDate(date));
            getSessions();
        });
    }

    private void getSessions() {
        sessions.clear();
        SessionRequest sessionRequest = new SessionRequest();
        sessionRequest.setUser_secret(userSecret);
        sessionRequest.setDate(chosenDate);

        Call<Response<Session>> sessionResponseCall = ApiClient.request().saveSession(sessionRequest);
        sessionResponseCall.enqueue(new Callback<Response<Session>>() {
            @Override
            public void onResponse(@NonNull Call<Response<Session>> call, @NonNull retrofit2.Response<Response<Session>> response) {
                if (response.isSuccessful()) {
                    Response<Session> sessionResponse = response.body();
                    assert sessionResponse != null;
                    List<Session> sessionList = sessionResponse.getValues();
                    sessions.addAll(sessionList);
                    sortItems();
                } else {
                    if (response.code() == 404) {
                        rv.setVisibility(View.INVISIBLE);
                        noClassView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(ScheduleActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.e("Api Error", "Failed to Fetch Data");
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<Response<Session>> call, @NonNull Throwable t) {
                Log.wtf("responses", "Failed " + t.getLocalizedMessage());
            }
        });
    }

    private void sortItems () {
        if(!sessions.isEmpty()){
            rv.setVisibility(View.VISIBLE);
            noClassView.setVisibility(View.INVISIBLE);
        }

        Collections.sort(sessions, (session, t1) -> {
            try {
                return Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).parse(session.getSessionStart())).compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).parse(t1.getSessionStart()));
            } catch (ParseException e) {
                Log.wtf("error", e);
                return 0;
            }
        });

        sessionAdapter.notifyDataSetChanged();
    }

    private void buttonOnClick() {
        buttonBack.setOnClickListener(v -> finish());
    }

    private void menuOnClickListener () {
        fab.setOnClickListener(view -> animateFab());

        fab1.setOnClickListener(view -> {
            animateFab();
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        });

        fab2.setOnClickListener(view -> {
            animateFab();
            Intent intent = new Intent(this, ViewClassActivity.class);
            startActivity(intent);
        });

//        fab3.setOnClickListener(view -> {
//            animateFab();
//            Intent intent = new Intent(this, DiscussActivity.class);
//            startActivity(intent);
//        });
    }

    private void animateFab() {
        if (isOpen) {
            if (!role.equals("L")) {
                fab3.startAnimation(fabClose);
                fab3Text.startAnimation(fabClose);
                fab3.setClickable(false);
            }
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab1Text.startAnimation(fabClose);
            fab2Text.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isOpen = false;
        } else {
            if (!role.equals("L")) {
                fab3.startAnimation(fabOpen);
                fab3Text.startAnimation(fabOpen);
                fab3.setClickable(true);
            }
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab1Text.startAnimation(fabOpen);
            fab2Text.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isOpen = true;
        }
    }
}