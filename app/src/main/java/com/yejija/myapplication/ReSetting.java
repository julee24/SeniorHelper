package com.yejija.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yejija.myapplication.MainActivity;
import com.yejija.myapplication.R;
import com.yejija.myapplication.User;

public class ReSetting extends AppCompatActivity{
    private DatabaseReference mDatabase;
    private Button btnsave;
    EditText edit_tel;
    String post, num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetting);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String post = ((MainActivity)MainActivity.context_main).post;

        edit_tel = findViewById(R.id.edit_tel2);

        read_num(post);
        btnsave = findViewById(R.id.btn_save2);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_num(post);
                finish();
            }
        });
    }

    private void read_num(String post){
        mDatabase.child("users").child(post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.v("유저프린트", ""+user);

                num = user.getnum();

                edit_tel.setText(num);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void update_num(String post){
        mDatabase.child("users").child(post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                num = edit_tel.getText().toString();

                mDatabase.child("users").child(post).child("num").setValue(num);
                Toast.makeText(getApplicationContext(), "번호가 수정되었습니다.", Toast.LENGTH_SHORT).show();

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

