package com.example.adabv2.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.adabv2.ChatGroupActivity;
import com.example.adabv2.ClassSessionActivity;
import com.example.adabv2.DiscussAdapter;
import com.example.adabv2.Model.Discuss;
import com.example.adabv2.Room.DiscussDatabase;
import com.example.adabv2.UserPreferences;
import com.example.adabv2.databinding.FragmentClassBinding;

import java.util.ArrayList;
import java.util.List;

public class DiscussFragment extends Fragment implements DiscussAdapter.DiscussClickListener {
    private FragmentClassBinding binding;
    private RecyclerView recyclerView;
    private DiscussAdapter discussAdapter;
    private List<Discuss> discusses = new ArrayList<>();
    private DiscussDatabase dbDiscuss;
    private UserPreferences userPreferences;
    private String userSecret;
    private SearchView searchView;
    private LinearLayout classNotFoundView, noClassView;
    private FrameLayout progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentClassBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        classNotFoundView = binding.classNotFoundView;
        noClassView = binding.noClassView;
        recyclerView = binding.rvViewClass;
        userPreferences = new UserPreferences(requireContext());
        searchView = binding.searchClasses;
        progressBar = binding.progressBar;
        progressBar.setVisibility(View.INVISIBLE);
        searchView.setQueryHint("Cari Kelas");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        userSecret = userPreferences.getUserSecret();
        dbDiscuss = Room.databaseBuilder(requireContext(), DiscussDatabase.class, "searchdiscuss-database").allowMainThreadQueries().build();
        //dbDiscuss = Room.databaseBuilder(getContext(), DiscussDatabase.class, "searchdiscuss-database").fallbackToDestructiveMigration().build();
        callFuncAPI();
        prepareRecyclerView();

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

        return view;
    }


    @Override
    public void selectedMember(Discuss discuss) {
        Intent intent = new Intent(getActivity(), ChatGroupActivity.class);
        intent.putExtra("className", discuss.getClass_name());
        intent.putExtra("classId", discuss.getClass_id());
        intent.putExtra("classType", discuss.getClass_type());
        startActivity(intent);
    }

    private void filterList(String text) {
        List<Discuss> filteredList = new ArrayList<>();
        for(Discuss discuss : discusses){
            if(discuss.getClass_code().toLowerCase().contains(text.toLowerCase()) || discuss.getClass_name().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(discuss);
            }
        }

        if (filteredList.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            classNotFoundView.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            classNotFoundView.setVisibility(View.INVISIBLE);
            discussAdapter.setFilteredList(filteredList);
        }
    }

    private void callFuncAPI (){
        progressBar.setVisibility(View.VISIBLE);
        discusses.clear();
        List<Discuss> discuss = dbDiscuss.discussWithMember().getAllMember();
        for (Discuss discussing : discuss) {
            Log.wtf("get di add ","coba ");
            discusses.add(discussing);
            Log.wtf("searching di add ","coba ");
        }

        if (discusses.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            noClassView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noClassView.setVisibility(View.INVISIBLE);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }


    private void prepareRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        preAdapter();

    }

    private void preAdapter(){
        discussAdapter = new DiscussAdapter(discusses, this::selectedMember);
        recyclerView.setAdapter(discussAdapter);
    }




}