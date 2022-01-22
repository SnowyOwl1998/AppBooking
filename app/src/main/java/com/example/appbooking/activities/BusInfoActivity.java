package com.example.appbooking.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbooking.databinding.ActivityBusInfoBinding;
import com.example.appbooking.models.BusRoute;
import com.example.appbooking.ultis.LoadingBar;
import com.example.appbooking.ultis.Server;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BusInfoActivity extends AppCompatActivity {

    private ActivityBusInfoBinding binding;

    private final LoadingBar loadingBar = new LoadingBar(BusInfoActivity.this);

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(BusInfoActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Xác nhận");
                    builder.setMessage("Bạn có muốn đặt vé xe không?");
                    builder.setPositiveButton("Xác nhận",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    confirmBooking(busRoute);
                                }
                            });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(BusInfoActivity.this, "Hủy đặt vé xe", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void setBusInfo(BusRoute busRoute){
        Locale locale = new Locale("vi", "VN");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String price = numberFormat.format(busRoute.getPrice());
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

    private void confirmBooking(BusRoute busRoute){

        loadingBar.showDialog();

        SharedPreferences sharedPreferences = this.getSharedPreferences("loginStatus", 0);
        String username = sharedPreferences.getString("username", null);

        Log.d("TAG", "confirmBooking: " + username + " - " + busRoute.getId());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Server.urlBooking, response -> {
            String message = "";
            try{
                JSONObject jsonObject = new JSONObject(response);
                Log.d("TAG", "onResponse: " + response);
                if(jsonObject.getInt("command") == 1){
                    message = jsonObject.getString("message");
                    Log.d("Booking", "onResponse: " + message);
                    finish();
                } else if(jsonObject.getInt("command") == 0) {
                    message = jsonObject.getString("message");
                    Log.d("Booking", "onResponse: " + message);
                } else {
                    message = jsonObject.getString("message");
                    Log.d("Booking", "onResponse: " + message);
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loadingBar.dismissbar();
        }, error -> loadingBar.dismissbar()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("busrouteid", String.valueOf(busRoute.getId()));
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);

//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String phoneNumber = firebaseUser.getPhoneNumber();
//
//        Map<String, Object> bookedTicket = new HashMap<>();
//        bookedTicket.put("from", busRoute.getFrom());
//        bookedTicket.put("to", busRoute.getTo());
//        bookedTicket.put("date", busRoute.getDate());
//        bookedTicket.put("time", busRoute.getTime());
//        bookedTicket.put("type", busRoute.getType());
//        bookedTicket.put("company", busRoute.getCompany());
//        bookedTicket.put("price", busRoute.getPrice());
//
//        FirebaseFirestore.getInstance().collection("User")
//                .document(phoneNumber)
//                .collection("Booked")
//                .document()
//                .set(bookedTicket)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Intent intent = new Intent(getApplicationContext(), ConfirmBookingActivity.class);
//                        startActivity(intent);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("confirmBooking", "onFailure: ", e);
//            }
//        });


    }
}
