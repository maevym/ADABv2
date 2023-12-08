package com.example.adabv2.Fragment;

import android.content.Intent;
import android.os.Bundle;

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
import android.widget.Toast;

import com.example.adabv2.ClassSessionActivity;
import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Search;
import com.example.adabv2.Model.SearchRequest;
import com.example.adabv2.Model.Session;
import com.example.adabv2.R;
import com.example.adabv2.Room.SearchDatabase;
import com.example.adabv2.SearchAdapter;
import com.example.adabv2.UserPreferences;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.ViewClassActivity;
import com.example.adabv2.databinding.FragmentClassBinding;
import com.example.adabv2.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ClassFragment extends Fragment implements SearchAdapter.SearchClickListener {
    private FragmentClassBinding binding;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<Search> searches = new ArrayList<>();
    private SearchDatabase dbSearch;
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
        dbSearch = Room.databaseBuilder(requireContext(), SearchDatabase.class, "search-database").allowMainThreadQueries().build();
        callFuncAPI();
        prepareRecyclerView();

        return view;
    }

    @Override
    public void selectedSearch(Search search) {
        int classId = search.getClass_id();
        userPreferences.setClassId(classId);
        Log.wtf("class id view woi", String.valueOf(classId));
        Intent intent = new Intent(getActivity(), ClassSessionActivity.class);
        intent.putExtra("class", search.getClass_name());
        startActivity(intent);
    }

    private void filterList(String text) {
        List<Search> filteredList = new ArrayList<>();
        for(Search search : searches){
            if(search.getClass_code().toLowerCase().contains(text.toLowerCase()) || search.getClass_name().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(search);
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
            searchAdapter.setFilteredList(filteredList);
        }
    }

    private void callFuncAPI (){
        progressBar.setVisibility(View.VISIBLE);
        searches.clear();
        List<Search> search = dbSearch.searchDAO().getAllSearch();
        for (Search searching : search) {
            Log.wtf("get di add ","coba ");
            searches.add(searching);
            Log.wtf("searching di add ","coba ");
        }

        if (searches.isEmpty()) {
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
        searchAdapter = new SearchAdapter(searches, this::selectedSearch);
        recyclerView.setAdapter(searchAdapter);
    }
}