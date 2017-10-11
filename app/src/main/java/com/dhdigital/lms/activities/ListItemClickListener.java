package com.dhdigital.lms.activities;

import android.view.View;

/**
 * Created by admin on 09/10/17.
 */


interface ListItemClickListener {

        void listItemSelected(View childView, final int position);
        void listApproveBtnSelected(final int position);
        void listRejectBtnSelected(final int position);

}
