package com.example.appbooking.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbooking.R;
import com.example.appbooking.adapters.ConfirmBookingAdapter;
import com.example.appbooking.databinding.ActivityConfirmBookingBinding;
import com.example.appbooking.models.BookedTicket;
import com.example.appbooking.models.BusRoute;
import com.example.appbooking.ultis.LoadingBar;
import com.example.appbooking.ultis.Server;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmBookingActivity extends AppCompatActivity {

    private ActivityConfirmBookingBinding binding;

    private ListView listViewBookedTicket;

    private ArrayList<BookedTicket> bookedTickets;

    private final LoadingBar loadingBar = new LoadingBar(ConfirmBookingActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityConfirmBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        listViewBookedTicket = findViewById(R.id.bookedTicket_Lv);
        bookedTickets = new ArrayList<>();

        ConfirmBookingAdapter adapter = new ConfirmBookingAdapter(
                ConfirmBookingActivity.this,
                R.layout.list_buses_layout,
                bookedTickets
        );

        loadingBar.showDialog();

        SharedPreferences sharedPreferences = this.getSharedPreferences("loginStatus", 0);
        String username = sharedPreferences.getString("username", null);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.urlTicket, response -> {
            if (response != null){
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String fromLocation1 = jsonObject.getString("fromlocation");
                        String toLocation1 = jsonObject.getString("tolocation");
                        String bookingDate = jsonObject.getString("bookingdate");
                        int price = jsonObject.getInt("price");
                        String timeTravel = jsonObject.getString("timetravel");
                        String company = jsonObject.getString("company");
                        int slot = jsonObject.getInt("slot");
                        String type = jsonObject.getString("type");
                        if(true){
                            bookedTickets.add(new BookedTicket(fromLocation1,
                                    toLocation1,
                                    bookingDate,
                                    timeTravel,
                                    type,
                                    company,
                                    price));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("SearchResult", "onComplete: " + bookedTickets.size());
                }
                listViewBookedTicket.setAdapter(adapter);
                loadingBar.dismissbar();
        }, error -> {
            Toast.makeText(ConfirmBookingActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            loadingBar.dismissbar();
        });
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);

//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        String phoneNumber = firebaseUser.getPhoneNumber();
//
//        loadingBar.showDialog();


//        FirebaseFirestore.getInstance().collection("User")
//                .document(phoneNumber)
//                .collection("Booked")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                                String from = queryDocumentSnapshot.getString("from");
//                                String to = queryDocumentSnapshot.getString("to");
//                                String date = queryDocumentSnapshot.getString("date");
//                                String time = queryDocumentSnapshot.getString("time");
//                                String type = queryDocumentSnapshot.getString("type");
//                                String company = queryDocumentSnapshot.getString("company");
//                                String price = queryDocumentSnapshot.getString("price");
//                                bookedTickets.add(new BookedTicket(from, to, date, time, type, company, price));
//                            }
//                            Log.d("bookedTicketSize", "onComplete: " + bookedTickets.size());
//                            listViewBookedTicket.setAdapter(adapter);
//                            loadingBar.dismissbar();
//                        }
//                    }
//                });

    }
}