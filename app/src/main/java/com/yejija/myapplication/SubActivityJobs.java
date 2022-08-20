package com.yejija.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
    private String name;
    private String all;
    private String a;
    private ArrayList<String> JobPlace;
    private ArrayList<String> JobType;
    private ArrayList<String> JobAge;
    private ArrayList<String> JobLoc;
    private ArrayList<String> JobAvail;

    //

    ////big change
    ListView mListView = null;
    BaseAdapterEx mAdapter = null;
    ArrayList<Jobnotice> mData =null;
    ////

    //int t = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //layout 설정하는 곳
        setContentView(R.layout.job_list);

//        textView4 = (TextView)findViewById(R.id.textView4);
//        textView5 = (TextView)findViewById(R.id.textView5);
//        textView6 = (TextView)findViewById(R.id.textView6);
//        textView7 = (TextView)findViewById(R.id.textView7);
//        textView8 = (TextView)findViewById(R.id.textView8);
        mData = new ArrayList<Jobnotice>();
        getWebsite();

    }

    private void getWebsite() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder_name = new StringBuilder();// 직장 이름
                final StringBuilder builder_all = new StringBuilder();// 나머지
                //final StringBuilder builder_age = new StringBuilder(); // 나이

                try {
                    for (int t=1;t<10; t++ ) {
                        String urlname = "https://goldenjob.or.kr/job/find-person.asp?keyfield=&keyword=&p=" + t;
                        Log.v("url", urlname);
                        Document doc = Jsoup.connect(urlname).get(); //크롤링할 주소
                        Elements links_name = doc.select("a");  //직장 이름
                        Elements links_all = doc.select("td"); //직종
                        builder_name.append(links_name).append("\n");
                        builder_all.append(links_all).append("\n");
                    }
                } catch (IOException e) {
                    builder_name.append("Error");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //result.setText(extract_temp(builder_name.toString()) + ", " + extract_cast(builder_all.toString()));
                        name = builder_name.toString();
                        all = builder_all.toString();
                        //Log.v("hello1", name);
                        Log.v("hello2", all);
                        extract_name(name);
                        extract_type(all);
                        extract_age(all);
                        extract_loc(all);
                        extract_avail(all);
//                        //여기 for문로 정리해보기
//                        textView4.setText(JobPlace.get(0));
//                        textView5.setText(JobPlace.get(1));
//                        textView6.setText(JobPlace.get(2));
//                        textView7.setText(JobPlace.get(3));
//                        textView8.setText(JobPlace.get(4));
//                        //

                        //// 문제 일어나면 여기 삭제!
//                        mData = new ArrayList<Jobnotice>();

                        //Log.v("jname 크기", "" + JobPlace.size());

                        for (int i = 0; i < JobPlace.size(); i++){
                            Jobnotice job = new Jobnotice();
                            //Log.v("구인상태",JobAvail.get(i));
                            if (JobAvail.get(i).equals("state-ing")) {

                                job.jname = JobPlace.get(i);
                                job.jtype = JobType.get(i);
                                job.jage = JobAge.get(i);
                                job.jloc = JobLoc.get(i);
                                mData.add(job);
                            }
                            else break;
                        }


                        mAdapter = new BaseAdapterEx(SubActivityJobs.this, mData);
                        //원래 그냥 (this, mData였다)

                        mListView = (ListView) findViewById(R.id.list_view);
                        mListView.setAdapter(mAdapter);

                        //// 여기까지 수정한것!

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
public void extract_name(String text){
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


    public void extract_type(String text){
        boolean isFirst = true; //처음인지 검사
        String text2="";
        JobType = new ArrayList<String>(120);
        while(true){
            int index = text.indexOf("직종</span>");    //찾는 문자열 위치 찾기

            if(index == -1) break;  //없으면 종료

            //text2 = text.substring(index+5); // 문자열을 찾은 위치로 재저장
            text = text.substring(index+9);
            int eindex = text.indexOf("<");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            // text 끝
            JobType.add(text2);

        }
    }

    public void extract_age(String text){
        boolean isFirst = true; //처음인지 검사
        String text2="";
        JobAge = new ArrayList<String>(120);
        while(true){
            int index = text.indexOf("연령</span>");    //찾는 문자열 위치 찾기

            if(index == -1) break;  //없으면 종료

            //text2 = text.substring(index+5); // 문자열을 찾은 위치로 재저장
            text = text.substring(index+9);
            int eindex = text.indexOf("<");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            // text 끝
            JobAge.add(text2);

        }
    }

    public void extract_loc(String text){
        boolean isFirst = true; //처음인지 검사
        String text2="";
        JobLoc = new ArrayList<String>(120);
        while(true){
            int index = text.indexOf("지역</span>");    //찾는 문자열 위치 찾기

            if(index == -1) break;  //없으면 종료

            //text2 = text.substring(index+5); // 문자열을 찾은 위치로 재저장
            text = text.substring(index+9);
            int eindex = text.indexOf("<");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            // text 끝
            JobLoc.add(text2);

        }
    }

    public void extract_avail(String text){
        boolean isFirst = true; //처음인지 검사
        String text2="";
        JobAvail = new ArrayList<String>(120);
        while(true){
            int index = text.indexOf("구인상태</span><span class=\"");    //찾는 문자열 위치 찾기

            if(index == -1) break;  //없으면 종료

            //text2 = text.substring(index+5); // 문자열을 찾은 위치로 재저장
            text = text.substring(index+24);
            int eindex = text.indexOf("\">");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            // text 끝
            JobAvail.add(text2);
        }
    }





}



