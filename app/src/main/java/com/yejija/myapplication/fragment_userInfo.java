package com.yejija.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class fragment_userInfo extends Fragment {
    String name;
    String age;
    String date;
    EditText edtName, edtAge;

    SharedPreferences pref;
    SharedPreferences.Editor editor;



    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_userinfo, container, false);
        Button btnDoB = rootView.findViewById(R.id.btnDoB);
        Button btnSave = rootView.findViewById(R.id.btnSave);
        Calendar calendar = Calendar.getInstance();
        Date currentDate = Calendar.getInstance().getTime();
        btnDoB.setText(new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(currentDate));
        Log.e("hi", name+"?");


        DatePickerDialog.OnDateSetListener dpDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONDAY, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateDate();
            }

            private void updateDate() {
                String birth = "YYYY/MM/dd";
                @SuppressLint({"NewApi", "LocalSuppress"}) SimpleDateFormat sdf = new SimpleDateFormat(birth, Locale.KOREA);
                Button btnDoB = rootView.findViewById(R.id.btnDoB);
                btnDoB.setText(sdf.format(calendar.getTime()));
            }

        };



        btnDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, dpDialog,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        name = pref.getString("name", "");
        age = pref.getString("age", "");
        //date = pref.getString("date", "");
        edtName = rootView.findViewById(R.id.edtName);
        edtName.setText(String.valueOf(name));
        edtAge = rootView.findViewById(R.id.edtAge);
        edtAge.setText(String.valueOf(age));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = edtName.getText().toString();
                age = edtAge.getText().toString();
                date = btnDoB.getText().toString();

                editor.putString("name", name);
                editor.putString("age", age);
                editor.apply();


                if(name.replace(" ", "").equals("") || age.replace(" ", "").equals("")) {
                    Toast.makeText(getContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "이름 : " + name + "/ 나이 : " + age + " / " + date, Toast.LENGTH_SHORT).show();
                    Log.e("hello", name+"!!");
                }


            }



        });


        return rootView;
    }

    



}
