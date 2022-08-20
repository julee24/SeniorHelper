package com.yejija.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BaseAdapterEx extends BaseAdapter {
    Context mContext = null;
    ArrayList<Jobnotice> mData = null;
    LayoutInflater mLayoutInflater = null;
    public BaseAdapterEx(Context context, ArrayList<Jobnotice> data){
        mContext = context;
        mData = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount(){
        return mData.size();
    }

    @Override
    public Jobnotice getItem(int position ){
        return mData.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    class ViewHolder{
        TextView mNameTv;
        TextView mNumberTv;
        TextView mlocTv;
        TextView mAgeTv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View itemLayout = convertView;
        ViewHolder viewHolder = null;
        //어뎁터뷰가 재사용할 뷰를 넘겨주지 않은 경우에만 새로운 뷰를 생성.
        if(itemLayout == null){
            itemLayout = mLayoutInflater.inflate(R.layout.list_view_item_layout, null);

            viewHolder = new ViewHolder();

            viewHolder.mNameTv = (TextView) itemLayout.findViewById(R.id.name_text);
            viewHolder.mNumberTv = (TextView) itemLayout.findViewById(R.id.number_text);
            viewHolder.mlocTv = (TextView) itemLayout.findViewById(R.id.loc_text);
            viewHolder.mAgeTv = (TextView) itemLayout.findViewById(R.id.age_text);
            itemLayout.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)itemLayout.getTag();
        }
        viewHolder.mNameTv.setText(mData.get(position).jname);
        viewHolder.mNumberTv.setText(mData.get(position).jtype);
        viewHolder.mlocTv.setText(mData.get(position).jloc);
        viewHolder.mAgeTv.setText(mData.get(position).jage);

        return itemLayout;
    }
}
