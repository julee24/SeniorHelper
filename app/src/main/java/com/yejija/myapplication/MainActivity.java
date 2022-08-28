package com.yejija.myapplication;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.Signature;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {
    private TextView txtResult;
    String currentAddress;

    //
    Button btn_tel;
    String tel_number;
    //

    public static ArrayList<SeniorCenterVO> SeniorCenterLoc = new ArrayList<>();

//
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

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // start에 지정된 Drawer 열기
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

//        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
//        NavigationUI.setupWithNavController(navigationView, navController);


        final TextView textTitle = findViewById(R.id.textTitle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch(menuItem.getItemId()){
                    case R.id.nav_home:
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_edit:
                        Intent intent2 = new Intent(getApplicationContext(), Setting.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_profile:
                        Intent intent3 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent3);
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_settings:
//                        menuItem.setChecked(true);
                        Intent intent4 = new Intent(getApplicationContext(), Save_tel.class);
                        startActivity(intent4);
                        drawerLayout.closeDrawers();
                        return true;
                }

                return false;

            }

        });

//        getAppKeyHash();
        ManagePublicData.getInstance().parseSeniorCenter.execute();

        txtResult = (TextView)findViewById(R.id.txtResult);
//        txtResult.setOnClickListener(v -> {
//            Intent intent = new Intent(getApplicationContext(), SubActivityMap.class);
//            startActivity(intent);
//        });

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
            startActivity(intent);
        });

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
        Intent intent1 = getIntent();
        tel_number = intent1.getStringExtra("number");

        btn_tel = findViewById(R.id.button5);

        btn_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("tel: ", ""+((Save_tel) MainActivity.this.getApplication()).getSomeVariable());
                if (((Save_tel) MainActivity.this.getApplication()).getSomeVariable() == null) {
                    Toast.makeText(getApplicationContext(),"저장된 번호가 없습니다. 번호를 저장해주세요",Toast.LENGTH_LONG).show();
                    Intent intent2 = new Intent(getApplicationContext(), com.yejija.myapplication.Setting.class);
                    startActivity(intent2);
                } else {
                    Intent intent3 = new Intent("android.intent.action.DIAL", Uri.parse(((Save_tel) MainActivity.this.getApplication()).getSomeVariable()));
                    //여기 전역변수로 바꾸기
                    //startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel_number)));
                    startActivity(intent3);
                }


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
                }

            }
            //

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onBackPressed() {
        // 이 코드에서는 메소드를 오버라이드해서 드로어가 열려있다면 닫아주고,
        // 아니라면 super.onBackPressed를 호출해 원래 onBackPressed()의 기능을 하도록 한 것이다.
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // DrawerLayout 닫기
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}