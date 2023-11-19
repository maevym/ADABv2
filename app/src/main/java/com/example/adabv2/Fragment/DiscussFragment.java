package com.example.adabv2.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adabv2.R;
import com.example.adabv2.databinding.FragmentDiscussBinding;
import com.example.adabv2.databinding.FragmentHomeBinding;

public class DiscussFragment extends Fragment {
    private FragmentDiscussBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDiscussBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        return view;
    }
}