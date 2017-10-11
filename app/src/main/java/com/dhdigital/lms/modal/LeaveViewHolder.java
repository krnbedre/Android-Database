package com.dhdigital.lms.modal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhdigital.lms.R;

/**
 * Created by admin on 06/10/17.
 */

public class LeaveViewHolder extends RecyclerView.ViewHolder {

    public TextView no_of_days;
    public TextView from_date;
    public TextView to_date;
    public TextView leaveType;
    public TextView status;
    public TextView leaveReaon;
    public LinearLayout statusHolder;

    public LeaveViewHolder(View view) {
        super(view);

        this.no_of_days = (TextView) view.findViewById(R.id.total_leaves_label);
        this.from_date = (TextView) view.findViewById(R.id.from_date_text);
        this.to_date = (TextView) view.findViewById(R.id.to_date_text);
        this.leaveType = (TextView) view.findViewById(R.id.leave_type_text);
        this.status = (TextView) view.findViewById(R.id.status_text);
        this.leaveReaon = (TextView) view.findViewById(R.id.leave_reason_text);
        this.statusHolder = (LinearLayout) view.findViewById(R.id.status_text_holder);


    }
}
