package com.example.appbooking.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbooking.databinding.ActivityProfileBinding;
import com.example.appbooking.models.BusRoute;
import com.example.appbooking.models.User;
import com.example.appbooking.ultis.LoadingBar;
import com.example.appbooking.ultis.Server;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity{

    private ActivityProfileBinding binding;

    private final LoadingBar loadingBar = new LoadingBar(ProfileActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = this.getSharedPreferences("loginStatus", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String username = sharedPreferences.getString("username", null);
        Boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        binding.logoutBtn.setOnClickListener(view ->{
            editor.putString("username", null);
            editor.putBoolean("isLogin", false);
            editor.apply();
            finish();
        });

        binding.backBtn.setOnClickListener(view ->{
            finish();
        });

        binding.bookedTicketCv.setOnClickListener(view ->{
            Intent intent1 = new Intent(getApplicationContext(), ConfirmBookingActivity.class);
            startActivity(intent1);
        });

        loadingBar.showDialog();

        if(!username.equals(null)){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.urlGetUserData, response -> {
                if (response != null){
                    String message = "";
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        User user = new User();
                        user.setUsername(jsonObject.getString("username"));
                        user.setName(jsonObject.getString("name"));
                        user.setAddress(jsonObject.getString("address"));
                        Log.d("TAG", "onCreate: " + user.getUsername() + user.getName() + user.getAddress());
                        if(TextUtils.isEmpty(user.getUsername()) || TextUtils.isEmpty(user.getName()) || TextUtils.isEmpty(user.getAddress())){
                            binding.usernameEdt.setText(user.getUsername());
                            binding.nameEdt.setText(user.getName());
                            binding.addressEdt.setText(user.getAddress());
                        }
                        message = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                loadingBar.dismissbar();
            }, error -> {
                Toast.makeText(this, "Có lỗi xảy ra (0)", Toast.LENGTH_SHORT).show();
                loadingBar.dismissbar();
            }){
                @Override
                protected Map<String, String> getParams() {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("username", username);
                    return params;
                }
            };
            stringRequest.setShouldCache(false);
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            loadingBar.dismissbar();
        }

        binding.updateBtn.setOnClickListener(view ->{
            String name1 = binding.nameEdt.getText().toString();
            String address1 = binding.addressEdt.getText().toString();
            loadingBar.showDialog();

            if(TextUtils.isEmpty(name1) || TextUtils.isEmpty(address1)){
                Toast.makeText(ProfileActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                loadingBar.dismissbar();
            }else if(isLogin){
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                loadingBar.dismissbar();
            }else{
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                loadingBar.dismissbar();
            }
        });
    }

    public void changeInfo(final String name, final String address) {
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.POST, Server.urlLogin, response -> {
//            String message = "";
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                Log.d("TAG", "onResponse: " + response);
//                if(jsonObject.getInt("command") == 1){
//                    User user = new User();
//                    user.setUsername(jsonObject.getString("username"));
//
//                    message = jsonObject.getString("message");
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//                    isLogin = true;
//                    finish();
//
//                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
//                    intent.putExtra("username", user.getUsername());
//                    startActivity(intent);
//                } else {
//                    message =jsonObject.getString("message");
//                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            loadingBar.dismissbar();
//        }, error -> {
//            NetworkResponse response = error.networkResponse;
//            if(error instanceof ServerError && response != null){
//                try{
//                    String res = new String(response.data,
//                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                } catch (UnsupportedEncodingException e1){
//                    e1.printStackTrace();
//                }
//            }
//            loadingBar.dismissbar();
//        }){
//            @Override
//            protected Map<String, String> getParams() {
//                HashMap<String, String> params = new HashMap<>();
//                params.put(KEY_USERNAME, username);
//                params.put(KEY_PASSWORD, password);
//                return params;
//            }
//        };
//        request.setShouldCache(false);
//        requestQueue.add(request);
    }
}

