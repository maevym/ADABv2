package com.example.adabv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adabv2.Manager.ApiClient;
import com.example.adabv2.Model.Response;
import com.example.adabv2.Model.Search;
import com.example.adabv2.Model.SearchRequest;
import com.example.adabv2.Room.SearchDatabase;
import com.example.adabv2.databinding.ActivityViewClassBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewClassActivity extends AppCompatActivity implements SearchAdapter.SearchClickListener {

    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<Search> searches = new ArrayList<>();
    private SearchDatabase database;
    private UserPreferences userPreferences;
    private String userSecret;
    private ActivityViewClassBinding binding;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewClassBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = binding.rvViewClass;
        userPreferences = new UserPreferences(getApplicationContext());
        searchView = binding.searchClasses;
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
        database = Room.databaseBuilder(getApplicationContext(), SearchDatabase.class, "search-database").allowMainThreadQueries().build();
        database.searchDAO().deleteAllSearch();
        setData(createSearchRequest());
        prepareRecyclerView();

    }

    private void filterList(String text) {
        List<Search> filteredList = new ArrayList<>();
        for(Search search : searches){
            if(search.getClass_code().toLowerCase().contains(text.toLowerCase()) || search.getClass_name().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(search);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
        else {
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
//                            Log.wtf("masuk", "notify");
                        searches.add(newSearch);
                        database.searchDAO().insertSearchClass(newSearch);

                    }
                    searchAdapter.notifyDataSetChanged();
//                    Toast.makeText(AllClassActivity.this,"Search Successful", Toast.LENGTH_LONG).show();
                }
                else {
                    if (response.code() == 404) {
                        recyclerView.setVisibility(View.INVISIBLE);

                    } else {
                        Toast.makeText(ViewClassActivity.this, "Failed to Fetch Data", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<Search>> call, Throwable t) {

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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        preAdapter();

    }

    public void preAdapter(){
        searchAdapter = new SearchAdapter(searches, this::selectedSearch);
        recyclerView.setAdapter(searchAdapter);
    }

    @Override
    public void selectedSearch(Search search) {
//        Toast.makeText(this, "Selected Search " + search.getClass_name(), Toast.LENGTH_SHORT).show();
//       Toast.makeText(this, "Selected Search " + search.getClass_id() + " " + search.getClass_name() + " " + search.getClass_code() + "" + search.getClass_lecturer_id(), Toast.LENGTH_LONG).show();
        int classId = search.getClass_id();
        userPreferences.setClassId(classId);
        Log.wtf("class id view woi", String.valueOf(classId));
//        Intent intent = new Intent(ViewClassActivity.this, SelectedClassSessionActivity.class);
        Intent intent = new Intent(ViewClassActivity.this, ClassSessionActivity.class);
        startActivity(intent);
    }
}