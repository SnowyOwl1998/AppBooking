package com.example.appbooking.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.R;
import com.example.appbooking.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private String fromTxt = null, toTxt = null, dateTxt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = this.getSharedPreferences("loginStatus", 0);
        Boolean isLogin = sharedPreferences.getBoolean("isLogin", false);

        binding.searchBuses.setOnClickListener(view -> {
            if(!isLogin){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Vui lòng đăng nhập trước", Toast.LENGTH_SHORT).show();
            } else {
                searchBus(fromTxt, toTxt, dateTxt);
            }
        });

        binding.loginIcon.setOnClickListener(view -> {
            if(!isLogin){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.dateEdt.setOnClickListener(v -> datePick());

        String[] cityArrays = getResources().getStringArray(R.array.city_arrays);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, cityArrays);
        binding.fromSpn.setAdapter(adapter);
        binding.toSpn.setAdapter(adapter);

        binding.fromSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromTxt = cityArrays[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.toSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toTxt = cityArrays[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void datePick(){
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            calendar.set(year1, month1, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            binding.dateEdt.setText(simpleDateFormat.format(calendar.getTime()));
            dateTxt = simpleDateFormat.format(calendar.getTime());
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }, year, month, date);
        datePickerDialog.show();
    }

    private void searchBus(String fromLocation, String toLocation, String date){
        Intent intent = new Intent(getApplicationContext(), ListBusesActivity.class);
        intent.putExtra("fromLocation", fromLocation);
        intent.putExtra("toLocation", toLocation);
        intent.putExtra("date", date);
        startActivity(intent);
    }
}
