package com.example.appbooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbooking.databinding.ActivityLoginBinding;
import com.example.appbooking.ultis.LoadingBar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId;

    private static final String TAG = "MAIN_TAG";

    private FirebaseAuth firebaseAuth;

    private final LoadingBar loadingBar = new LoadingBar(LoginActivity.this);

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.phoneLayout.setVisibility(View.VISIBLE);
        binding.otpLayout.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                loadingBar.dismissbar();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);

                Log.d(TAG, "onCodeSent: " +verificationId);

                mVerificationId = verificationId;
                forceResendingToken = token;
                loadingBar.dismissbar();

                binding.phoneLayout.setVisibility(View.GONE);
                binding.otpLayout.setVisibility(View.VISIBLE);

                Toast.makeText(LoginActivity.this, "Gửi mã OTP thành công", Toast.LENGTH_SHORT).show();
            }
        };

        binding.phoneContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneEdt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else {
                    startPhoneNumberVerification(phone);
                }

            }
        });

        binding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.phoneEdt.getText().toString().trim();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(LoginActivity    .this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else {
                    resendVerificationCode(phone, forceResendingToken);
                }

            }
        });

        binding.otpContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.otpEdt.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
                }else {
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        loadingBar.showDialog();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    loadingBar.dismissbar();
                    String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                    Toast.makeText(LoginActivity.this, "Đăng nhập bằng SĐT: "+phone, Toast.LENGTH_SHORT).show();
                    finish();

                    startActivity(new Intent(LoginActivity.this,    ProfileActivity.class));
                })
                .addOnFailureListener(e -> {
                    loadingBar.dismissbar();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                });
    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        loadingBar.showDialog();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startPhoneNumberVerification(String phone) {
        loadingBar.showDialog();

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(phone)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
