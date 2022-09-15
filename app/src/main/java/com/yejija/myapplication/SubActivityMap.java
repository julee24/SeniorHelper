package com.yejija.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.RelativeLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.util.*;


import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class SubActivityMap extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private static final String LOG_TAG = "SubActivityMap";

    private TextView txtname;
    private TextView txtaddress;


    private MapView mMapView;

    double currentLocLat;
    double currentLocLon;

    public int onlyonce=0;

    MapPoint [] markerpoints = new MapPoint[MainActivity.SeniorCenterLoc.size()];
    MapPOIItem [] markers = new MapPOIItem[MainActivity.SeniorCenterLoc.size()];


    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION};

    int min;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //layout 설정하는 곳
        setContentView(R.layout.activity_map2);



        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.setCurrentLocationEventListener(this);


        for (int i=0; i<MainActivity.SeniorCenterLoc.size();i++) {
            markerpoints[i] = MapPoint.mapPointWithGeoCoord(MainActivity.SeniorCenterLoc.get(i).getCenterX(), MainActivity.SeniorCenterLoc.get(i).getCenterY());
            if (markerpoints[i] != null) {

                markers[i] = new MapPOIItem();
                markers[i].setItemName(MainActivity.SeniorCenterLoc.get(i).getCenterName());
                markers[i].setTag(i);
                markers[i].setMapPoint(markerpoints[i]);
                markers[i].setMarkerType(MapPOIItem.MarkerType.CustomImage);
                markers[i].setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
                markers[i].setCustomImageResourceId(R.drawable.housepin1);
                mMapView.addPOIItem(markers[i]);
            }
            else{

            }
        }


        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

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
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));

        currentLocLat = mapPointGeo.latitude;
        currentLocLon = mapPointGeo.longitude;

        txtname = (TextView)findViewById(R.id.txtname);
        txtaddress = (TextView)findViewById(R.id.txtaddress);

        List<Float> distance = new ArrayList<Float>();
        float[] result = new float[1];

        for (int i=0; i<MainActivity.SeniorCenterLoc.size();i++) {
            android.location.Location.distanceBetween(currentLocLat,currentLocLon,MainActivity.SeniorCenterLoc.get(i).getCenterX(),MainActivity.SeniorCenterLoc.get(i).getCenterY(),result);
            distance.add(result[0]);
        }

        min = distance.indexOf(Collections.min(distance));
        mMapView.removePOIItem(markers[min]);
        markers[min].setMarkerType(MapPOIItem.MarkerType.CustomImage);
        markers[min].setCustomImageResourceId(R.drawable.housepin);
        mMapView.addPOIItem(markers[min]);
        Log.v("hello2", " " + min);
        txtname.setText(MainActivity.SeniorCenterLoc.get(min).getCenterName());
        txtaddress.setText(MainActivity.SeniorCenterLoc.get(min).getCenterAddress());

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

    }

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        //
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
                Log.d("@@@", "start");
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);

                mMapView.setShowCurrentLocationMarker(false);

            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                    Toast.makeText(SubActivityMap.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(SubActivityMap.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){
        //런타임 퍼미션 처리

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(SubActivityMap.this,
                Manifest.permission.ACCESS_FINE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ) {

            mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);


        } else {

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(SubActivityMap.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(SubActivityMap.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(SubActivityMap.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(SubActivityMap.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SubActivityMap.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
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

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
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

    public void getDistance(float[] results, double a, double b) {
        android.location.Location.distanceBetween(currentLocLat,currentLocLon,a,b,results);
    }
    //

    public int minDistance(float[] results){
        float a = results[0];
        int plac = 0;
        for (int i = 0; i < results.length; i++){
            if (a>results[i]){
                a = results[i];
                plac = i;
            }
        }
        return plac;
    }

}