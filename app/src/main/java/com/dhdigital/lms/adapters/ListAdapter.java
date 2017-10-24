package com.dhdigital.lms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dhdigital.lms.R;
import com.dhdigital.lms.glide.HeaderLoader;
import com.dhdigital.lms.modal.Employee;
import com.dhdigital.lms.modal.Files;
import com.dhdigital.lms.net.APIUrls;
import com.dhdigital.lms.util.CircleTransform;

import java.util.Arrays;
import java.util.List;

/**
 * @author Kiran Bedre
 *         Created on 4/21/2017
 */

public class ListAdapter extends ArrayAdapter<Employee> {
    private List<Employee> mDataset;
    private Context context;
    private FragmentActivity mContext;


    public ListAdapter(Context context, List<Employee> mDataset) {
        super(context, R.layout.emp_lookup_item_layout, mDataset);
        this.context = context;
        this.mDataset = mDataset;
        this.mContext = (FragmentActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView empName;
        TextView employeeId;
        ImageView empImage;
        List<String> menuOptions = Arrays.asList("Remove as preferred", "Mark as preferred");
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
            empName = (TextView) convertView.findViewById(R.id.textView_username);
            employeeId = (TextView) convertView.findViewById(R.id.textView_userId);
            empImage = (ImageView) convertView.findViewById(R.id.userDispIcon);

            Employee employee = mDataset.get(position);
            empName.setText(employee.getCompleteName());
            employeeId.setText(String.valueOf(employee.getId()));
            Files fileUpload = employee.getFileUpload();
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
                        .into(empImage);


            }

        }
        return convertView;
    }


}
