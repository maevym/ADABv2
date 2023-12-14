package com.example.adabv2.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adabv2.DiscussAdapter;
import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Discuss;
import com.example.adabv2.Model.DiscussRequest;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Room.DiscussDatabase;
import com.example.adabv2.UserPreferences;
import com.example.adabv2.databinding.FragmentDiscussBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DiscussFragment extends Fragment implements DiscussAdapter.DiscussClickListener {
    private FragmentDiscussBinding binding;
    private RecyclerView recyclerViewDiscussMember;
    private DiscussAdapter discussAdapter;
    private List<Discuss> discussList = new ArrayList<>();
    private UserPreferences userPreferences;
    private int class_id;
    private DiscussDatabase database;
    private SearchView searchViewDiscuss;
    private LinearLayout noMemberView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiscussBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        noMemberView = binding.noMemberView;
        recyclerViewDiscussMember = binding.rvDiscussMember;
        userPreferences = new UserPreferences(requireContext());
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


        userPreferences = new UserPreferences(requireContext());
        class_id = userPreferences.getClassId();
        database = Room.databaseBuilder(requireContext(), DiscussDatabase.class, "discuss-database").allowMainThreadQueries().build();
        database.discussWithMember().deleteAllSMember();
        setData(createDiscussRequest());
        prepareRecyclerView();


        return view;
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
                        Toast.makeText(getContext(), "Failed to Fetch Data", Toast.LENGTH_LONG).show();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDiscussMember.setLayoutManager(linearLayoutManager);
        preAdapter();

    }

    public void preAdapter(){
        discussAdapter = new DiscussAdapter(discussList, this::selectedMember);
        recyclerViewDiscussMember.setAdapter(discussAdapter);
    }

    @Override
    public void selectedMember(Discuss discuss) {

    }
}