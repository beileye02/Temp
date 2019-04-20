package com.ssx.hepingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ssx.hepingapp.R;
import com.ssx.hepingapp.data.PolicyData;
import com.ssx.hepingapp.data.RecordData;

import java.util.ArrayList;
import java.util.List;

public class RecordAdapter extends BaseAdapter {
    private Context context;
    private List<RecordData> dataList = new ArrayList<RecordData>();
    private LayoutInflater inflater;

    public RecordAdapter(Context context, List<RecordData> dataList) {
        this.context = context;
        this.dataList = dataList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.layout_record_item, null);
            holder.icon = convertView.findViewById(R.id.icon);
            holder.title = convertView.findViewById(R.id.title);
            holder.time = convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        RecordData recordData = dataList.get(position);
        if (!"0".equals(recordData.getUrl())) {
            Glide.with(context).load(recordData.getUrl()).placeholder(R.mipmap.ic_launcher).into(holder.icon);
        }
        holder.title.setText(recordData.getTitle());
        holder.time.setText(recordData.getAddTime());
        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView time;
    }
}
