package com.yejija.myapplication;


import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Button btnsave;
    private String userName, num1, num2, num3, year, month, day, num;
    private String name, n1, n2, n3, b1, b2, b3;
    EditText et_user_name,et_user_number, editTextNumber, editTextNumber2, editTextNumber3, editTextNumber4, editTextNumber5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String post = ((MainActivity)MainActivity.context_main).post;

        et_user_name = findViewById(R.id.edtName);
        et_user_number = findViewById(R.id.edtAge);
        editTextNumber = findViewById(R.id.edtAge2);
        editTextNumber2 = findViewById(R.id.edtAge3);
        editTextNumber3 = findViewById(R.id.edtbirth);
        editTextNumber4 = findViewById(R.id.edtbirth2);
        editTextNumber5 = findViewById(R.id.edtbirth3);

//

        readUser(post);

        btnsave = findViewById(R.id.btnSave);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(post);
                finish();
            }
        });
    }

    private void readUser(String post){
        mDatabase.child("users").child(post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.v("유저프린트", ""+user);

                userName = user.getuserName();

                num1 = user.getNum1();
                num2 = user.getNum2();
                num3 = user.getNum3();
                year = user.getyear();
                month = user.getmonth();
                day = user.getday();

                //텍스트뷰에 받아온 문자열 대입하기
                et_user_name.setText(userName);
                et_user_number.setText(num1);
                editTextNumber.setText(num2);
                editTextNumber2.setText(num3);
                editTextNumber3.setText(year);
                editTextNumber4.setText(month);
                editTextNumber5.setText(day);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateUser(String post){
        mDatabase.child("users").child(post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User ser = dataSnapshot.getValue(User.class);

                name = et_user_name.getText().toString();
                n1 = et_user_number.getText().toString();
                n2 = editTextNumber.getText().toString();
                n3 = editTextNumber2.getText().toString();
                b1 = editTextNumber3.getText().toString();
                b2 = editTextNumber4.getText().toString();
                b3 = editTextNumber5.getText().toString();
                num = ser.getnum();
                Log.v(name, n1);
                User user = new User(name, n1, n2, n3, b1, b2, b3, num);

                mDatabase.child("users").child(post).setValue(user);

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}


