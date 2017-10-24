package com.dhdigital.lms.adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.modal.LeaveViewHolder;
import com.kelltontech.volley.utils.DateTimeUtils;

import java.util.ArrayList;

/**
 * Created by admin on 06/10/17.
 */

public class MyLeavesRecyclerAdapter extends
        RecyclerView.Adapter<LeaveViewHolder> {

    private ArrayList<LeaveModal> arrayList = new ArrayList<>();
    private Context context;


    public MyLeavesRecyclerAdapter(Context context,
                                ArrayList<LeaveModal> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }

    @Override
    public void onBindViewHolder(LeaveViewHolder viewholder,
                                 int position) {


        final LeaveViewHolder holder = (LeaveViewHolder) viewholder;
        //Setting text over textview




        LeaveModal leaveModal = arrayList.get(position);


        holder.from_date.setText(leaveModal.getStartDate() !=0 ? DateTimeUtils.getFormattedDate(leaveModal.getStartDate(), DateTimeUtils.Format.DD_Mmm_YYYY) : "" );
        holder.to_date.setText(leaveModal.getEndDate() !=0 ? DateTimeUtils.getFormattedDate(leaveModal.getEndDate(), DateTimeUtils.Format.DD_Mmm_YYYY) : "" );

        holder.leaveReaon.setText(null != leaveModal.getLeaveReason()? leaveModal.getLeaveReason().getName() : "");

        //Log.d("STR","ref no: "+ com.dhdigital.lms.modal.Leave.getReferenceNo());

        holder.leaveType.setText(null != leaveModal.getLeaveType() ? leaveModal.getLeaveType().getName(): "");
        holder.no_of_days.setText(Double.toString(leaveModal.getCount()) + " Days");

        if (null != leaveModal.getStatus()) {
            holder.status.setText(leaveModal.getStatus().getName());
            LinearLayout status_holder = holder.statusHolder;
            switch(leaveModal.getStatus().getName().toUpperCase()) {

                case "APPROVED":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        status_holder.setBackgroundColor(context.getColor(R.color.greenBulb));
                        //listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.logout_icon), null, null, null);
                    } else {
                        status_holder.setBackgroundColor(context.getResources().getColor(R.color.greenBulb, null));
                        // listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.logout_icon), null, null, null);
                    }

                    break;
                case "TAKEN":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        status_holder.setBackgroundColor(context.getColor(android.R.color.holo_blue_light));
                        //listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.logout_icon), null, null, null);
                    } else {
                        status_holder.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light, null));
                        // listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.logout_icon), null, null, null);
                    }
                    //status_holder.setBackgroundColor(context.getColor(android.R.color.holo_blue_light));
                    break;
                case "CANCELLED":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        status_holder.setBackgroundColor(context.getColor(android.R.color.holo_blue_light));
                        //listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.logout_icon), null, null, null);
                    } else {
                        status_holder.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light, null));
                        // listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.logout_icon), null, null, null);
                    }
                    //status_holder.setBackgroundColor(context.getColor(R.color.common_grey_2));
                    break;
                case "REJECTED":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        status_holder.setBackgroundColor(context.getColor(R.color.error_text_color));
                        //listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.logout_icon), null, null, null);
                    } else {
                        status_holder.setBackgroundColor(context.getResources().getColor(R.color.error_text_color, null));
                        // listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.logout_icon), null, null, null);
                    }
                    //status_holder.setBackgroundColor(context.getColor(R.color.error_text_color));
                    break;
                case "APPROVAL PENDING":
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        status_holder.setBackgroundColor(context.getColor(android.R.color.holo_orange_light));
                        //listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getDrawable(R.drawable.logout_icon), null, null, null);
                    } else {
                        status_holder.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_light, null));
                        // listTitleTextView.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.logout_icon), null, null, null);
                    }
                    //status_holder.setBackgroundColor(context.getColor(android.R.color.holo_orange_light));
                    break;

            }
        }
        holder.status.setText(null != leaveModal.getStatus() ? leaveModal.getStatus().getName() : "");

    }

    @Override
    public LeaveViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.my_leave_layout, viewGroup, false);
        LeaveViewHolder mainHolder = new LeaveViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };


        return mainHolder;

    }


}
