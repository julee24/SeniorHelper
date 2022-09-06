package com.yejija.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class SubActivity extends AppCompatActivity {

    public static Activity fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fa=this;
        // 필요한지 확인
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //
        setContentView(R.layout.progress_dialog);
        ManagePublicData.getInstance().parseSeniorCenter.execute();
    }
}