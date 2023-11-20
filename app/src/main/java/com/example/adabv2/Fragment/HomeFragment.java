package com.example.adabv2.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adabv2.Model.Session;
import com.example.adabv2.Room.SessionDatabase;
import com.example.adabv2.SessionAdapter;
import com.example.adabv2.SettingActivity;
import com.example.adabv2.UserPreferences;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.FragmentHomeBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private FragmentHomeBinding binding;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout noClassView;
    private ExtendedFloatingActionButton fabSetting;
    private RecyclerView rv;

    private SessionAdapter sessionAdapter;
    private final List<Session> sessions = new ArrayList<>();
    private final Date currentDate = new Date();
    private SessionDatabase db;
    private final Context applicationContext;

    public HomeFragment(Context applicationContect) {
        this.applicationContext = applicationContect;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        init();
        sortItems();

        return view;
    }

    private void init() {
        TextView name = binding.name;
        TextView todayDate = binding.todayDate;
        swipeRefreshLayout = binding.swipe;
        noClassView = binding.noClassView;
        rv = binding.recyclerView;
        fabSetting = binding.fabSetting;

        UserPreferences userPreferences = new UserPreferences(applicationContext);
        name.setText(userPreferences.getUserName());
        String role = userPreferences.getUserType();

        swipeRefreshLayout.setOnRefreshListener(this);
        fabSetting.setOnClickListener(this);
        sessionAdapter = new SessionAdapter(sessions, getContext());
        sessionAdapter.setUserType(role);

        rv.hasFixedSize();
        rv.setItemViewCacheSize(20);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(sessionAdapter);

        db = Room.databaseBuilder(applicationContext,
                SessionDatabase.class,"session-database").allowMainThreadQueries().build();

        String date = DateFormatter.DateToStringDate(currentDate);
        todayDate.setText(date);
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
        }
        if (sessions.isEmpty()) {
            rv.setVisibility(View.INVISIBLE);
            noClassView.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            noClassView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == fabSetting) {
            Intent intent = new Intent(getContext(), SettingActivity.class);
            startActivity(intent);
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