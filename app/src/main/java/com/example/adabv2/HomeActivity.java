package com.example.adabv2;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.adabv2.Util.DateFormatter;
import com.example.adabv2.databinding.ActivityHomeBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

public class HomeActivity extends AppCompatActivity {
    private Animation fabOpen, fabClose;
    private boolean isOpen = false;
    private Vector<Item> items;
    private Vector<Item> futureItems = new Vector<Item>();
    private int role = 1;// user role from user defaults -> LSC itu di set 0
    private ActivityHomeBinding binding;
    private Date currentDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addItems();
        sortItems();
        init();
        menuOnClickListener();
    }

    private void addItems () {
        items = new Vector<>();
        items.add(new Item("Mobile Multimedia Solutions","LA03","07:20 am", "25/09/2023"));
        items.add(new Item("Mobile Community Solutions","LA03","08:20 pm","25/09/2023"));
        items.add(new Item("Human Computer Interactions","LA03","1:20 pm","26/09/2023"));
        items.add(new Item("Mobile Community Solutions","LA03","09:20 am","30/09/2023"));
        items.add(new Item("Mobile Community Solutions","LA03","05:20 pm","21/09/2023"));
        items.add(new Item("Mobile Community Solutions","LA03","05:20 pm","21/09/2023"));
        items.add(new Item("Mobile Community Solutions","LA03","10:20 pm","27/09/2023"));
        items.add(new Item("Mobile Community Solutions","LA03","11:20 pm","29/09/2023"));
    }

    private void init () {
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        binding.homeRecyclerView.hasFixedSize();
        binding.homeRecyclerView.setItemViewCacheSize(20);
        binding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.homeRecyclerView.setAdapter(new ItemAdapter(futureItems));

        String date = DateFormatter.DateToString(currentDate);
        binding.homeTodayDate.setText(date);
    }

    private void sortItems () {
        for (Item item : items) {
            Date date = DateFormatter.StringToDate(item.getDate() + " " + item.getTime());

            if (currentDate.before(date)) {
                futureItems.add(item);
            }
        }

        Collections.sort(futureItems, new Comparator<Item>() {
            @Override
            public int compare(Item item, Item t1) {
                try {
                    return new SimpleDateFormat("hh:mm a").parse(item.getTime()).compareTo(new SimpleDateFormat("hh:mm a").parse(t1.getTime()));
                } catch (ParseException e) {
                    Log.wtf("error", e);
                    return 0;
                }
            }
        });
    }

    private void menuOnClickListener () {
        binding.fab.setOnClickListener(view -> {
            animateFab();
        });

        binding.fab1.setOnClickListener(view -> {
            animateFab();
            // intent to schedule view

        });

        binding.fab2.setOnClickListener(view -> {
            animateFab();
            // intent to class view

        });

        binding.fab3.setOnClickListener(view -> {
            animateFab();
            // intent to class view

        });
    }

    private void animateFab() {
        if (isOpen) {
            if (role != 0) {
                binding.fab3.startAnimation(fabClose);
                binding.fab3Text.startAnimation(fabClose);
                binding.fab3.setClickable(false);
            }
            binding.fab1.startAnimation(fabClose);
            binding.fab2.startAnimation(fabClose);
            binding.fab1Text.startAnimation(fabClose);
            binding.fab2Text.startAnimation(fabClose);
            binding.fab1.setClickable(false);
            binding.fab2.setClickable(false);
            isOpen = false;
        } else {
            if (role != 0) {
                binding.fab3.startAnimation(fabOpen);
                binding.fab3Text.startAnimation(fabOpen);
                binding.fab3.setClickable(true);
            }
            binding.fab1.startAnimation(fabOpen);
            binding.fab2.startAnimation(fabOpen);
            binding.fab1Text.startAnimation(fabOpen);
            binding.fab2Text.startAnimation(fabOpen);
            binding.fab1.setClickable(true);
            binding.fab2.setClickable(true);
            isOpen = true;
        }
    }
}

