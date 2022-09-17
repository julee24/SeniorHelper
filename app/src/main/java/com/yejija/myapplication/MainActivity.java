package com.yejija.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.daum.mf.map.api.MapReverseGeoCoder;

import java.util.Calendar;
import java.util.Locale;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;



public class MainActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private TextView txtResult;
    Button btn_tel;
    String tel_number;
    private DatabaseReference mDatabase;
    User user;
    public static Context context_main;
    SharedPreferences pref2;
    SharedPreferences.Editor editor2;
    String post;
    private String userName, year, month, day, num;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1000;

    String address;

    public List<String> dayMorning = new ArrayList<>();
    public List<String> dayAfternoon = new ArrayList<>();
    public List<String> dayDinner = new ArrayList<>();
    public List<String> dayNight = new ArrayList<>();

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private MapView mMapView;
    double currentLocLat;
    double currentLocLon;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    public static ArrayList<SeniorCenterVO> SeniorCenterLoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        SharedPreferences sharedPreferences = getSharedPreferences("checkFirstAccess", Activity.MODE_PRIVATE);
        boolean checkFirstAccess = sharedPreferences.getBoolean("checkFirstAccess", false);
        context_main = this;
        pref2 = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor2 = pref2.edit();
        post = pref2.getString("post", "hello");

        if (!checkFirstAccess) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("checkFirstAccess", true);
            editor.apply();

            Intent tutorialIntent = new Intent(MainActivity.this, TutorialActivity.class);
            startActivity(tutorialIntent);
            writeNewUser("홍길동", "010", "1234", "5678", "2099", "12", "30", null);
        } else {
            Intent intent7 = new Intent(getApplicationContext(), SubActivity.class);
            startActivity(intent7);
        }

        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.setCurrentLocationEventListener(this);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        } else {

            checkRunTimePermission();
        }

        //하루 응원 멘트
        dayMorning.add("좋은 아침입니다");
        dayMorning.add("오늘 하루 행복하세요!");
        dayMorning.add("오늘도 웃으면서 하루를 시작해봐요!");

        dayAfternoon.add("즐거운 오후 보내시길 바랍니다");
        dayAfternoon.add("오늘 오후도 활기차게 파이팅!");
        dayAfternoon.add("점심은 꼭 잘 챙겨드세요!");

        dayDinner.add("편히 쉬시고 내일도 파이팅입니다");
        dayDinner.add("편안한 저녁 보내시고 내일 뵙겠습니다");
        dayDinner.add("즐거운 저녁 시간 되세요");

        dayNight.add("안녕히 주무세요");
        dayNight.add("좋은 꿈 꾸세요!");
        dayNight.add("편안한 밤 되시고 좋은 꿈 꾸세요!");


        //set profileImage by time 프로필 사진 & 시간별 효과
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        TextView quotes = (TextView) findViewById(R.id.textView2);

        if (timeOfDay >= 0 && timeOfDay < 4) {
            quotes.setText(dayNight.get((int) (Math.random() * 3)));
        } else if (timeOfDay >= 4 && timeOfDay < 11) {
            quotes.setText(dayMorning.get((int) (Math.random() * 3)));
        } else if (timeOfDay >= 11 && timeOfDay < 15) {
            quotes.setText(dayAfternoon.get((int) (Math.random() * 3)));
        } else if (timeOfDay >= 15 && timeOfDay < 22) {
            quotes.setText(dayDinner.get((int) (Math.random() * 3)));
        } else if (timeOfDay >= 22 && timeOfDay < 24) {
            quotes.setText(dayNight.get((int) (Math.random() * 3)));
        }


        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(view -> {

            UserProfile(post);
            drawerLayout.openDrawer(GravityCompat.START);
        });


        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);


        navigationView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
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

                case R.id.nav_tuto:
                    Intent intent4 = new Intent(getApplicationContext(), Info.class);
                    startActivity(intent4);
                    drawerLayout.closeDrawers();
                    return true;
            }

            return false;

        });

        txtResult = (TextView) findViewById(R.id.txtResult);

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivityMap.class);
            ConstraintLayout frame = (ConstraintLayout) findViewById(R.id.mainLayout);
            frame.removeView(mMapView);
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


        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, GuList.class);
            myIntent.putExtra("address", address);
            startActivity(myIntent);
        });

        btn_tel = findViewById(R.id.button5);

        btn_tel.setOnClickListener(view -> readNum(post));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();

        currentLocLat = mapPointGeo.latitude;
        currentLocLon = mapPointGeo.longitude;


        //내 위치 주소 찾기
        MapReverseGeoCoder reverseGeoCoder = new MapReverseGeoCoder("7947271957296b6017458678a8697885", currentLocation, MainActivity.this, MainActivity.this);

        reverseGeoCoder.startFindingAddress();
    }


    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {
        String str = result;

        List<String> list = Arrays.asList(str.split(" "));
        address=list.get(1);

        txtResult.setText(address);

    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            boolean check_result = true;

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

                mMapView.setShowCurrentLocationMarker(false);

            }
            else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    try {
                        Intent intent7 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:"+getPackageName()));
                        Log.v("packagename", getPackageName());
                        intent7.addCategory(Intent.CATEGORY_DEFAULT);
                        intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent7);
                    } catch(ActivityNotFoundException e){
                        e.printStackTrace();
                        Intent intent7 = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        intent7.addCategory(Intent.CATEGORY_DEFAULT);
                        intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent7);
                    }
                    finish();

                }else {

                    Toast.makeText(MainActivity.this, "권한이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                    try {
                        Intent intent7 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:"+getPackageName()));
                        intent7.addCategory(Intent.CATEGORY_DEFAULT);
                        intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent7);

                    } catch(ActivityNotFoundException e){
                        e.printStackTrace();
                        Intent intent7 = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        intent7.addCategory(Intent.CATEGORY_DEFAULT);
                        intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent7);
                    }

                }
            }

        }
    }

    void checkRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);


        } else {


            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {

                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", (dialog, id) -> {
            Intent callGPSSettingIntent
                    = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
        });
        builder.setNegativeButton("취소", (dialog, id) -> dialog.cancel());
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }




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
                .addOnSuccessListener(aVoid -> {

                    post = pushedPostRef.getKey();
                    editor2.putString("post", post);
                    editor2.apply();

                })
                .addOnFailureListener(e -> {
                    // Write failed
                    Toast.makeText(getApplicationContext(), "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
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

                View nav_header_view = navigationView.getHeaderView(0);

                //year = year.substring(2);
                month = String.format(Locale.KOREA,"%02d", Integer.valueOf(month));
                day = String.format(Locale.KOREA,"%02d", Integer.valueOf(day));

                TextView profile_name = (TextView) nav_header_view.findViewById(R.id.profile_name);
                TextView profile_birth = (TextView) nav_header_view.findViewById(R.id.profile_birth);
                TextView profile_month = (TextView) nav_header_view.findViewById(R.id.profile_month);
                TextView profile_day = (TextView) nav_header_view.findViewById(R.id.profile_day);
                profile_name.setText(userName);
                profile_birth.setText(year);
                profile_month.setText(month);
                profile_day.setText(day);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void readNum(String post) {

        mDatabase.child("users").child(post).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
            } else {

                User user = task.getResult().getValue(User.class);
                num = user.getnum();

                button_pressed();
            }
        });
    }

    public void button_pressed(){
        if (num == null) {
            Toast.makeText(getApplicationContext(),"저장된 번호가 없습니다. 번호를 저장해주세요",Toast.LENGTH_LONG).show();
            Intent intent12 = new Intent(getApplicationContext(), com.yejija.myapplication.Setting.class);
            startActivity(intent12);
        } else {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (permissionCheck!= PackageManager.PERMISSION_GRANTED)
            {

                Toast.makeText(MainActivity.this, "권한이 거부되었습니다. 설정(앱 정보)에서 권한을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                try {
                    Intent intent7 = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            .setData(Uri.parse("package:"+getPackageName()));
                    intent7.addCategory(Intent.CATEGORY_DEFAULT);
                    intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent7);
                } catch(ActivityNotFoundException e){
                    e.printStackTrace();
                    Intent intent7 = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    intent7.addCategory(Intent.CATEGORY_DEFAULT);
                    intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent7);
                }
            }
            else {
                Intent intent3 = new Intent("android.intent.action.CALL", Uri.parse("tel:" + num));
                startActivity(intent3);
            }
            }

        }

    private void updateNum(String post){

        mDatabase.child("users").child(post).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
            }
            else {

                User user = task.getResult().getValue(User.class);
                num = user.getnum();
                if (num == null) {
                    Toast.makeText(getApplicationContext(), "번호를 저장해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent2 = new Intent(getApplicationContext(), ReSetting.class);
                    startActivity(intent2);
                }
            }
        });
    }
}