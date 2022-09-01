package com.yejija.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class GuAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<GuItem> data; //Item 목록을 담을 배열
    private int layout;
    Context context = null;

    public GuAdapter(Context context, int layout, ArrayList<GuItem> data) {
        this.context=context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data= data;
        this.layout = layout;
    }

    @Override
    public int getCount() { //리스트 안 Item의 개수를 센다.
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getName();
    }

    //
    @Override
    public long getItemId(int position) {
        return position;
    }
    //

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }
        GuItem guItem = data.get(position);

        //이름 연동
        TextView icon = (TextView) convertView.findViewById(R.id.icon);
        TextView info = (TextView) convertView.findViewById(R.id.name);

        if(guItem.getIcon()=="0"){
            icon.setText("시청");
            icon.setBackground(context.getResources().getDrawable(R.drawable.magnitude_si_circle));
        }
        else if(guItem.getIcon()=="1") {
            icon.setText("의료");
            icon.setBackground(context.getResources().getDrawable(R.drawable.magnitude_medi_circle));
        }
        else if(guItem.getIcon()=="2"){
            icon.setText("구청");
            icon.setBackground(context.getResources().getDrawable(R.drawable.magnitude_gu_circle));
        }

        info.setText(guItem.getName());

        return convertView;
    }
}