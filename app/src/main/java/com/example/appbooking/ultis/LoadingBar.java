package com.example.appbooking.ultis;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.appbooking.R;

public class LoadingBar {
    Activity activity;
    AlertDialog dialog;

    public LoadingBar(Activity thisActivity){
        activity = thisActivity;
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_layout, null));
        dialog = builder.create();
        dialog.show();
    }

    public void dismissbar(){
        dialog.dismiss();
    }
}
