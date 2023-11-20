package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Discuss;
import com.example.adabv2.Model.DiscussRequest;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Room.DiscussDatabase;
import com.example.adabv2.databinding.ActivityDiscussBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DiscussActivity extends AppCompatActivity implements DiscussAdapter.DiscussClickListener{


    private RecyclerView recyclerViewDiscussMember;
    private DiscussAdapter discussAdapter;
    private List<Discuss> discussList = new ArrayList<>();
    private UserPreferences userPreferences;
    private int class_id;
    private ActivityDiscussBinding binding;
    private DiscussDatabase database;
    private SearchView searchViewDiscuss;
    private LinearLayout noMemberView;

    private String role;
    private ExtendedFloatingActionButton fab, fab1, fab2, fab3;
    private CardView fab1Text, fab2Text, fab3Text;
    private Animation fabOpen, fabClose;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiscussBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        noMemberView = binding.noMemberView;
        recyclerViewDiscussMember = binding.rvDiscussMember;
        userPreferences = new UserPreferences(getApplicationContext());
        searchViewDiscuss = binding.searchDiscuss;
        searchViewDiscuss.setQueryHint("Cari Nama");

        searchViewDiscuss.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.wtf("berhasil masuk text submit", "search");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                Log.wtf("berhasil masuk text change", "search 2");
                return true;
            }
        });


        userPreferences = new UserPreferences(getApplicationContext());
        class_id = userPreferences.getClassId();
        role = userPreferences.getUserType();
        fab = binding.fab;
        fab1 = binding.fab1;
        fab2 = binding.fab2;
        fab3 = binding.fab3;
        fab1Text = binding.fab1Text;
        fab2Text = binding.fab2Text;
        fab3Text = binding.fab3Text;
        database = Room.databaseBuilder(getApplicationContext(), DiscussDatabase.class, "discuss-database").allowMainThreadQueries().build();
        database.discussWithMember().deleteAllSMember();
        setData(createDiscussRequest());
        prepareRecyclerView();
        menuOnClickListener();

    }

    private void filterList(String text) {
        List<Discuss> filteredList = new ArrayList<>();
        for(Discuss discuss : discussList){
            Log.wtf("berhasil filter", discuss.getName());
            Log.wtf("berhasil filter 2", discuss.getUser_unique());
            if(discuss.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(discuss);
            }
        }

        if (filteredList.isEmpty()){
            recyclerViewDiscussMember.setVisibility(View.INVISIBLE);
            noMemberView.setVisibility(View.VISIBLE);
            //Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else {
            recyclerViewDiscussMember.setVisibility(View.VISIBLE);
            noMemberView.setVisibility(View.INVISIBLE);
            discussAdapter.setFilteredList(filteredList);
        }
    }


    public void setData(DiscussRequest discussRequest){
        Call<Response<Discuss>> responseCallDiscuss = ApiClient.request().discussSearch(discussRequest);
        Log.wtf("masuk", "dapet panggil retrofit");
        responseCallDiscuss.enqueue(new Callback<Response<Discuss>>() {
            @Override
            public void onResponse(Call<Response<Discuss>> call, retrofit2.Response<Response<Discuss>> response) {
                if (response.isSuccessful()) {
                    discussList.clear();
                    Response<Discuss> discussResponse  = response.body();
                    List<Discuss> discusses = discussResponse.getValues();
                    for (int i=0; i<discusses.size(); i++) {
                        Discuss newMember = new Discuss();
                        newMember.setName(discusses.get(i).getName());
                        Log.wtf("berhasil name", discusses.get(i).getName());
                        newMember.setUser_unique(discusses.get(i).getUser_unique());
                        Log.wtf("berhasil nim", discusses.get(i).getUser_unique());
                        newMember.setUser_id(discusses.get(i).getUser_id());
                        Log.wtf("berhasil id", String.valueOf(discusses.get(i).getUser_id()));

                        // Log.wtf("masuk", "dapet searches" + searches.add(newSearch));
                        discussList.add(newMember);
                        //Log.wtf("masuk", "dapet searches" + discussList.add(newMember));
                        database.discussWithMember().insertDiscussMember(newMember);

                    }
                    discussAdapter.notifyDataSetChanged();
                }
                else {
                    if (response.code() == 404) {
                        recyclerViewDiscussMember.setVisibility(View.INVISIBLE);
                        //recyclerViewDiscussMember.setVisibility(View.INVISIBLE);
                        noMemberView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(DiscussActivity.this, "Failed to Fetch Data", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Response<Discuss>> call, Throwable t) {

            }
        });


    }

    public DiscussRequest createDiscussRequest(){
        DiscussRequest discussRequest = new DiscussRequest();
        discussRequest.setClass_id(class_id);
        Log.wtf("masuk", "class id " + class_id);
        return discussRequest;
    }

    public void prepareRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDiscussMember.setLayoutManager(linearLayoutManager);
        preAdapter();

    }

    public void preAdapter(){
        discussAdapter = new DiscussAdapter(discussList, this::selectedMember);
        recyclerViewDiscussMember.setAdapter(discussAdapter);
    }


    private void menuOnClickListener () {
        fab.setOnClickListener(view -> {
            animateFab();
        });

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
    public void selectedMember(Discuss discuss) {
        Toast.makeText(this, "Selected Member " + discuss.getName(), Toast.LENGTH_SHORT).show();
        // intent chat nanti disini
    }
}