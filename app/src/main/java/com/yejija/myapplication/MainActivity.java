package com.yejija.myapplication;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;


import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.Signature;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    private TextView txtResult;
    String currentAddress;
    Button btn_tel;
    String tel_number;
    private DatabaseReference mDatabase;
    User user;
    public static Context context_main;
    SharedPreferences pref2;
    SharedPreferences.Editor editor2;
    String post;
    private String userName, year, month, day, num;

    public static ArrayList<SeniorCenterVO> SeniorCenterLoc = new ArrayList<>();

//    private void getAppKeyHash() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md;
//                md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                String something = new String(Base64.encode(md.digest(), 0));
//                Log.e("Hash key", something);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            Log.e("name not found", e.toString());
//        }
//    }
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        // 최초 실행 여부를 판단 ->>>
        SharedPreferences sharedPreferences = getSharedPreferences("checkFirstAccess", Activity.MODE_PRIVATE);
        boolean checkFirstAccess = sharedPreferences.getBoolean("checkFirstAccess", false);
        context_main = this;
        pref2 = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor2 = pref2.edit();
        post = pref2.getString("post", "hello");
        Log.v("초기화",post);

        if (!checkFirstAccess) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("checkFirstAccess", true);
            editor.apply();

            Intent tutorialIntent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(tutorialIntent);
            writeNewUser("홍길동" , "010","1234", "5678", "2099", "12", "30", null);
        }

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserProfile(post);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        final TextView textTitle = findViewById(R.id.textTitle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_edit:
                        updateNum(post);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_profile:
                        Intent intent3 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent3);
                        drawerLayout.closeDrawers();
                        return true;
                }

                return false;

            }

        });


        // 로딩 스크린 구현 activity
        // activity 안에 ManagePublicData.getInstance().parseSeniorCenter.execute();가 포함되어 있으므로 합칠때는 밑에 execute 줄 제거
        // 아 하고 ManagePublicData안에 마지막 OnPostExecute에서는 .finish() 주석 풀기!
        // Intent intent7 = new Intent(getApplicationContext(), SubActivity.class);
        // startActivity(intent7);
        ManagePublicData.getInstance().parseSeniorCenter.execute();
        //async


        txtResult = (TextView)findViewById(R.id.txtResult);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivityMap.class);
            startActivity(intent);
        });

        // 일자리 사이트 크롤링
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivityJobs.class);
            startActivity(intent);
        });

        //일기쓰기
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivityDiary.class);
            startActivity(intent);
        });

        //전화 기능
        /*
        Intent intent1 = getIntent();
        tel_number = intent1.getStringExtra("number");
         */

        btn_tel = findViewById(R.id.button5);

        btn_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                readNum(post);

            }

        });

        // 위치 관리자 객체 참조하기
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Geocoder geocoder = new Geocoder(this);


//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions( MainActivity.this, new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
            }
            else{
                // 가장최근 위치정보 가져오기
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                List<Address> list = null;
                if(location != null)

                // 위치정보를 원하는 시간, 거리마다 갱신해준다.
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000,
                        1,
                        gpsLocationListener);
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        1000,
                        1,
                        gpsLocationListener);
            }
        }
    //        });
//    }
    final Geocoder geocoder = new Geocoder(this);
    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도
            List<Address> list = null;
            try {

                list = geocoder.getFromLocation(
                        latitude, // 위도
                        longitude, // 경도
                        10); // 얻어올 값의 개수
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("test", "입출력 오류");
            }
//            if (list != null) {
//                if (list.size() == 0) {
//                    txtResult.setText("해당되는 주소 정보는 없습니다");
//                } else {
//                    txtResult.setText(list.get(0).getAddressLine(0));
//                }
//            } // 원래 코드

            
            //구 만 뽑아내는거
            if (list != null) {
                if (list.size() == 0) {
                    txtResult.setText("해당되는 주소 정보는 없습니다");
                }
                else {
                    currentAddress = null;

                    Address address = list.get(0);

                    if (address.getLocality() != null){
                        currentAddress = address.getLocality();
                    }
                    if (address.getSubLocality() != null){
                        if (currentAddress != null){
                            currentAddress += " " + address.getSubLocality();
                        }
                        else {
                            currentAddress = address.getSubLocality();
                        }
                    }
                    txtResult.setText(currentAddress);

                    Button button = (Button) findViewById(R.id.button1);
                    button.setOnClickListener(v -> {
                        Intent myIntent = new Intent(MainActivity.this, GuList.class);
                        myIntent.putExtra("address", currentAddress);
                        startActivity(myIntent);
                    });
                }

            }
            //

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };


    //번호 저장
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // DrawerLayout 닫기
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        saveState();
    }

    protected void onStart(){
        super.onStart();
        restoreState();
    }

    protected void saveState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("tel_num", tel_number);

        editor.commit();
    }

    protected void restoreState(){
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if((pref != null) && (pref.contains("tel_num"))){
            tel_number = pref.getString("tel_num", "");
        }
    }

    protected void clearPref() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        tel_number = null;
        editor.commit();
    }

    public void writeNewUser(String userName, String num1, String num2, String num3, String year, String month, String day, String num) {
        user = new User(userName, num1, num2, num3, year, month, day, num);

        DatabaseReference pushedPostRef = mDatabase.child("users").push();

        pushedPostRef.setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Write was successful!
                        post = pushedPostRef.getKey();
                        editor2.putString("post", post);
                        editor2.apply();
                        Toast.makeText(getApplicationContext(), "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        Toast.makeText(getApplicationContext(), "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void UserProfile(String post){
        mDatabase.child("users").child(post).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                userName = user.getuserName();
                year = user.getyear();
                month = user.getmonth();
                day = user.getday();

                NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
//                navigationView.OnNavigationItemSelectedListener(this);
                //View nav_header_view = navigationView.inflateHeaderView(R.layout.nav_header_main);
                View nav_header_view = navigationView.getHeaderView(0);
                TextView profile_name = (TextView) nav_header_view.findViewById(R.id.profile_name);
                TextView profile_birth = (TextView) nav_header_view.findViewById(R.id.profile_birth);
                TextView profile_month = (TextView) nav_header_view.findViewById(R.id.profile_month);
                TextView profile_day = (TextView) nav_header_view.findViewById(R.id.profile_day);
                // 위치바꿈
                profile_name.setText(userName);
                profile_birth.setText(year);
                profile_month.setText(month);
                profile_day.setText(day);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

    private void readNum(String post) {

        mDatabase.child("users").child(post).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {

                    User user = task.getResult().getValue(User.class);
                    num = user.getnum();

                    button_pressed();
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

    public void button_pressed(){
        Log.v("넘이멀까: ", "헤에에잉"+num);
        if (num == null) {
            Toast.makeText(getApplicationContext(),"저장된 번호가 없습니다. 번호를 저장해주세요",Toast.LENGTH_LONG).show();
            Intent intent12 = new Intent(getApplicationContext(), com.yejija.myapplication.Setting.class);
            startActivity(intent12);
        } else {

            Intent intent3 = new Intent("android.intent.action.DIAL", Uri.parse("tel:"+num));
            //여기 전역변수로 바꾸기
            //startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel_number)));
            startActivity(intent3);

        }

    }

    private void updateNum(String post){

        mDatabase.child("users").child(post).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase2", "Error getting data", task.getException());
                }
                else {

                    User user = task.getResult().getValue(User.class);
                    num = user.getnum();
                    //Log.v("멀까요", ""+num);
                    if (num == null) {
                        Toast.makeText(getApplicationContext(), "번호를 저장해주세요.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent2 = new Intent(getApplicationContext(), ReSetting.class);
                        startActivity(intent2);
                    }
                }
            }
        });
    }
}