package com.yejija.myapplication;

import android.content.Context;

public class Jobnotice {
    String jname; //회사이름
    String jtype; //직장 타입
    String jloc; //직장 위치
    String jtrue; //아직 모집중인지 아닌지.
    String jage; // 나이
    String jurl; //사이트

    public String getName(){
        return jname;
    }

    public String getType(){
        return jtype;
    }
    public String getLoc(){
        return jloc;
    }

    public String getAge(){
        return jage;
    }

    public String getUrl(){
        return jurl;
    }
}
