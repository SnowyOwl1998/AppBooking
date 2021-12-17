package com.example.appbooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.appbooking.R;
import com.example.appbooking.databinding.ActivityListBusesBinding;

public class ListBusesActivity extends AppCompatActivity {

    private ActivityListBusesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityListBusesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), BusInfoActivity.class);
            startActivity(intent);
        });
        binding.backBtn.setOnClickListener(view -> finish());
    }
}
