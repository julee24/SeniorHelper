package com.yejija.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

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

        job.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), InfoJob.class);
            startActivity(intent);
        });

        citizen.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Infocitizen.class);
            startActivity(intent);
        });

        gu.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), InfoGu.class);
            startActivity(intent);
        });

        journal.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), InfoJourney.class);
            startActivity(intent);
        });

        call.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), InfoCall.class);
            startActivity(intent);
        });
    }


}
