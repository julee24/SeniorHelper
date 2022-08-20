package com.yejija.myapplication;

import android.app.Application;

public class Save_tel extends Application {
    String tel_num;

    public String getSomeVariable() {
        return tel_num;
    }

    public void setSomeVariable(String num) {
        this.tel_num = num;
    }
}
