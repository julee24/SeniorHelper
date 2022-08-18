package com.yejija.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SubActivityJobs extends AppCompatActivity {
    private static final String LOG_TAG = "SubActivityJobs";
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    private TextView textView8;

    //
    private String temp;
    private String a;
    private ArrayList<String> JobPlace;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //layout 설정하는 곳
        setContentView(R.layout.activity_jobs);

        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView6 = (TextView)findViewById(R.id.textView6);
        textView7 = (TextView)findViewById(R.id.textView7);
        textView8 = (TextView)findViewById(R.id.textView8);
        getWebsite();

    }

    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder_temp = new StringBuilder();
                //final StringBuilder builder_cast = new StringBuilder(); 다른 글
                try {
                    Document doc = Jsoup.connect("https://goldenjob.or.kr/job/find-person.asp").get(); //크롤링할 주소
                    Elements links_temp = doc.select("a");  //오늘의 온도
                    //Elements links_cast = doc.select("p.cast_txt"); //오늘의 날씨
                    builder_temp.append(links_temp).append("\n");
                    //builder_cast.append(links_cast).append("\n");

                } catch (IOException e) {
                    builder_temp.append("Error");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //result.setText(extract_temp(builder_temp.toString()) + ", " + extract_cast(builder_cast.toString()));
                        temp = builder_temp.toString();
                        Log.v("hello", temp);
                        extract_temp(temp);
                        //여기 for문로 정리해보기
                        textView4.setText(JobPlace.get(0));
                        textView5.setText(JobPlace.get(1));
                        textView6.setText(JobPlace.get(2));
                        textView7.setText(JobPlace.get(3));
                        textView8.setText(JobPlace.get(4));
                        //

                    }
                });
            }


        }).start();
    }

//    public String extract_temp(String text){
//        boolean isFirst = true; //처음인지 검사
//        String text2="";
//
//        while(true){
//            int index = text.indexOf("ld=\">");    //찾는 문자열 위치 찾기
//
//            if(index == -1) break;  //없으면 종료
//
//            //text2 = text.substring(index+5); // 문자열을 찾은 위치로 재저장
//            text = text.substring(index+5);
//            int eindex = text.indexOf("<");
//            text2 = text.substring(0,eindex);
//            text = text.substring(eindex+1);
//            // text 끝
//
//        }
//        return text2;
//    }
public void extract_temp(String text){
    boolean isFirst = true; //처음인지 검사
    String text2="";
    JobPlace = new ArrayList<String>(120);
    while(true){
        int index = text.indexOf("ld=\">");    //찾는 문자열 위치 찾기

        if(index == -1) break;  //없으면 종료

        //text2 = text.substring(index+5); // 문자열을 찾은 위치로 재저장
        text = text.substring(index+5);
        int eindex = text.indexOf("<");
        text2 = text.substring(0,eindex);
        text = text.substring(eindex+1);
        // text 끝
        JobPlace.add(text2);

    }
}






}



