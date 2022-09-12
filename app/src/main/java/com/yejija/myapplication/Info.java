package com.yejija.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.yejija.myapplication.Infocitizen;

public class Info extends AppCompatActivity{
    LinearLayout job, citizen, gu, journal, call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        job = (LinearLayout) findViewById(R.id.job);
        citizen = (LinearLayout) findViewById(R.id.citizen);
        gu = (LinearLayout) findViewById(R.id.gu);
        journal = (LinearLayout) findViewById(R.id.journal);
        call = (LinearLayout) findViewById(R.id.call);

        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InfoJob.class);
                startActivity(intent);
            }
        });

        citizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Infocitizen.class);
                startActivity(intent);
            }
        });

        gu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InfoGu.class);
                startActivity(intent);
            }
        });

    }


}
