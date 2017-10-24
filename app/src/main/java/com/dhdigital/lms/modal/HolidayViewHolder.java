package com.dhdigital.lms.modal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dhdigital.lms.R;

/**
 * Created by admin on 13/10/17.
 */

public class HolidayViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView event;
    public TextView day;


    public HolidayViewHolder(View view) {
        super(view);

        this.date = (TextView) view.findViewById(R.id.holiday_date);
        this.event = (TextView) view.findViewById(R.id.event_name);
        this.day = (TextView) view.findViewById(R.id.holiday_day);

    }
}
