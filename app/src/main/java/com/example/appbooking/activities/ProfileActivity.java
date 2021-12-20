package com.example.appbooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.R;
import com.example.appbooking.databinding.ActivityLoginProfileBinding;
import com.example.appbooking.models.User;
import com.example.appbooking.ultis.LoadingBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity{

    FirebaseAuth firebaseAuth;

    private ActivityLoginProfileBinding binding;

    CollectionReference userRef;

    DocumentReference docRef;

    private final LoadingBar loadingBar = new LoadingBar(ProfileActivity.this);

    final boolean[] isExist = {false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityLoginProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        String phoneNumber = firebaseUser.getPhoneNumber();

        userRef = FirebaseFirestore.getInstance().collection("User");

        checkUserStatus();

        docRef = FirebaseFirestore.getInstance().collection("User").document(phoneNumber);

        loadingBar.showDialog();

        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Log.d("getdata", ""+document.getData());
                                String getName = document.getString("name");
                                String getAddress = document.getString("address");
                                binding.nameEdt.setText(getName);
                                binding.addressEdt.setText(getAddress);
                                isExist[0] = true;
                            } else {
                                Log.d("getdata", "Không tồn tại");
                            }
                            checkExist();
                            loadingBar.dismissbar();
                        } else {
                            Log.d("getdata", "Thất bại");
                        }
                    }
                });

        binding.logoutBtn.setOnClickListener(view ->{
            firebaseAuth.signOut();
            finish();
        });

        binding.backBtn.setOnClickListener(view ->{
            finish();
        });

        binding.bookedTicketCv.setOnClickListener(view ->{
            Intent intent = new Intent(getApplicationContext(), ConfirmBookingActivity.class);
            startActivity(intent);
        });

        binding.updateBtn.setOnClickListener(view ->{
            String name = binding.nameEdt.getText().toString();
            String address = binding.addressEdt.getText().toString();
            loadingBar.showDialog();

            if(TextUtils.isEmpty(name) || TextUtils.isEmpty(address)){
                Toast.makeText(ProfileActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                loadingBar.dismissbar();
            }else if(isExist[0]){
                docRef.update("name", name, "address", address).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            loadingBar.dismissbar();
                            finish();
                        }else {
                            Log.d("updatedata", "Có lỗi xảy ra");
                        }
                    }
                });
            }else{
                User user = new User(name, address, phoneNumber);
                userRef.document(phoneNumber)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                loadingBar.dismissbar();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!isExist[0]){
            Toast.makeText(ProfileActivity.this , "Vui lòng cập nhật thông tin cá nhân", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }

    private void checkUserStatus(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userRef = FirebaseFirestore.getInstance().collection("User");
        if(firebaseUser != null){
            String phone = firebaseUser.getPhoneNumber();
            binding.phoneEdt.setText(phone);
        }else {
            finish();
        }
    }

    private void checkExist(){
        if(!isExist[0]){
            binding.backBtn.setVisibility(View.GONE);
            binding.logoutBtn.setVisibility(View.GONE);
        } else {
            binding.backBtn.setVisibility(View.VISIBLE);
            binding.logoutBtn.setVisibility(View.VISIBLE);
        }
    }

    }

