package com.example.adabv2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adabv2.Model.Search;
import com.example.adabv2.databinding.SearchItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    List<Search> searches;
    View.OnClickListener onClickListener;


    //new
    SearchClickListener searchClickListener;

    public interface SearchClickListener{
        void selectedSearch(Search search);
    }

    //
    public SearchAdapter(List<Search> searches, SearchClickListener searchClickListener){
        this.searches = searches;
        this.searchClickListener = searchClickListener;
    }

    public void setFilteredList(List<Search> filteredList){
        this.searches = filteredList;
        notifyDataSetChanged();
    }

    public void filterList(List<Search> filterlist) {
        searches = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchItemBinding binding;
        binding = SearchItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
//        Search item = searches.get(position);
        Search search = searches.get(position);
        holder.className.setText(searches.get(position).getClass_name());
        holder.classCode.setText(searches.get(position).getClass_code());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               searchClickListener.selectedSearch(search);
            }
        });


    }


    @Override
    public int getItemCount() {
        Log.d("Debug", "Jumlah item dalam searches: " + searches.size());
        return searches.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView className, classCode;
        public MyViewHolder(SearchItemBinding binding) {
            super(binding.getRoot());
            className = binding.searchItemClassName;
            classCode = binding.searchItemClassCode;
        }
    }
}
