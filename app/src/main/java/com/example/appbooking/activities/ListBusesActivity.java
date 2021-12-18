package com.example.appbooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.R;
import com.example.appbooking.adapters.ListBusesAdapter;
import com.example.appbooking.databinding.ActivityListBusesBinding;
import com.example.appbooking.models.BusRoute;
import com.example.appbooking.ultis.LoadingBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListBusesActivity extends AppCompatActivity {

    private ActivityListBusesBinding binding;

    private final LoadingBar loadingBar = new LoadingBar(ListBusesActivity.this);

    private ListView listViewBusRoute;

    private ArrayList<BusRoute> busRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityListBusesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtn.setOnClickListener(view -> finish());

        Intent intent = this.getIntent();
        String fromLocation = intent.getStringExtra("fromLocation");
        String toLocation = intent.getStringExtra("toLocation");
        String date = intent.getStringExtra("date");

        binding.fromTxt.setText(fromLocation);
        binding.toTxt.setText(toLocation);
        binding.dateTv.setText(date);

        listViewBusRoute = findViewById(R.id.busRoute_Lv);
        busRoutes = new ArrayList<>();

        ListBusesAdapter adapter = new ListBusesAdapter(
                ListBusesActivity.this,
                R.layout.list_buses_layout,
                busRoutes
        );

        loadingBar.showDialog();

        FirebaseFirestore.getInstance().collection("BusRoute")
                .whereEqualTo("from", fromLocation)
                .whereEqualTo("to", toLocation)
                .whereEqualTo("date", date)
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
                                Integer slotAvailable = Integer.parseInt(queryDocumentSnapshot.get("slotAvailable").toString());
                                busRoutes.add(new BusRoute(from, to, date, time, type, company, price, slotAvailable));
                            }
                            Log.d("SearchResult", "onComplete: " + busRoutes.size());
                            if(busRoutes.size() == 0){
                                Toast.makeText(ListBusesActivity.this, "Không có chuyến xe nào", Toast.LENGTH_SHORT).show();
                            }
                            listViewBusRoute.setAdapter(adapter);
                            loadingBar.dismissbar();
                        }
                    }
                });

        listViewBusRoute.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BusRoute busRoute = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), BusInfoActivity.class);
                intent.putExtra("busInfo", busRoute);
                startActivity(intent);
            }
        });
    }
}
