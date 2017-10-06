package com.dhdigital.lms.adapters;

/**
 * Created by Kiran Bedre on 8/8/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


import com.dhdigital.lms.R;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.net.HeaderManager;
import com.dhdigital.lms.net.NetworkEvents;
import com.dhdigital.lms.net.VolleyErrorListener;



import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;




    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       LinkedHashMap<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        //updateCountStatus();
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null, false);
        }
        //updateCountStatus();
        Log.d("TAG", " listPosition :: " + listPosition + " expandedListPosition :: " + expandedListPosition);

        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        TextView countTextView = (TextView) convertView
                .findViewById(R.id.listItemCount);
        expandedListTextView.setText(expandedListText);
        switch (listPosition) {
            case 0:
                switch (expandedListPosition) {
                    case 0:
                        countTextView.setVisibility(View.GONE);
                        break;
                    case 1:
                        countTextView.setVisibility(View.GONE);
                        break;
                    case 2:
                        countTextView.setVisibility(View.GONE);
                        break;
                }
                break;
            case 1:
                switch (expandedListPosition) {
                    case 0:
                        countTextView.setVisibility(View.GONE);
                        break;
                    case 1:
                        countTextView.setVisibility(View.GONE);


                        break;
                    case 2:
                        countTextView.setVisibility(View.VISIBLE);

                        break;
                }
                break;
            case 2:
                switch (expandedListPosition) {
                    case 0:
                        countTextView.setVisibility(View.GONE);

                        break;
                    case 1:
                        countTextView.setVisibility(View.GONE);

                    case 2:
                        countTextView.setVisibility(View.GONE);

                        break;

                }
                break;
            case 3:
            case 4:
                countTextView.setVisibility(View.GONE);
                break;

            default:
                break;

        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null, false);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        //updateCountStatus();
        switch (listPosition) {
            case 0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.apply_leave_icon), null, null, null);
                } else {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.apply_leave_icon), null, null, null);
                }
                //setDraBackground(context.getDrawable(R.drawable.title_icon_flight));
                break;
            case 1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.option_indicator), null, null, null);
                } else {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.option_indicator), null, null, null);
                }

                //  listTitleImageView.setBackground(context.getDrawable(R.drawable.title_icon_expense));
                break;
            case 2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.logout_icon), null, null, null);
                } else {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.logout_icon), null, null, null);
                }
                //listTitleImageView.setBackground(context.getDrawable(R.drawable.title_icon_my_inbox));
                break;
            case 3:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.option_indicator), null, null, null);
                } else {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.option_indicator), null, null, null);
                }
                break;
            case 4:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.option_indicator), null, null, null);
                } else {
                    listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.option_indicator), null, null, null);
                }
                break;
        }
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    /**
     *
     */


    public int getSize() {
        return expandableListTitle.size();
    }
}