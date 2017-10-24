package com.dhdigital.lms.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

/**
 * Created by admin on 23/10/17.
 */

public class MySpinner extends AppCompatSpinner {
    OnItemSelectedListener listener;

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(null, null, position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            OnItemSelectedListener listener) {
        //this.listener = listener;
    }
}