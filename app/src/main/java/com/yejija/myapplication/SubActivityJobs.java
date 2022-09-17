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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class SubActivityJobs extends AppCompatActivity {
    private String name;
    private String all;
    private ArrayList<String> JobPlace;
    private ArrayList<String> JobType;
    private ArrayList<String> JobAge;
    private ArrayList<String> JobLoc;
    private ArrayList<String> JobAvail;
    private ArrayList<String> JobUrl;

    private TextView textView;

    ListView mListView = null;
    private BaseAdapterEx mAdapter;
    ArrayList<Jobnotice> mData =null;

    EditText searchbox = null;
    ArrayList<Jobnotice> mSearchData =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //layout 설정하는 곳
        setContentView(R.layout.job_list);

        textView = (TextView) findViewById(R.id.callbutton);
        mData = new ArrayList<>();
        mSearchData = new ArrayList<>();
        searchbox = (EditText) findViewById(R.id.job_search);
        mListView = (ListView) findViewById(R.id.list_view);
        Toolbar toolbar = findViewById (R.id.toolbar_job);
        setSupportActionBar (toolbar);

        getWebsite();


        //전화하기
        textView = (TextView) findViewById(R.id.callbutton);
        textView.setOnClickListener(view -> startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:"+"02-6220-8640"))));


        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                String searchText = searchbox.getText().toString();
                mAdapter.filter(searchText);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
            }
        });

        //온클릭 웹뷰
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            String job = mData.get(position).getUrl();
            String jobUrl = "https://goldenjob.or.kr/job/find-person_view.asp?idx=" + job;
            Intent intent = new Intent(SubActivityJobs.this, WebViewActivity.class);
            intent.putExtra("job", jobUrl);
            startActivity(intent);

        });
        //온클릭
    }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_job, menu);

    return true;
}

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

        new Thread(() -> {
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
            runOnUiThread(() -> {

                name = builder_name.toString();
                all = builder_all.toString();
                extract_name(name);
                extract_type(all);
                extract_age(all);
                extract_loc(all);
                extract_avail(all);
                extract_site(name);

                for (int i = 0; i < JobPlace.size(); i++){
                    Jobnotice job = new Jobnotice();

                    if (JobAvail.get(i).equals("state-ing")) {

                        job.jname = JobPlace.get(i);
                        job.jtype = JobType.get(i);
                        job.jage = JobAge.get(i);
                        job.jloc = JobLoc.get(i);
                        job.jurl = JobUrl.get(i);
                        mData.add(job);

                    }
                    else break;
                }
                mAdapter = new BaseAdapterEx(SubActivityJobs.this, mData);
                mListView.setAdapter(mAdapter);

            });
        }).start();
    }


public void extract_name(String text){
    String text2="";
    JobPlace = new ArrayList<>(120);
    while(true){
        int index = text.indexOf("ld=\">");   //찾는 문자열 위치 찾기

        if(index == -1) break;  //없으면 종료

        text = text.substring(index+5);
        int eindex = text.indexOf("<");
        text2 = text.substring(0,eindex);
        text = text.substring(eindex+1);
        // text 끝
        JobPlace.add(text2);

    }
}



    public void extract_type(String text){
        boolean isFirst = true;
        String text2="";
        JobType = new ArrayList<>(120);
        while(true){
            int index = text.indexOf("직종</span>");

            if(index == -1) break;

            text = text.substring(index+9);
            int eindex = text.indexOf("<");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            // text 끝
            JobType.add("직종: "+text2);

        }
    }

    public void extract_age(String text){
        boolean isFirst = true;
        String text2="";
        JobAge = new ArrayList<>(120);
        while(true){
            int index = text.indexOf("연령</span>");

            if(index == -1) break;

            text = text.substring(index+9);
            int eindex = text.indexOf("<");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);

            JobAge.add("나이: "+text2);

        }
    }

    public void extract_loc(String text){
        boolean isFirst = true;
        String text2="";
        JobLoc = new ArrayList<>(120);
        while(true){
            int index = text.indexOf("지역</span>");

            if(index == -1) break;

            text = text.substring(index+9);
            int eindex = text.indexOf("<");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            JobLoc.add(text2);

        }
    }

    public void extract_avail(String text){
        boolean isFirst = true;
        String text2="";
        JobAvail = new ArrayList<>(120);
        while(true){
            int index = text.indexOf("구인상태</span><span class=\"");

            if(index == -1) break;

            text = text.substring(index+24);
            int eindex = text.indexOf("\">");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            JobAvail.add(text2);
        }
    }

    //사이트 주소 추출
    public void extract_site(String text){
        boolean isFirst = true;
        String text2="";
        JobUrl = new ArrayList<>(120);
        while(true){
            int index = text.indexOf("idx=");

            if(index == -1) break;

            text = text.substring(index+4);
            int eindex = text.indexOf("\"");
            text2 = text.substring(0,eindex);
            text = text.substring(eindex+1);
            JobUrl.add(text2);

        }
    }

}



