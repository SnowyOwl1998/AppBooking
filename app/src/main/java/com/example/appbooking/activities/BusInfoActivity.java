package com.example.appbooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.R;
import com.example.appbooking.databinding.ActivityBusInfoBinding;
import com.example.appbooking.models.BusRoute;

import java.text.NumberFormat;
import java.util.Locale;

public class BusInfoActivity extends AppCompatActivity {

    private ActivityBusInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityBusInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = this.getIntent();
        BusRoute busRoute = intent.getParcelableExtra("busInfo");
        setBusInfo(busRoute);

        binding.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(busRoute.getSlotAvailable() == 0){
                    Toast.makeText(BusInfoActivity.this, "Đã hết chỗ, vui lòng đặt xe khác", Toast.LENGTH_SHORT).show();
                }else {

                }
            }
        });
    }

    private void setBusInfo(BusRoute busRoute){
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String price = numberFormat.format(Integer.parseInt(busRoute.getPrice()));
        int slot = busRoute.getSlotAvailable();
        String slotTxt = "DEFALUT";
        if(slot == 0){
            slotTxt = "Hết chỗ";
        }else{
            slotTxt = String.valueOf(slot);
        }

        binding.fromTxt.setText(busRoute.getFrom());
        binding.toTxt.setText(busRoute.getTo());
        binding.dateTv.setText(busRoute.getDate());
        binding.timeTv.setText(busRoute.getTime());
        binding.companyTv.setText(busRoute.getCompany());
        binding.typeTv.setText(busRoute.getType());
        binding.priceTv.setText(price);
        binding.slotTv.setText(slotTxt);
    }
}
