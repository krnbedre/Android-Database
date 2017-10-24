package com.dhdigital.lms.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhdigital.lms.R;
import com.dhdigital.lms.activities.ApproverTasksActivity;
import com.dhdigital.lms.glide.HeaderLoader;
import com.dhdigital.lms.modal.Files;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.util.CircleTransform;
import com.kelltontech.volley.utils.DateTimeUtils;

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
    private ApproverTasksActivity activity;

    public MyLeavesAdapter(Context context, List<LeaveModal> listTR , int networkEvent) {
        super(context, R.layout.my_leave_layout, listTR);
        this.context = context;
        this.listTR = listTR;
        this.mInflater = LayoutInflater.from(context);
        this.event = networkEvent;
        activity = (ApproverTasksActivity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.approver_leave_request_item, null);
            holder.no_of_days = (TextView) convertView.findViewById(R.id.total_leaves_label);
            holder.from_date = (TextView) convertView.findViewById(R.id.from_date_text);
            holder.to_date = (TextView) convertView.findViewById(R.id.to_date_text);
            holder.leaveType = (TextView) convertView.findViewById(R.id.leave_type_text);
            holder.leaveReaon = (TextView) convertView.findViewById(R.id.leave_reason_text);
            holder.approvebtn = (Button) convertView.findViewById(R.id.approve_button);
            holder.rejectbtn = (Button) convertView.findViewById(R.id.reject_button);
            holder.userName = (TextView) convertView.findViewById(R.id.requestor_name);
            holder.userIcon = (ImageView) convertView.findViewById(R.id.user_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LeaveModal leaveModal = listTR.get(position);


        holder.from_date.setText(leaveModal.getStartDate() !=0 ? DateTimeUtils.getFormattedDate(leaveModal.getStartDate(), DateTimeUtils.Format.DD_Mmm_YYYY) : "" );
        holder.to_date.setText(leaveModal.getEndDate() !=0 ? DateTimeUtils.getFormattedDate(leaveModal.getEndDate(), DateTimeUtils.Format.DD_Mmm_YYYY) : "" );

        holder.leaveReaon.setText(null != leaveModal.getLeaveReason()? leaveModal.getLeaveReason().getName() : "");

        //Log.d("STR","ref no: "+ com.dhdigital.lms.modal.Leave.getReferenceNo());

        holder.leaveType.setText(null != leaveModal.getLeaveType() ? leaveModal.getLeaveType().getName(): "");
        holder.no_of_days.setText(Double.toString(leaveModal.getCount()) + " Days");


        holder.userName.setText(leaveModal.getRequestedBy().getEmployee().getCompleteName());
        holder.rejectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.listRejectBtnSelected(position);
            }
        });

        Files fileUpload = leaveModal.getRequestedBy().getEmployee().getFileUpload();
        if (null != fileUpload) {

            String URL = APIUrls.FILE_DOWNLOAD + "?fileName=" + fileUpload.getFileName() + "&filePath=" + fileUpload.getPathURI();
            Log.d("IMAGE", "URL: " + URL);
            //AppUtil.loadThumbNailImage(context,URL ,holder.userIcon);


            Glide.with(context)
                    .load(HeaderLoader.getUrlWithHeaders(URL, context))
                    .asBitmap()
                    .transform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .placeholder(R.drawable.user_icon_disp)
                    .into(holder.userIcon);


        }
        holder.approvebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.listApproveBtnSelected(position);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.listItemSelected(view,position);
            }
        });


        //holder.status.setText(null != leave.getStatus() ? leave.getStatus().getName() : "");
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
        TextView leaveReaon;
        TextView userName;
        Button approvebtn;
        Button rejectbtn;
        ImageView userIcon;
    }
}
