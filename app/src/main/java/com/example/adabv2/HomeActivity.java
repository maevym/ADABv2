package com.example.adabv2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Session;
import com.example.adabv2.Model.SessionRequest;
import com.example.adabv2.Room.SessionDatabase;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.ActivityHomeBinding;
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

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ExtendedFloatingActionButton fab, fab1, fab2, fab3;
    private CardView fab1Text, fab2Text, fab3Text;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noClassView;
    private ImageView logo;
    private RecyclerView rv;
    private SessionDatabase db;

    private String role;
    private String userSecret;
    private Animation fabOpen, fabClose;
    private ActivityHomeBinding binding;
    private SessionAdapter sessionAdapter;

    private boolean isOpen = false;
    private final List<Session> sessions = new ArrayList<>();
    private final Date currentDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        saveSession(createSessionRequest());
        menuOnClickListener();
    }

    private void init () {
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
        TextView name = binding.name;
        TextView todayDate = binding.todayDate;
        fab1Text = binding.fab1Text;
        fab2Text = binding.fab2Text;
        fab3Text = binding.fab3Text;
        logo = binding.logo;
        rv = binding.recyclerView;
        swipeRefreshLayout = binding.swipe;
        noClassView = binding.noClassView;

        name.setText(userPreferences.getUserName());

        rv.hasFixedSize();
        rv.setItemViewCacheSize(20);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(sessionAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        db = Room.databaseBuilder(getApplicationContext(),
                SessionDatabase.class,"session-database").allowMainThreadQueries().build();
        db.sessionDAO().deleteAll();

        String date = DateFormatter.DateToStringDate(currentDate);
        todayDate.setText(date);
    }

    public SessionRequest createSessionRequest() {
        SessionRequest sessionRequest = new SessionRequest();

        sessionRequest.setUser_secret(userSecret);
        sessionRequest.setDate(DateFormatter.DateToString(currentDate));

        // TODO: NANTI HAPUS KALO UDAH BENER
//        sessionRequest.setDate("2023-10-01");
        return sessionRequest;
    }

    private void saveSession(SessionRequest sessionRequest) {
        Call<Response<Session>> sessionResponseCall = ApiClient.request().saveSession(sessionRequest);
        sessionResponseCall.enqueue(new Callback<Response<Session>>() {
            @Override
            public void onResponse(@NonNull Call<Response<Session>> call, @NonNull retrofit2.Response<Response<Session>> response) {
                if (response.isSuccessful()) {
                    Response<Session> sessionResponse = response.body();
                    assert sessionResponse != null;
                    List<Session> sessionList = sessionResponse.getValues();
                    for (int i=0; i<sessionList.size(); i++) {
                        Session newSession = new Session();
                        newSession.setSessionID(sessionList.get(i).getSessionID());
                        newSession.setClassName(sessionList.get(i).getClassName());
                        newSession.setClassID(sessionList.get(i).getClassID());
                        newSession.setClassCode(sessionList.get(i).getClassCode());
                        newSession.setSessionEnd(sessionList.get(i).getSessionEnd());
                        newSession.setSessionID(sessionList.get(i).getSessionID());
                        newSession.setSessionLocation(sessionList.get(i).getSessionLocation());
                        newSession.setSessionName(sessionList.get(i).getSessionName());
                        newSession.setSessionStart(sessionList.get(i).getSessionStart());
                        newSession.setSessionEnd(sessionList.get(i).getSessionEnd());

                        db.sessionDAO().insertAllSession(newSession);
                    }
                    sortItems();

                } else {
                    if (response.code() == 404) {
                        rv.setVisibility(View.INVISIBLE);
                        noClassView.setVisibility(View.VISIBLE);
                    } else {
                        Log.e("Api Error", "Failed to Fetch Data");
//                        Toast.makeText(HomeActivity.this, "Failed to Fetch Data", Toast.LENGTH_LONG).show();
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
        sessions.clear();
        updateSession();

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

    private void updateSession () {
        sessions.clear();
        for (Session session : db.sessionDAO().getAllSessions()) {
            Date date = DateFormatter.StringToDateMillisecond(session.getSessionStart());

            if (date.after(currentDate)) {
                sessions.add(session);
            }
            // TODO: hapus kalo udah bener
//            else {
//                sessions.add(session);
//            }
        }
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

        fab3.setOnClickListener(view -> {
            animateFab();
            Intent intent = new Intent(this, DiscussActivity.class);
            startActivity(intent);
        });
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

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            updateSession();
            sessionAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }, 1000);
    }
}

