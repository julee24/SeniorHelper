package com.yejija.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapterEx extends BaseAdapter{
    Context mContext = null;
    ArrayList<Jobnotice> mData = null;
    LayoutInflater mLayoutInflater = null;
    //
    ArrayList<Jobnotice> mSearchData= null;
    //

    public BaseAdapterEx(Context context, ArrayList<Jobnotice> mSearchdata){
        this.mContext = context;
        //this.mData = data;
        this.mSearchData = mSearchdata;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mData = new ArrayList<Jobnotice>();
        this.mData.addAll(mSearchdata);
    }
    //8.22 mData to mSearchData
    @Override
    public int getCount(){
        return mSearchData.size();
    }
    @Override
    public Jobnotice getItem(int position ){
        return mSearchData.get(position);
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
        //
        final Jobnotice jobnotice = mSearchData.get(position);
        //
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
        viewHolder.mNameTv.setText(mSearchData.get(position).jname);
        viewHolder.mNumberTv.setText(mSearchData.get(position).jtype);
        viewHolder.mlocTv.setText(mSearchData.get(position).jloc);
        viewHolder.mAgeTv.setText(mSearchData.get(position).jage);

        return itemLayout;
    }
    //
    public void filter(String searchText) {
        mSearchData.clear();
        Log.v("searchtext", " " + searchText);
        if(searchText.length() == 0)
        {
            mSearchData.addAll(mData);
        }
        else
        {
            Log.v("hello", "size: " + mData.size() );
            for (int i = 0; i < mData.size(); i++)
            //Jobnotice item : mData
            {
                Log.v("hello-for", "yup hello");
                //Log.v("itemname", item.getName());
                if(mData.get(i).getName().contains(searchText) || mData.get(i).getType().contains(searchText) || mData.get(i).getLoc().contains(searchText) || mData.get(i).getAge().contains(searchText))
                {
                    mSearchData.add(mData.get(i));
                }
            }
        }
        notifyDataSetChanged(); //이거 위치 바꿔야 하나..?
        Log.v("hello2", "yup hello");
    }
    //
}
