package com.yejija.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

public class SubActivity extends AppCompatActivity {

    public static Activity fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fa=this;

        setContentView(R.layout.progress_dialog);
        ManagePublicData.getInstance().parseSeniorCenter.execute();
    }
}