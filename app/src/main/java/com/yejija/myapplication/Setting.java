package com.yejija.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Setting extends AppCompatActivity {
    EditText edit_tel;
    Button btn_save;
    private DatabaseReference mDatabase;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        String post = ((MainActivity)MainActivity.context_main).post;

        btn_save = findViewById(R.id.btn_save);
        edit_tel = findViewById(R.id.edit_tel);
        edit_tel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn_save.setOnClickListener(view -> {
            writenum(post, edit_tel.getText().toString());
            finish();
        });

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

        if (permissionCheck!= PackageManager.PERMISSION_GRANTED)
        {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveState();
    }

    @Override
    protected void onStart(){
        super.onStart();
        restoreState();
    }

    protected void saveState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("number", ((Save_tel) this.getApplication()).getSomeVariable());
        editor.apply();

    }

    protected void restoreState() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        ((Save_tel) Setting.this.getApplication()).setSomeVariable(pref.getString("number", ""));
    }

    public void writenum(String post, String num) {

        DatabaseReference pushedPostRef = mDatabase.child("users").child(post).child("num");


        pushedPostRef.setValue(num)
                .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                });

    }
}
