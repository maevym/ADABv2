package com.example.adabv2.Fragment;

import android.os.Bundle;

<<<<<<< Updated upstream
=======
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
>>>>>>> Stashed changes
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< Updated upstream
=======
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
>>>>>>> Stashed changes

import com.example.adabv2.R;
import com.example.adabv2.databinding.FragmentHomeBinding;
import com.example.adabv2.databinding.FragmentScheduleBinding;

public class ScheduleFragment extends Fragment {
    private FragmentScheduleBinding binding;
<<<<<<< Updated upstream
=======
    private CalendarView calendarView;
    private LinearLayout noClassView;
    private FrameLayout progressBar;
    private RecyclerView rv;
    private TextView todayDate;
    private String chosenDate;
    private String userSecret;
    private SessionAdapter sessionAdapter;
    private final List<Session> sessions = new ArrayList<>();
    private final Date currentDate = new Date();
    private SessionDatabase db;
>>>>>>> Stashed changes

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
<<<<<<< Updated upstream
=======

    private void init() {
        sessionAdapter = new SessionAdapter(sessions, getContext());
        UserPreferences userPreferences = new UserPreferences(requireContext());
        String role = userPreferences.getUserType();
        userSecret = userPreferences.getUserSecret();
        sessionAdapter.setUserType(role);

        todayDate = binding.todayDate;
        rv = binding.recyclerView;
        noClassView = binding.noClassView;
        calendarView = binding.calendarView;
        progressBar = binding.progressBar;
        progressBar.setVisibility(View.INVISIBLE);

        db = Room.databaseBuilder(requireContext(),
                SessionDatabase.class,"session-database").allowMainThreadQueries().build();

        rv.hasFixedSize();
        rv.setItemViewCacheSize(20);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(sessionAdapter);

        String date = DateFormatter.DateToStringDate(currentDate);
        todayDate.setText(date);
        chosenDate = DateFormatter.DateToString(currentDate);

        getTodayData();
    }

    private void getTodayData() {
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
        }

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
            getSessionsFromAPI();
        });
    }

    private void getSessionsFromAPI() {
        progressBar.setVisibility(View.VISIBLE);
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
                        Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                        Log.e("Api Error", "Failed to Fetch Data");
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onFailure(@NonNull Call<Response<Session>> call, @NonNull Throwable t) {
                Log.wtf("responses", "Failed " + t.getLocalizedMessage());
                Toast.makeText(getContext(), "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
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
>>>>>>> Stashed changes
}