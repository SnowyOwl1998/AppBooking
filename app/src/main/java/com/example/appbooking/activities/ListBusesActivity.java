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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbooking.R;
import com.example.appbooking.adapters.ListBusesAdapter;
import com.example.appbooking.databinding.ActivityListBusesBinding;
import com.example.appbooking.models.BusRoute;
import com.example.appbooking.ultis.LoadingBar;
import com.example.appbooking.ultis.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                        if(fromLocation1.equals(fromLocation) && toLocation1.equals(toLocation) && bookingDate.equals(date)){
                            busRoutes.add(new BusRoute(id,
                                    fromLocation1,
                                    toLocation1,
                                    bookingDate,
                                    timeTravel,
                                    type,
                                    company,
                                    price,
                                    slot));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("SearchResult", "onComplete: " + busRoutes.size());
                if(busRoutes.size() == 0){
                    Toast.makeText(ListBusesActivity.this, "Không có chuyến xe nào", Toast.LENGTH_SHORT).show();
                }
                listViewBusRoute.setAdapter(adapter);
                loadingBar.dismissbar();
            }
        }, error -> {
            Toast.makeText(ListBusesActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            loadingBar.dismissbar();
        });
        jsonArrayRequest.setShouldCache(false);
        requestQueue.add(jsonArrayRequest);

//        FirebaseFirestore.getInstance().collection("BusRoute")
//                .whereEqualTo("from", fromLocation)
//                .whereEqualTo("to", toLocation)
//                .whereEqualTo("date", date)
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
//                                Integer slotAvailable = Integer.parseInt(queryDocumentSnapshot.get("slotAvailable").toString());
//                                busRoutes.add(new BusRoute(from, to, date, time, type, company, price, slotAvailable));
//                            }
//                            Log.d("SearchResult", "onComplete: " + busRoutes.size());
//                            if(busRoutes.size() == 0){
//                                Toast.makeText(ListBusesActivity.this, "Không có chuyến xe nào", Toast.LENGTH_SHORT).show();
//                            }
//                            listViewBusRoute.setAdapter(adapter);
//                            loadingBar.dismissbar();
//                        }
//                    }
//                });

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
