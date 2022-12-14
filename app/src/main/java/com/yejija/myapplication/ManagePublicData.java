package com.yejija.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import kr.hyosang.coordinate.*;

public class ManagePublicData
{
    private static ManagePublicData managePublicData;

    public double lati;
    public double longi;

    SeniorCenterVO SeniorCenterVO;

    private final ArrayList<SeniorCenterVO> SeniorCenterVOArrayList;

    public ParseSeniorCenter parseSeniorCenter;

    public int count = 0;

    String tag_name = null;
    boolean bSet = false;

    public static ManagePublicData getInstance() {
        if (managePublicData == null) {
            managePublicData = new ManagePublicData();
        }
        return managePublicData;
    }

    private ManagePublicData() {
        SeniorCenterVO = new SeniorCenterVO();
        SeniorCenterVOArrayList = new ArrayList<>(120);
        parseSeniorCenter = new ParseSeniorCenter();
    }

    public ArrayList<SeniorCenterVO> getSeniorCenterVOArrayList() {
        return SeniorCenterVOArrayList;
    }

    public class ParseSeniorCenter extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            for (int i = 1; i < 4000; i += 50) { //i가 왜 5000까지지...
                try {
                    String url = "http://openapi.seoul.go.kr:8088/79527649756a756c3130326d4a517543/xml/OdsnBuildingInfo/" + i + "/" + (i + 49) + "/";
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    URL xmlUrl = new URL(url);
                    xmlUrl.openConnection().getInputStream();
                    parser.setInput(xmlUrl.openStream(), "utf-8");
                    int eventType = parser.getEventType();

                    while (eventType != XmlPullParser.END_DOCUMENT) {

                        if (eventType == XmlPullParser.START_TAG) {
                            tag_name = parser.getName();
                            if (tag_name.equals("NM") | tag_name.equals("ADDR_OLD") | tag_name.equals("XCODE") | tag_name.equals("YCODE"))
                                bSet = true;
                        } else if (eventType == XmlPullParser.TEXT) {
                            if (bSet) {
                                String data = parser.getText();
                                switch (tag_name) {
                                    case "NM":
                                        SeniorCenterVO.setCenterName(data);
                                        break;
                                    case "ADDR_OLD":
                                        SeniorCenterVO.setCenterAddress(data);
                                        break;
                                    case "XCODE":
                                        if (data!=null) {
                                            SeniorCenterVO.setCenterX(data);
                                        }
                                        else SeniorCenterVO.setCenterX(null);
                                        break;
                                    case "YCODE":
                                        if (data!=null) {
                                            SeniorCenterVO.setCenterY(data);
                                        }
                                        else SeniorCenterVO.setCenterY(null);

                                        CoordPoint pt = new CoordPoint(SeniorCenterVO.getCenterX(), SeniorCenterVO.getCenterY()); //서울시청
                                        CoordPoint ktmPt = TransCoord.getTransCoord(pt, TransCoord.COORD_TYPE_TM, TransCoord.COORD_TYPE_WGS84);
                                        lati = ktmPt.y;
                                        longi = ktmPt.x;
                                        SeniorCenterVOArrayList.add(new SeniorCenterVO(SeniorCenterVO.getCenterName(), SeniorCenterVO.getCenterAddress(), lati, longi));
                                        Log.v("test", count + " " + lati+ " " + longi + " 경로당 : " + SeniorCenterVOArrayList.get(SeniorCenterVOArrayList.size() - 1).getCenterName());
                                        count ++;
                                        break;
                                }
                                bSet = false;
                            }
                        } else if (eventType == XmlPullParser.END_TAG) {
                        }
                        eventType = parser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            for (int i = 0; i < managePublicData.getSeniorCenterVOArrayList().size(); i++) {
                MainActivity.SeniorCenterLoc.add(getSeniorCenterVOArrayList().get(i));
            }

            SubActivity.fa.finish();
        }
    }
}
