package com.example.adabv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.adabv2.Fragment.ClassFragment;
import com.example.adabv2.Fragment.DiscussFragment;
import com.example.adabv2.Fragment.HomeFragment;
import com.example.adabv2.Fragment.ScheduleFragment;
import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Session;
import com.example.adabv2.Model.SessionRequest;
import com.example.adabv2.Room.SessionDatabase;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton fabMenu, fabClass, fabSchedule, fabDiscuss, fabHome;
    private CardView fabHomeText, fabClassText, fabScheduleText, fabDiscussText;
    private FrameLayout popUp;
    private Animation fabOpen, fabClose;
    private ActivityMainBinding binding;

    private boolean isOpen = false;
    private String role;
    private String userSecret;
    private SessionDatabase dbSession;
    private final Date currentDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        menuOnClickListener();
        saveSession();
    }

    private void init() {
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        UserPreferences userPreferences = new UserPreferences(getApplicationContext());
        role = userPreferences.getUserType();
        userSecret = userPreferences.getUserSecret();
        switchFragment(new HomeFragment());

        fabMenu = binding.fabMenu;
        fabClass = binding.fabClass;
        fabSchedule = binding.fabSchedule;
        fabHome = binding.fabHome;
        fabDiscuss = binding.fabDiscuss;
        fabClassText = binding.fabClassText;
        fabScheduleText = binding.fabScheduleText;
        fabHomeText = binding.fabHomeText;
        fabDiscussText = binding.fabDiscussText;
        popUp = binding.popUp;

        dbSession = Room.databaseBuilder(getApplicationContext(),
                SessionDatabase.class,"session-database").allowMainThreadQueries().build();
        dbSession.sessionDAO().deleteAll();
    }

    private void menuOnClickListener () {
        fabMenu.setOnClickListener(view -> animateFab());

        fabSchedule.setOnClickListener(view -> {
            animateFab();
            switchFragment(new ScheduleFragment());
        });

        fabClass.setOnClickListener(view -> {
            animateFab();
            switchFragment(new ClassFragment());
        });

        fabHome.setOnClickListener(view -> {
            animateFab();
            switchFragment(new HomeFragment());
        });

        fabDiscuss.setOnClickListener(view -> {
            animateFab();
            switchFragment(new DiscussFragment());
        });
    }

    private void animateFab() {
        if (isOpen) {
            popUp.setVisibility(View.INVISIBLE);
            if (!role.equals("L")) {
                fabDiscuss.startAnimation(fabClose);
                fabDiscussText.startAnimation(fabClose);
                fabDiscuss.setClickable(false);
            }
            fabSchedule.startAnimation(fabClose);
            fabHome.startAnimation(fabClose);
            fabClass.startAnimation(fabClose);
            fabScheduleText.startAnimation(fabClose);
            fabClassText.startAnimation(fabClose);
            fabHomeText.startAnimation(fabClose);
            fabHome.setClickable(false);
            fabSchedule.setClickable(false);
            fabClass.setClickable(false);
            isOpen = false;
        }
        else {
            popUp.setVisibility(View.VISIBLE);
            if (!role.equals("L")) {
                fabDiscuss.startAnimation(fabOpen);
                fabDiscussText.startAnimation(fabOpen);
                fabDiscuss.setClickable(true);
            }
            fabSchedule.startAnimation(fabOpen);
            fabHome.startAnimation(fabOpen);
            fabClass.startAnimation(fabOpen);
            fabScheduleText.startAnimation(fabOpen);
            fabHomeText.startAnimation(fabOpen);
            fabClassText.startAnimation(fabOpen);
            fabSchedule.setClickable(true);
            fabHome.setClickable(true);
            fabClass.setClickable(true);
            isOpen = true;
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void saveSession() {
        SessionRequest sessionRequest = new SessionRequest();

        sessionRequest.setUser_secret(userSecret);
        sessionRequest.setDate(DateFormatter.DateToString(currentDate));

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

                        dbSession.sessionDAO().insertAllSession(newSession);
                    }

                } else {
                    if (response.code() == 404) {
                        // pastiin kalo datanya jadi kosong
                        dbSession.sessionDAO().deleteAll();
                    } else {
                        Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Response<Session>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal mengambil data", Toast.LENGTH_LONG).show();
                Log.wtf("responses", "Failed " + t.getLocalizedMessage());
            }
        });
    }
}