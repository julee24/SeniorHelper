package com.yejija.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class GuList extends AppCompatActivity {


    private ArrayList<GuItem> data = null;
    private ArrayList<String> web = null;
    String local_web ;
    String local_gu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* xml과 연결 */
        setContentView(R.layout.gu_list);

        ListView listView2 = (ListView) findViewById(R.id.gu_listview);
        TextView gu_icon = (TextView) findViewById(R.id.gu_icon);
        TextView gu_name = (TextView) findViewById(R.id.gu_name);

        /* 서버와 연동했닫면 값을 받아서 띄울 수 있지만,
         * 연동이 되어있지 않으므로
         * 하드코딩으로 값을 집어넣는다.
         * GuItem에 정의 한 구조대로 값을 넣을 수 있다.
         */

        /*
        lo_data = new ArrayList<>();
        SiItem si1 = new SiItem("서울시청");
        lo_data.add("2", "강남구청");

        SiAdapter adapter = new SiAdapter(this, R.layout.si_item, siData);
        listView1.setAdapter(adapter);


         */



        data = new ArrayList<>();
        // 1번 아이템
        GuItem gu1 = new GuItem("2","강남구청");
        GuItem gu2 = new GuItem("2","강동구청");
        GuItem gu3 = new GuItem("2","강북구청");
        GuItem gu4 = new GuItem("2","강서구청");
        GuItem gu5 = new GuItem("2","관악구청");
        GuItem gu6 = new GuItem("2","광진구청");
        GuItem gu7 = new GuItem("2","구로구청");
        GuItem gu8 = new GuItem("2","금천구청");
        GuItem gu9 = new GuItem("2","노원구청");
        GuItem gu10 = new GuItem("2","동대문구청");
        GuItem gu11 = new GuItem("2","도봉구청");
        GuItem gu12 = new GuItem("2","동작구청");
        GuItem gu13 = new GuItem("2","마포구청");
        GuItem gu14 = new GuItem("2","서대문구청");
        GuItem gu15 = new GuItem("2","성동구청");
        GuItem gu16 = new GuItem("2","성북구청");
        GuItem gu17 = new GuItem("2","서초구청");
        GuItem gu18 = new GuItem("2","송파구청");
        GuItem gu19 = new GuItem("2","영등포구청");
        GuItem gu20 = new GuItem("2","용산구청");
        GuItem gu21 = new GuItem("2","양천구청");
        GuItem gu22 = new GuItem("2","은평구청");
        GuItem gu23 = new GuItem("2","종로구청");
        GuItem gu24 = new GuItem("2","중구청");
        GuItem gu25 = new GuItem("2","중랑구청");

        GuItem gu26 = new GuItem("0", "서울시청");
        GuItem gu27 = new GuItem("1", "국민건강보험");
        GuItem gu28 = new GuItem("1", "중앙치매센터");


        //리스트에 추가
        data.add(gu26);
        data.add(gu27);
        data.add(gu28);
        data.add(gu1);
        data.add(gu2);
        data.add(gu3);
        data.add(gu4);
        data.add(gu5);
        data.add(gu6);
        data.add(gu7);
        data.add(gu8);
        data.add(gu9);
        data.add(gu10);
        data.add(gu11);
        data.add(gu12);
        data.add(gu13);
        data.add(gu14);
        data.add(gu15);
        data.add(gu16);
        data.add(gu17);
        data.add(gu18);
        data.add(gu19);
        data.add(gu20);
        data.add(gu21);
        data.add(gu22);
        data.add(gu23);
        data.add(gu24);
        data.add(gu25);

        //url
        web = new ArrayList<>();

        web.add("https://news.seoul.go.kr/welfare/archives/695");
        web.add("https://www.nhis.or.kr/nhis/index.do");
        web.add("https://www.nid.or.kr/main/main.aspx");
        web.add("http://gangnam.go.kr/pitapatgn/0802_elderlywelfare.html#two");
        web.add("https://www.gangdong.go.kr/web/newportal/contents/gdp_005_001_007_001_001");
        web.add("https://www.gangbuk.go.kr/www/contents.do?key=401");
        web.add("https://www.gangseo.seoul.kr/welfare/wel030402");
        web.add("https://www.gwanak.go.kr/site/gwanak/04/10405030000002016051205.jsp");
        web.add("https://www.gwangjin.go.kr/portal/main/contents.do?menuNo=200371");
        web.add("https://www.guro.go.kr/www/contents.do?key=2397&");
        web.add("https://www.geumcheon.go.kr/portal/contents.do?key=537");
        web.add("https://www.nowon.kr/www/info/info2/info2_03/info2_03_01/info2_03_01_02.jsp");
        web.add("https://www.ddm.go.kr/www/index.do");
        web.add("https://www.dobong.go.kr/Contents.asp?code=10003748");
        web.add("https://www.dongjak.go.kr/portal/main/main.do");
        web.add("https://www.mapo.go.kr/site/main/home");
        web.add("https://www.sdm.go.kr/index.do");
        web.add("https://www.seocho.go.kr/site/seocho/main.do");
        web.add("https://www.sd.go.kr/main/index.do");
        web.add("https://www.sb.go.kr/main/mainPage.do");
        web.add("https://www.songpa.go.kr/www/index.do");
        web.add("https://www.yangcheon.go.kr/site/yangcheon/main.do");
        web.add("https://www.ydp.go.kr/www/index.do");
        web.add("https://www.yongsan.go.kr/portal/main/main.do");
        web.add("https://www.ep.go.kr/www/index.do");
        web.add("https://www.jongno.go.kr/portalMain.do;jsessionid=AWZALW4ng5ttDgXSJwuio58FzQJ7Vxuar69tjuZu6zNRPMu7bOygzNYaAh6dlzQB.was_servlet_engine1");
        web.add("http://www.junggu.seoul.kr/index.jsp");
        web.add("https://www.jungnang.go.kr/portal/main.do");


        /* 리스트 속의 아이템 연결 */
        GuAdapter adapter = new GuAdapter(this, R.layout.gu_item, data);
        listView2.setAdapter(adapter);


        /* 아이템 클릭시 작동 */
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String getURL = web.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getURL));
                startActivity(intent);
            }
        });

        /*
        for (int i=0;i<=data.size();i++){
            if (data.get(i) ==  )
        }

         */



        Intent intent = getIntent();
        String address = intent.getStringExtra("address") + "청";

        gu_icon.setText("구청");
        gu_name.setText(address);


        //Log.e("hi",test+"!");

        for (int i=0;i<data.size();i++){
            local_gu = data.get(i).getName() ;
            Log.v("b", local_gu+"mm");
            if(local_gu.equals(address)){
                Log.v("hello", i+"??");
                Log.v("hi", web.get(i)+"!!");
                local_web = web.get(i);
            }
            else{continue;}
        }

        gu_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(local_web));
                startActivity(intent);
            }
        });


    }

}
