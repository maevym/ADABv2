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
import com.example.adabv2.Room.ClassSessionDatabase;
import com.example.adabv2.databinding.ActivityClassSessionBinding;

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
    private int classId;
    private TextView textViewNameClass;
    private LinearLayout noSessionView;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClassSessionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        userPreferences = new UserPreferences(getApplicationContext());
        classId = userPreferences.getClassId();
        recyclerViewClassSession = binding.recyclerViewClassSession;
        backButton = binding.buttonBackSession;
        textViewNameClass = binding.choosenClassName;
        noSessionView = binding.noSessionView;

        dbClassSession = Room.databaseBuilder(getApplicationContext(), ClassSessionDatabase.class, "classsession-database").allowMainThreadQueries().build();
        //dbClassSession = Room.databaseBuilder(getApplicationContext(), ClassSessionDatabase.class, "classsession-database").allowMainThreadQueries().build();
        dbClassSession.classSessionDAO().deleteAllClassSession();

        //        callFuncAPI();
        setData(createClassSessionRequest());
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

    public void setData(ClassSessionRequest classSessionRequest){
        Call<Response<ClassSession>> responseCallClassSession = ApiClient.request().classSessionAPI(classSessionRequest);
        Log.wtf("masuk", "dapet panggil retrofit");
        responseCallClassSession.enqueue(new Callback<Response<ClassSession>>(){
            @Override
            public void onResponse(Call<Response<ClassSession>> call, retrofit2.Response<Response<ClassSession>> response) {
                if (response.isSuccessful()) {
                    classSessionList.clear();
                    Response<ClassSession> classSessionResponse  = response.body();
                    List<ClassSession> sessionList = classSessionResponse.getValues();
                    for (int i=0; i<sessionList.size(); i++) {
                        ClassSession newSession = new ClassSession();
                        newSession.setSession_id(sessionList.get(i).getSession_id());

                        Log.wtf("berhasil session id ", String.valueOf(sessionList.get(i).getSession_id()));

                        newSession.setSession_name(sessionList.get(i).getSession_name());
                        Log.wtf("berhasil session name ", sessionList.get(i).getSession_name());

                        newSession.setClass_id(sessionList.get(i).getClass_id());
                        Log.wtf("berhasil class id ", String.valueOf(sessionList.get(i).getClass_id()));

                        newSession.setSession_start(sessionList.get(i).getSession_start());
                        Log.wtf("berhasil session start ", sessionList.get(i).getSession_start());

                        newSession.setSession_end(sessionList.get(i).getSession_end());
                        Log.wtf("berhasil session end ", sessionList.get(i).getSession_end());

                        newSession.setTime_start(sessionList.get(i).getTime_start());
                        Log.wtf("berhasil time start ", sessionList.get(i).getTime_start());

                        newSession.setTime_end(sessionList.get(i).getTime_end());
                        Log.wtf("berhasil time end ", sessionList.get(i).getTime_end());

                        //Log.wtf("masuk", "dapet class add" + classSessionList.add(newSession));
                        classSessionList.add(newSession);
                        dbClassSession.classSessionDAO().insertClassSession(newSession);

                    }
                    classSessionAdapter.notifyDataSetChanged();
                    Log.wtf("masuk notify","masuk");
//                    Toast.makeText(AllClassActivity.this,"Search Successful", Toast.LENGTH_LONG).show();
                }
                else {
                    if (response.code() == 404) {
                        recyclerViewClassSession.setVisibility(View.INVISIBLE);
                        noSessionView.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(ClassSessionActivity.this, "Gagal mengambil data", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ClassSession>> call, Throwable t) {
                Toast.makeText(ClassSessionActivity.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public ClassSessionRequest createClassSessionRequest(){
        ClassSessionRequest classSessionRequest = new ClassSessionRequest();
        classSessionRequest.setClass_id(classId);
        Log.wtf("masuk class id", String.valueOf(classId));

        return classSessionRequest;
    }

//    public void callFuncAPI (){
//        classSessionList.clear();
//        List<ClassSession> classSessions = dbClassSession.classSessionDAO().getAllClassSession();
//        for (ClassSession classSession : classSessions) {
//            classSessionList.add(classSession);
//        }
//
//        if (classSessionList.isEmpty()) {
//            recyclerViewClassSession.setVisibility(View.INVISIBLE);
//            noSessionView.setVisibility(View.VISIBLE);
//        } else {
//            recyclerViewClassSession.setVisibility(View.VISIBLE);
//            noSessionView.setVisibility(View.INVISIBLE);
//        }
//    }

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