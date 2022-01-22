package com.example.appbooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appbooking.databinding.ActivityRegisterBinding;
import com.example.appbooking.ultis.LoadingBar;
import com.example.appbooking.ultis.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_TAG";

    private final LoadingBar loadingBar = new LoadingBar(RegisterActivity.this);

    private ActivityRegisterBinding binding;

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";
    public static final String KEY_ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(v -> finish());

        binding.signUpBtn.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        binding.signUpBtn.setOnClickListener(v -> {
            String username = binding.phoneEdt.getText().toString().trim().toLowerCase(Locale.ROOT);
            String password = binding.passEdt.getText().toString().trim();
            String name = binding.nameEdt.getText().toString().trim();
            String address = binding.addressEdt.getText().toString().trim();
            if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password) && TextUtils.isEmpty(name) && TextUtils.isEmpty(address)){
                Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
            else {
                registerAccount(username, password, name, address);
                loadingBar.showDialog();
            }
        });
    }

    private void registerAccount(final String username,final String password,final String name,final String address){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, Server.urlRegister, response -> {
            String message = "";
            try{
                JSONObject jsonObject = new JSONObject(response);
                Log.d(TAG, "onResponse: " + response);
                if(jsonObject.getInt("command") == 1){
                    message = jsonObject.getString("message");
                    Log.d("RegisterPhone", "onResponse: " + message);
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if(jsonObject.getInt("command") == 0) {
                    message = jsonObject.getString("message");
                    Log.d("RegisterPhone", "onResponse: " + message);
                } else {
                    message = jsonObject.getString("message");
                    Log.d("RegisterPhone", "onResponse: " + message);
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
                params.put(KEY_USERNAME, username);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_NAME, name);
                params.put(KEY_ADDRESS, address);
                return params;
            }
        };
        request.setShouldCache(false);
        requestQueue.add(request);
    }
}
