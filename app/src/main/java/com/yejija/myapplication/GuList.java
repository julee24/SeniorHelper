package com.yejija.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GuList extends AppCompatActivity {


    private ArrayList<GuItem> data = null;
    private ArrayList<String> web = null;
    String local_web ;
    String local_gu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gu_list);

        ListView listView2 = (ListView) findViewById(R.id.gu_listview);
        TextView gu_icon = (TextView) findViewById(R.id.gu_icon);
        TextView gu_name = (TextView) findViewById(R.id.gu_name);
        LinearLayout linear = (LinearLayout) findViewById(R.id.linear);


        data = new ArrayList<>();

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

        web = new ArrayList<>();

        web.add("https://wis.seoul.go.kr/senior/service/certify.do");
        web.add("https://www.longtermcare.or.kr/npbs/e/b/201/npeb201m01.web?menuId=npe0000000080&prevPath=/npbs/e/b/202/npeb202m01.web");
        web.add("https://m.nid.or.kr/support/c_service.aspx?RC=020000002");
        web.add("http://gangnam.go.kr/pitapatgn/0802_elderlywelfare.html#two");
        web.add("https://www.gangdong.go.kr/web/newportal/contents/gdp_005_001_007_001_001");
        web.add("https://www.gangbuk.go.kr/www/contents.do?key=401");
        web.add("https://www.gangseo.seoul.kr/welfare/wel030402");
        web.add("https://www.gwanak.go.kr/site/gwanak/04/10405030000002016051205.jsp");
        web.add("https://www.gwangjin.go.kr/portal/main/contents.do?menuNo=200371");
        web.add("https://www.guro.go.kr/www/contents.do?key=2397&");
        web.add("https://www.geumcheon.go.kr/portal/contents.do?key=537");
        web.add("https://www.nowon.kr/www/info/info2/info2_03/info2_03_01/info2_03_01_02.jsp");
        web.add("https://www.ddm.go.kr/www/contents.do?key=686");
        web.add("https://www.dobong.go.kr/Contents.asp?code=10003748");
        web.add("https://www.dongjak.go.kr/portal/main/contents.do?menuNo=200331");
        web.add("https://www.mapo.go.kr/site/main/content/mapo05011204");
        web.add("https://www.sdm.go.kr/welfare/old/careservice.do");
        web.add("https://www.sd.go.kr/main/contents.do?key=1541&");
        web.add("https://www.sb.go.kr/main/mainPage.do");
        web.add("https://www.seocho.go.kr/site/seocho/04/10402030100002015070710.jsp");
        web.add("https://www.songpa.go.kr/www/contents.do?key=2983&");
        web.add("https://www.ydp.go.kr/www/contents.do?key=3806&");
        web.add("https://www.yongsan.go.kr/portal/main/contents.do?menuNo=200515");
        web.add("https://www.yangcheon.go.kr/site/yangcheon/ex/bbs/List.do?cbIdx=368");
        web.add("https://www.ep.go.kr/www/contents.do?key=892");
        web.add("https://www.jongno.go.kr/portalMain.do;jsessionid=AWZALW4ng5ttDgXSJwuio58FzQJ7Vxuar69tjuZu6zNRPMu7bOygzNYaAh6dlzQB.was_servlet_engine1");
        web.add("http://www.junggu.seoul.kr/content.do?cmsid=14386&sf_text4=G");
        web.add("https://www.jungnang.go.kr/portal/bbs/list/B0000157.do?menuNo=200775");


        GuAdapter adapter = new GuAdapter(this, R.layout.gu_item, data);
        listView2.setAdapter(adapter);


        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                String getURL = web.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getURL));
                startActivity(intent);
            }
        });




        Intent intent = getIntent();
        String address = intent.getStringExtra("address") + "청";

        gu_icon.setText("구청");
        gu_name.setText(address);



        for (int i=0;i<data.size();i++){
            local_gu = data.get(i).getName() ;
            Log.v("b", local_gu+"mm");
            if(local_gu.equals(address)){
                Log.v("hello", i+"??");
                Log.v("hi", web.get(i)+"!!");
                local_web = web.get(i);
            }
        }

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(local_web));
                startActivity(intent);
            }
        });


    }

}
