package com.yejija.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {
    EditText edit_tel;
    Button btn_save;
    //Save_tel tel_number;
    Context context = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        btn_save = findViewById(R.id.btn_save);
        edit_tel = findViewById(R.id.edit_tel);
        edit_tel.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Save_tel) Setting.this.getApplication()).setSomeVariable("tel:"+edit_tel.getText().toString());
                //Intent i = new Intent(Setting.this, MainActivity.class);
                //i.putExtra("number", tel_number);
                //startActivity(i);
            }
        });

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
        editor.commit();

    }

    protected void restoreState() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        ((Save_tel) Setting.this.getApplication()).setSomeVariable(pref.getString("number", ""));
        //((Save_tel) context.getApplicationContext()).tel_num = pref.getString("number", "");
    }

    protected void clearPref(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        ((Save_tel) Setting.this.getApplication()).setSomeVariable(null);
        //((Save_tel) context.getApplicationContext()).tel_num = null;
        editor.commit();
    }
}
