package com.example.adabv2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adabv2.Model.Session;
import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.SessionItemBinding;

import java.util.Date;
import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.MyViewHolder> {
    private final List<Session> sessions;
    private final String userType;
    private final Context context;

    public SessionAdapter(List<Session> sessions, Context context, String userType) {
        this.sessions = sessions;
        this.context = context;
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
        final Date date = DateFormatter.stringToDateMillisecond(sessions.get(position).getSessionStart());
        final String time = DateFormatter.dateToTime(date);

        Log.wtf("testingHolder", sessions.get(position).getSessionStart() + " " + sessions.get(position).getClassCode() + " " + sessions.get(position).getClassName());

        holder.code.setText(sessions.get(position).getClassCode());
        holder.name.setText(sessions.get(position).getClassName());
        holder.time.setText(time);
        holder.location.setText(sessions.get(position).getSessionLocation());
        holder.itemView.setOnClickListener(v -> {
            Date startDate = DateFormatter.stringToDateMillisecond(sessions.get(position).getSessionStart());
            Date endDate = DateFormatter.stringToDateMillisecond(sessions.get(position).getSessionEnd());
            Date currentDate = new Date();
            // check if class has not started yet
            if (currentDate.before(startDate)) {
                Toast.makeText(context, R.string.class_not_started, Toast.LENGTH_LONG).show();
            }
            // check if current time is within interval startDate and endDate
            else if (currentDate.before(endDate) && currentDate.after(startDate) || currentDate.equals(startDate)) {
                if (userType.equals("D")) {
                    chooseLanguage(position);
                } else {
                    Intent intent = new Intent(context, TranscriptActivity.class);
                    intent.putExtra("sessionID", sessions.get(position).getSessionID());
                    intent.putExtra("sessionName", sessions.get(position).getSessionName());
                    intent.putExtra("sessionHasPassed", false);
                    context.startActivity(intent);
                }
            }
            // check if class already in the past
            else {
                Intent intent = new Intent(context, TranscriptActivity.class);
                intent.putExtra("sessionID", sessions.get(position).getSessionID());
                intent.putExtra("sessionName", sessions.get(position).getSessionName());
                intent.putExtra("sessionHasPassed", true);
                context.startActivity(intent);
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

    private void chooseLanguage(int position) {
        final String[] languages = {"Indonesia", "Inggris", "Jepang", "Mandarin"};
        final String[] languagesID = {"id-ID", "en-US", "ja-JP", "zh"};
        final String[] selectedItem = {"id-ID"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        builder.setTitle(R.string.choose_language)
                .setNegativeButton(R.string.cancel, (di, i) -> di.dismiss())
                .setSingleChoiceItems(languages, 0, (di,i) -> selectedItem[0] = languagesID[i])
                .setPositiveButton(R.string.continues, (di,i) -> {
                        Intent intent = new Intent(context, RecordRealtimeActivity.class);
                        intent.putExtra("sessionID", sessions.get(position).getSessionID());
                        intent.putExtra("sessionName", sessions.get(position).getSessionName());
                        intent.putExtra("chosenLanguage", selectedItem[0]);
                        context.startActivity(intent);
                    })
                .show();
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


