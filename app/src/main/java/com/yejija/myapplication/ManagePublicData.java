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

    public static final int COORD_TYPE_TM = 1;//아니면 이거... 와 어렵 몰라 힘들...
    public static final int COORD_TYPE_KTM = 2;
    public static final int COORD_TYPE_UTM = 3;
    public static final int COORD_TYPE_CONGNAMUL = 4;
    public static final int COORD_TYPE_WGS84 = 5;
    public static final int COORD_TYPE_BESSEL = 6;
    public static final int COORD_TYPE_WTM = 7;
    public static final int COORD_TYPE_WKTM = 8; //우리가 사용하는 공공데이터는 WKTM인가보다...
    public static final int COORD_TYPE_WCONGNAMUL = 10;

    public double lati;
    public double longi;

    SeniorCenterVO SeniorCenterVO;

    private ArrayList<SeniorCenterVO> SeniorCenterVOArrayList;

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
        SeniorCenterVOArrayList = new ArrayList<SeniorCenterVO>(120);
        parseSeniorCenter = new ParseSeniorCenter();
    }

    public ArrayList<SeniorCenterVO> getSeniorCenterVOArrayList() {
        return SeniorCenterVOArrayList;
    }

    public void setSeniorCenterVOArrayList(ArrayList<SeniorCenterVO> SeniorCenterVOArrayList) {
        this.SeniorCenterVOArrayList = SeniorCenterVOArrayList;
    }

    public class ParseSeniorCenter extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            for (int i = 1; i < 4000; i += 50) { //i가 왜 5000까지지...
                try {
                    //String url = "http://openapi.seoul.go.kr:8088/(인증키)/xml/SearchPublicToiletPOIService/" + i + "/" + (i + 999) + "/";
                    String url = "http://openapi.seoul.go.kr:8088/79527649756a756c3130326d4a517543/xml/OdsnBuildingInfo/" + i + "/" + (i + 49) + "/";
                    //(50,49)(100,99),(200,199)(1000,999)
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
                                        //SeniorCenterVOArrayList.add(new SeniorCenterVO(SeniorCenterVO.getCenterName(), SeniorCenterVO.getCenterAddress(), SeniorCenterVO.getCenterX(), SeniorCenterVO.getCenterY()));
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
            Log.v("size", SeniorCenterVOArrayList.size() + " ");
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            for (int i = 0; i < managePublicData.getSeniorCenterVOArrayList().size(); i++) {
                //MainActivity.mainTextView.append("\n");
                //MainActivity.mainTextView.append(getSeniorCenterVOArrayList().get(i).getCenterName() + " ");
                MainActivity.SeniorCenterLoc.add(getSeniorCenterVOArrayList().get(i));
            }

            SubActivity.fa.finish();
        }
    }
}
