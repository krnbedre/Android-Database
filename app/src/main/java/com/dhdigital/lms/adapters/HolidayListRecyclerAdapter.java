package com.dhdigital.lms.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.Holiday;
import com.dhdigital.lms.modal.HolidayViewHolder;
import com.kelltontech.volley.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 13/10/17.
 */

public class HolidayListRecyclerAdapter extends
        RecyclerView.Adapter<HolidayViewHolder> {

    private List<Holiday> arrayList = new ArrayList<>();
    private Context context;


    public HolidayListRecyclerAdapter(Context context,
                                      List<Holiday> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(HolidayViewHolder viewholder,
                                 int position) {


        final HolidayViewHolder holder = (HolidayViewHolder) viewholder;
        //Setting text over textview


        Holiday holidayDay = arrayList.get(position);


        holder.date.setText(holidayDay.getDate() != 0 ? DateTimeUtils.getFormattedDate(holidayDay.getDate(), DateTimeUtils.Format.ONLY_DATE) + " " + DateTimeUtils.getFormattedDate(holidayDay.getDate(), DateTimeUtils.Format.ONLY_MONTH) : "");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(holidayDay.getDate());
        int day = cal.get(Calendar.DAY_OF_WEEK);

        String weekDay = "";

        if (day == Calendar.MONDAY) weekDay = "MON";
        else if (day == Calendar.TUESDAY) weekDay = "TUE";
        else if (day == Calendar.WEDNESDAY) weekDay = "WED";
        else if (day == Calendar.THURSDAY) weekDay = "THU";
        else if (day == Calendar.FRIDAY) weekDay = "FRI";
        else if (day == Calendar.SATURDAY) weekDay = "SAT";
        else if (day == Calendar.SUNDAY) weekDay = "SUN";


        holder.day.setText(weekDay);
        holder.event.setText(holidayDay.getName());


    }

    @Override
    public HolidayViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.holiday_list_item, viewGroup, false);
        HolidayViewHolder mainHolder = new HolidayViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };


        return mainHolder;

    }


}

