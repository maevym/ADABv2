package com.example.adabv2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adabv2.Model.Session;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.SessionItemBinding;

import java.util.Date;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.MyViewHolder> {
    List<Session> sessions;
    String userType;
    Context context;

    public SessionAdapter(List<Session> sessions, Context context) {
        this.sessions = sessions;
        this.context = context;
    }

    public void setUserType(String userType){
        this.userType = userType;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SessionItemBinding binding;
        binding = SessionItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Date date = DateFormatter.StringToDateMillisecond(sessions.get(position).getSessionStart());
        final String time = DateFormatter.DateToTime(date);

        Log.wtf("testingHolder", sessions.get(position).getSessionStart() + " " + sessions.get(position).getClassCode() + " " + sessions.get(position).getClassName());

        holder.code.setText(sessions.get(position).getClassCode());
        holder.name.setText(sessions.get(position).getClassName());
        holder.time.setText(time);
        holder.location.setText(sessions.get(position).getSessionLocation());
        holder.itemView.setOnClickListener(v -> {
            Date startDate = DateFormatter.StringToDateMillisecond(sessions.get(position).sessionStart);
            Date endDate = DateFormatter.StringToDateMillisecond(sessions.get(position).getSessionEnd());
//            Date currentDate = new Date();
            Date currentDate = DateFormatter.StringToDateMillisecond("2023-10-01 13:10:04.100");
            // check if current time is within interval startDate and endDate
            if (currentDate.before(endDate) && currentDate.after(startDate) || currentDate.equals(startDate)) {
                if (userType.equals("D")) {
                    Intent intent = new Intent(context, RecordRealtimeActivity.class);
                    intent.putExtra("sessionID", sessions.get(position).getSessionID());
                    intent.putExtra("sessionName", sessions.get(position).getSessionName());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, TranscriptRealtimeActivity.class);
                    intent.putExtra("sessionID", sessions.get(position).getSessionID());
                    intent.putExtra("sessionName", sessions.get(position).getSessionName());
                    context.startActivity(intent);
                }
            } else {
                Log.d("session clicked", "class not started");
            }

        });
    }

    @Override
    public int getItemCount() {
        if(sessions == null){
            return 0;
        } else {
            return sessions.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView code, name, time, location;
        public MyViewHolder(@NonNull SessionItemBinding binding) {
            super(binding.getRoot());
            code = binding.itemClassCode;
            name = binding.itemClassName;
            time = binding.itemClassTime;
            location = binding.itemClassLocation;
        }
    }
}


