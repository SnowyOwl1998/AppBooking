package com.example.appbooking.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appbooking.R;
import com.example.appbooking.adapters.ConfirmBookingAdapter;
import com.example.appbooking.databinding.ActivityConfirmBookingBinding;
import com.example.appbooking.models.BookedTicket;
import com.example.appbooking.models.BusRoute;
import com.example.appbooking.ultis.LoadingBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String phoneNumber = firebaseUser.getPhoneNumber();

        loadingBar.showDialog();


        FirebaseFirestore.getInstance().collection("User")
                .document(phoneNumber)
                .collection("Booked")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                String from = queryDocumentSnapshot.getString("from");
                                String to = queryDocumentSnapshot.getString("to");
                                String date = queryDocumentSnapshot.getString("date");
                                String time = queryDocumentSnapshot.getString("time");
                                String type = queryDocumentSnapshot.getString("type");
                                String company = queryDocumentSnapshot.getString("company");
                                String price = queryDocumentSnapshot.getString("price");
                                bookedTickets.add(new BookedTicket(from, to, date, time, type, company, price));
                            }
                            Log.d("bookedTicketSize", "onComplete: " + bookedTickets.size());
                            listViewBookedTicket.setAdapter(adapter);
                            loadingBar.dismissbar();
                        }
                    }
                });

    }
}