package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.ClassSession;
import com.example.adabv2.Model.ClassSessionRequest;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Search;
import com.example.adabv2.Model.SearchRequest;
import com.example.adabv2.Room.ClassSessionDatabase;
import com.example.adabv2.Room.SearchDatabase;
import com.example.adabv2.databinding.ActivityClassSessionBinding;
import com.example.adabv2.databinding.ActivityViewClassBinding;
import com.example.adabv2.databinding.ClassSessionItemBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class ClassSessionActivity extends AppCompatActivity implements ClassSessionAdapter.ClassSessionClickListener{

    private ActivityClassSessionBinding binding;
    private RecyclerView recyclerViewClassSession;
    private ClassSessionAdapter classSessionAdapter;
    private List<ClassSession> classSessionList = new ArrayList<>();
    private ClassSessionDatabase dbClassSession;
    private ImageView backButton;
    private TextView textViewNameClass;
    private LinearLayout noSessionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        recyclerViewClassSession = binding.recyclerViewClassSession;
        backButton = binding.buttonBackSession;
        textViewNameClass = binding.choosenClassName;
        noSessionView = binding.noSessionView;

        dbClassSession = Room.databaseBuilder(getApplicationContext(), ClassSessionDatabase.class, "classsession-database").allowMainThreadQueries().build();
        callFuncAPI();
        prepareRecyclerView();
        Log.wtf("masuk on create","masuk");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        textViewNameClass.setText(getIntent().getStringExtra("class"));



    }

    public void callFuncAPI (){
        classSessionList.clear();
        List<ClassSession> classSessions = dbClassSession.classSessionDAO().getAllClassSession();
        for (ClassSession classSession : classSessions) {
            classSessionList.add(classSession);
        }

        if (classSessionList.isEmpty()) {
            recyclerViewClassSession.setVisibility(View.INVISIBLE);
            noSessionView.setVisibility(View.VISIBLE);
        } else {
            recyclerViewClassSession.setVisibility(View.VISIBLE);
            noSessionView.setVisibility(View.INVISIBLE);
        }
    }

    public void prepareRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewClassSession.setLayoutManager(linearLayoutManager);
        preAdapter();
        Log.wtf("masuk prepareRecycler","masuk");

    }


    public void preAdapter(){
        classSessionAdapter = new ClassSessionAdapter(classSessionList,this::selectedClassSession);
        recyclerViewClassSession.setAdapter(classSessionAdapter);
        Log.wtf("masuk preAdapter","masuk");
    }


    @Override
    public void selectedClassSession(ClassSession classSession) {
        Toast.makeText(this, "Selected Class Session " + classSession.getSession_name(), Toast.LENGTH_SHORT).show();
    }
}