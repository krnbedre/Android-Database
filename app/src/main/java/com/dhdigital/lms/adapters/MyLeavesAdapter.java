package com.dhdigital.lms.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.Leave;
import com.dhdigital.lms.modal.LeaveModal;
import com.kelltontech.volley.utils.DateTimeUtils;


import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by admin on 04/10/17.
 */

public class MyLeavesAdapter extends ArrayAdapter<LeaveModal> {
    private Context context;
    private List<LeaveModal> listTR;
    private LayoutInflater mInflater;
    private int event;
    private int lastPosition = -1;

    public MyLeavesAdapter(Context context, List<LeaveModal> listTR , int networkEvent) {
        super(context, R.layout.my_leave_layout, listTR);
        this.context = context;
        this.listTR = listTR;
        this.mInflater = LayoutInflater.from(context);
        this.event = networkEvent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.my_leave_layout, null);
            holder.no_of_days = (TextView) convertView.findViewById(R.id.total_leaves_label);
            holder.from_date = (TextView) convertView.findViewById(R.id.from_date_text);
            holder.to_date = (TextView) convertView.findViewById(R.id.to_date_text);
            holder.leaveType = (TextView) convertView.findViewById(R.id.leave_type_text);
            holder.status = (TextView) convertView.findViewById(R.id.status_text);
            holder.leaveReaon = (TextView) convertView.findViewById(R.id.leave_reason_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LeaveModal leaveModal = listTR.get(position);
        Leave leave = listTR.get(position).getLeave();

        holder.from_date.setText(leaveModal.getStartDate() !=0 ? DateTimeUtils.getFormattedDate(leaveModal.getStartDate(), DateTimeUtils.Format.DD_Mmmm_YYYY) : "" );
        holder.to_date.setText(leaveModal.getEndDate() !=0 ? DateTimeUtils.getFormattedDate(leaveModal.getEndDate(), DateTimeUtils.Format.DD_Mmmm_YYYY) : "" );

        holder.leaveReaon.setText(null != leave.getLeaveReason()? leave.getLeaveReason().getName() : "");

        //Log.d("STR","ref no: "+ com.dhdigital.lms.modal.Leave.getReferenceNo());

        holder.leaveType.setText(null != leave.getLeaveType() ? leave.getLeaveType().getName(): "");
        holder.no_of_days.setText(Double.toString(leaveModal.getCount()));

        if (null != leave.getStatus()) {
            holder.status.setText(leave.getStatus().getName());
            LinearLayout status_holder = (LinearLayout) convertView.findViewById(R.id.status_text_holder);
            switch(leave.getStatus().getName().toUpperCase()) {

                case "APPROVED":
                    status_holder.setBackgroundColor(context.getColor(R.color.greenBulb));
                    break;
                case "TAKEN":
                    status_holder.setBackgroundColor(context.getColor(android.R.color.holo_blue_light));
                    break;
                case "CANCELLED":
                    status_holder.setBackgroundColor(context.getColor(R.color.common_grey_2));
                    break;
                case "REJECTED":
                    status_holder.setBackgroundColor(context.getColor(R.color.error_text_color));
                    break;
                case "APPROVAL PENDING":
                    status_holder.setBackgroundColor(context.getColor(android.R.color.holo_orange_light));
                    break;

            }
        }
        holder.status.setText(null != leave.getStatus() ? leave.getStatus().getName() : "");
      /*  Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ?  R.anim.up_from_bottom : R.anim.down_from_top );
        convertView.startAnimation(animation);
        lastPosition = position;*/
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    //---------------static views for each row-----------//
    static class ViewHolder {
        TextView no_of_days;
        TextView from_date;
        TextView to_date;
        TextView leaveType;
        TextView status;
        TextView leaveReaon;
    }
}
