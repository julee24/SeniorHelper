package com.yejija.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

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
    private ArrayList<String> JobUrl;

    private TextView textView;
    //

    ////big change
    ListView mListView = null;
    private BaseAdapterEx mAdapter;
    BaseAdapterEx mAdapterTemp;
    ArrayList<Jobnotice> mData =null;
    ////

    // 8/22
    EditText searchbox = null;
    ArrayList<Jobnotice> mSearchData =null;
    //

    //온클릭

    //int t = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //layout 설정하는 곳
        setContentView(R.layout.job_list);

        textView = (TextView) findViewById(R.id.callbutton);
//        textView4 = (TextView)findViewById(R.id.textView4);
//        textView5 = (TextView)findViewById(R.id.textView5);
//        textView6 = (TextView)findViewById(R.id.textView6);
//        textView7 = (TextView)findViewById(R.id.textView7);
//        textView8 = (TextView)findViewById(R.id.textView8);
        mData = new ArrayList<Jobnotice>();
        //8.22
        mSearchData = new ArrayList<Jobnotice>();
        searchbox = (EditText) findViewById(R.id.job_search);
        mListView = (ListView) findViewById(R.id.list_view);
        Toolbar toolbar = findViewById (R.id.toolbar_job);
        setSupportActionBar (toolbar);

        //
        getWebsite();
        //mAdapter = new BaseAdapterEx(SubActivityJobs.this, mData);
        //mListView.setAdapter(mAdapter);


        //전화하기
        textView = (TextView) findViewById(R.id.callbutton);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+"02-6220-8640")));
            }
        });


        //8.22
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                String searchText = searchbox.getText().toString();
                //mAdapter.filter(searchText);
                mAdapter.filter(searchText);
//
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
            }
        });
        //8.
        //온클릭 웹뷰
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //
//                Fruit fruit = fruitList.get(position);
//                Toast.makeText(MainActivity.this, fruit.getName(),
//                        Toast.LENGTH_SHORT).show();
                String job = mData.get(position).getUrl();
                String jobUrl = "https://goldenjob.or.kr/job/find-person_view.asp?idx=" + job;
                Intent intent = new Intent(SubActivityJobs.this, WebViewActivity.class);
                intent.putExtra("job", jobUrl);
                startActivity(intent);

            }
        });
        //온클릭
    }
//
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_job, menu);

    return true;
}

    //앱바(App Bar)에 표시된 액션 또는 오버플로우 메뉴가 선택되면
    //액티비티의 onOptionsItemSelected() 메서드가 호출
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        final EditText layout = (EditText) findViewById(R.id.job_search);
        switch (item.getItemId()) {
            case R.id.item1:
                layout.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        //Log.v("hello1", all);
                        //Log.v("hello2", name);
                        extract_name(name);
                        extract_type(all);
                        extract_age(all);
                        extract_loc(all);
                        extract_avail(all);
                        extract_site(name);

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
                                job.jurl = JobUrl.get(i);
                                mData.add(job);
                                //
                                //
                                //mSearchData.add(job);
                                //
                            }
                            else break;
                        }
                        Jobnotice mSearchData = new Jobnotice();
                        mAdapter = new BaseAdapterEx(SubActivityJobs.this, mData);
                        //mAdapterTemp = mAdapter;

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
            JobType.add("직종: "+text2);

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
            JobAge.add("나이: "+text2);

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

    //사이트 주소 추출
    public void extract_site(String text){
        boolean isFirst = true; //처음인지 검사
        String text2="";
        JobUrl = new ArrayList<String>(120);
        while(true){
            int index = text.indexOf("idx=");    //찾는 문자열 위치 찾기

            if(index == -1) break;  //없으면 종료

            //text2 = text.substring(index+5); // 문자열을 찾은 위치로 재저장
            text = text.substring(index+4);
            int eindex = text.indexOf("\"");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            // text 끝
            JobUrl.add(text2);

        }
    }

    //site form: /find-person_view.asp?idx=905&amp;p=1&amp;keyword=&amp;keyfield=








}



