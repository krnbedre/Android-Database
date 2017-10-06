package com.dhdigital.lms.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.dhdigital.lms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Parveen on 7/18/2016.
 */
public class CustomAlertAdapter extends BaseAdapter {

    Context ctx=null;

    List<String> listarray= new ArrayList<String>();

    private LayoutInflater mInflater=null;

    public CustomAlertAdapter(Activity activty, List<String> list)
    {
        this.ctx=activty;
        mInflater = activty.getLayoutInflater();
        this.listarray=list;
    }

    @Override
    public int getCount() {
        return listarray.size();
    }


    @Override
    public Object getItem(int arg0) {
        return null;
    }


    @Override
    public long getItemId(int arg0) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        if (convertView == null ) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.search_list_view, null);
            holder.titlename = (TextView) convertView.findViewById(R.id.location_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        String datavalue=listarray.get(position);
        holder.titlename.setText(datavalue);
        return convertView;
    }



    private static class ViewHolder {
        TextView titlename;
    }
}
