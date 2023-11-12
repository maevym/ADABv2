package com.example.adabv2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adabv2.Model.ClassSession;
import com.example.adabv2.Model.Search;
import com.example.adabv2.databinding.ClassSessionItemBinding;
import com.example.adabv2.databinding.SearchItemBinding;

import java.util.List;

public class ClassSessionAdapter extends RecyclerView.Adapter<ClassSessionAdapter.MyViewHolder>{

    List<ClassSession> classSessionsList;
    View.OnClickListener onClickListener;

    ClassSessionAdapter.ClassSessionClickListener classSessionClickListener;

    public interface ClassSessionClickListener{
        void selectedClassSession(ClassSession classSession);
    }

    //, ClassSessionClickListener classSessionClickListener
    public ClassSessionAdapter(List<ClassSession> classSessionsList, ClassSessionClickListener classSessionClickListener){
        this.classSessionsList = classSessionsList;
        this.classSessionClickListener = classSessionClickListener;
    }

    @Override
    public ClassSessionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ClassSessionItemBinding binding;
        binding = ClassSessionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull ClassSessionAdapter.MyViewHolder holder, int position) {
        ClassSession classSessions = classSessionsList.get(position);
        Log.wtf("testingHolder", classSessionsList.get(position).getSession_id() + " " + classSessionsList.get(position).getSession_name());
        holder.sessionId.setText(String.valueOf(classSessions.getSession_id()));
        holder.sessionName.setText(classSessions.getSession_name());
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                classSessionClickListener.selectedClassSession(classSessions);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.d("Debug", "Jumlah item dalam class session " + classSessionsList.size());
        return classSessionsList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView sessionId, sessionName;
        public MyViewHolder(ClassSessionItemBinding binding) {
            super(binding.getRoot());
            sessionId = binding.classSessionItemId;
            sessionName = binding.classSessionItemSessionName;
        }
    }
}
