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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adabv2.ClassSessionActivity;
import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Search;
import com.example.adabv2.Model.SearchRequest;
import com.example.adabv2.R;
import com.example.adabv2.Room.SearchDatabase;
import com.example.adabv2.SearchAdapter;
import com.example.adabv2.UserPreferences;
import com.example.adabv2.ViewClassActivity;
import com.example.adabv2.databinding.FragmentClassBinding;
import com.example.adabv2.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ClassFragment extends Fragment implements SearchAdapter.SearchClickListener {
    private FragmentClassBinding binding;
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<Search> searches = new ArrayList<>();
    private SearchDatabase database;
    private UserPreferences userPreferences;
    private String userSecret;
    private SearchView searchView;
    private LinearLayout classNotFoundView, noClassView;

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
        database = Room.databaseBuilder(requireContext(), SearchDatabase.class, "search-database").allowMainThreadQueries().build();
        database.searchDAO().deleteAllSearch();
        setData(createSearchRequest());
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

    public void setData(SearchRequest searchRequest){
        Call<Response<Search>> responseCallSearch = ApiClient.request().searchClass(searchRequest);
        Log.wtf("masuk", "dapet panggil retrofit");
        responseCallSearch.enqueue(new Callback<Response<Search>>(){
            @Override
            public void onResponse(Call<Response<Search>> call, retrofit2.Response<Response<Search>> response) {
                if (response.isSuccessful()) {
                    searches.clear();
                    Response<Search> searchResponse  = response.body();
                    List<Search> searchList = searchResponse.getValues();
                    for (int i=0; i<searchList.size(); i++) {
                        Search newSearch = new Search();
                        newSearch.setClass_code(searchList.get(i).getClass_code());
                        newSearch.setClass_name(searchList.get(i).getClass_name());
                        newSearch.setClass_id(searchList.get(i).getClass_id());
                        newSearch.setClass_lecturer_id(searchList.get(i).getClass_lecturer_id());
//                        newSearch.setClass_type(searchList.get(i).getClass_type());

                        // Log.wtf("masuk", "dapet searches" + searches.add(newSearch));
                        searches.add(newSearch);
                        database.searchDAO().insertSearchClass(newSearch);

                    }
                    searchAdapter.notifyDataSetChanged();
                }
                else {
                    if (response.code() == 404) {
                        //mesti di cek ulang, kalo success tapi kelas ga ada
                        recyclerView.setVisibility(View.INVISIBLE);
                        noClassView.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(getContext(), "Failed to Fetch Data", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Search>> call, Throwable t) {
                Toast.makeText(getContext(),"Gagal mengambil data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public SearchRequest createSearchRequest(){
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setUser_secret(userSecret);
        Log.wtf("masuk", "user secret" + userSecret);
        Log.wtf("masuk", "dapet user secret");

        return searchRequest;
    }
    public void prepareRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        preAdapter();

    }

    public void preAdapter(){
        searchAdapter = new SearchAdapter(searches, this::selectedSearch);
        recyclerView.setAdapter(searchAdapter);
    }
}