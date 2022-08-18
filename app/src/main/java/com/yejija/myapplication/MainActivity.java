package com.yejija.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.Signature;





public class MainActivity extends AppCompatActivity {
    private Button button1;
    private TextView txtResult;
    String currentAddress;

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



//        getAppKeyHash();
        ManagePublicData.getInstance().parseSeniorCenter.execute();

        txtResult = (TextView)findViewById(R.id.txtResult);
        txtResult.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivityMap.class);
            startActivity(intent);
        });

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
            startActivity(intent);
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
            startActivity(intent);
        });

        // 일자리 사이트 크롤링
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SubActivityJobs.class);
            startActivity(intent);
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

}
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button button = (Button) findViewById(R.id.button1);
//        button.setOnClickListener(v -> {
//            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
//            startActivity(intent);
//        });
//
//        Button button2 = (Button) findViewById(R.id.button2);
//        button2.setOnClickListener(v -> {
//            Intent intent = new Intent(getApplicationContext(), SubActivity.class);
//            startActivity(intent);
//        });
//    }
//}