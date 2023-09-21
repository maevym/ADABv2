package com.example.adabv2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adabv2.databinding.ItemViewBinding;

import java.util.Vector;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
    Vector<Item> list;

    public ItemAdapter(Vector<Item> list) {
        this.list = list;
    }
    private ItemViewBinding binding;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.code.setText(list.get(position).getCode());
        holder.name.setText(list.get(position).getName());
        holder.time.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        } else {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView code, name, time;
        public MyViewHolder(@NonNull ItemViewBinding binding) {
            super(binding.getRoot());
            code = binding.itemClassCode;
            name = binding.itemClassName;
            time = binding.itemClassTime;
        }

        @Override
        public void onClick(View view) {

        }
    }
}


