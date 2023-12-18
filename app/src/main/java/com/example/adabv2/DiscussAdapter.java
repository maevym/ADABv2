package com.example.adabv2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adabv2.Model.Discuss;
import com.example.adabv2.databinding.DiscussItemBinding;

import java.util.List;

public class DiscussAdapter extends RecyclerView.Adapter<DiscussAdapter.MyViewHolder> {
    List<Discuss> discussList;
    View.OnClickListener onClickListener;

    DiscussClickListener discussClickListener;

    public interface DiscussClickListener{
        void selectedMember(Discuss discuss);
    }


    public DiscussAdapter(List<Discuss> discussList, DiscussClickListener discussClickListener){
        this.discussList = discussList;
        this.discussClickListener = discussClickListener;
    }

    public void setFilteredList(List<Discuss> filteredList){
        this.discussList = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DiscussAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DiscussItemBinding binding;
        binding = DiscussItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussAdapter.MyViewHolder holder, int position) {
        Discuss discuss = discussList.get(position);
        holder.namaDiscussMember.setText(discussList.get(position).getClass_name());
        holder.nimDiscussMember.setText(discussList.get(position).getClass_code());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discussClickListener.selectedMember(discuss);
            }
        });

    }

    @Override
    public int getItemCount() {
        //Log.d("Debug", "Jumlah item dalam searches discuss: " + discussList.size());
        return discussList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
    TextView namaDiscussMember, nimDiscussMember;
        public MyViewHolder(@NonNull DiscussItemBinding binding) {
            super(binding.getRoot());
            namaDiscussMember = binding.discussItemMemberName;
            nimDiscussMember = binding.discussItemMemberNim;
        }
    }
}
