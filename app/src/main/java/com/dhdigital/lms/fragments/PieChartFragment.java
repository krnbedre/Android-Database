package com.dhdigital.lms.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.MonthWiseLeave;
import com.dhdigital.lms.util.AppConstants;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by admin on 11/10/17.
 */

public class PieChartFragment extends Fragment implements OnChartValueSelectedListener {


    private View view;
    private PieChart mChart1, mChart2;
    private TextView mChartTitle, mMonthSelectedTitle, mApprovedLeavesText, mRejectedLeavesText, mPendingLeavesText, mCancelledLeavesText;
    private LinearLayout mMonthDetailsContainer;

    private List<MonthWiseLeave> monthWiseLeaveList = new ArrayList<>();

    private String navigation = AppConstants.CUMULATIVE_LEAVE_CHART;

    public PieChartFragment() {

    }


    @Override
    public void onResume() {

        super.onResume();
        this.monthWiseLeaveList = getArguments().getParcelableArrayList("MONTHWISE_LEAVES");
        navigation = getArguments().getString(AppConstants.NAVIGATION, AppConstants.CUMULATIVE_LEAVE_CHART);
        switch (navigation) {
            case AppConstants.CUMULATIVE_LEAVE_CHART:
                setDataToCumulativeChart();
                break;
            case AppConstants.MONTH_LEAVE_CHART:
                setDataToChart();
                break;
        }

        mChart2.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.piec_chart_layout, container, false);
        this.monthWiseLeaveList = getArguments().getParcelableArrayList("MONTHWISE_LEAVES");
        initializeWidgets();
        initializeChart();

        return view;

    }

    private void initializeWidgets() {

        mMonthSelectedTitle = (TextView) view.findViewById(R.id.selected_month_title);
        mApprovedLeavesText = (TextView) view.findViewById(R.id.approved_value);
        mRejectedLeavesText = (TextView) view.findViewById(R.id.rejected_value);
        mCancelledLeavesText = (TextView) view.findViewById(R.id.cancelled_value);
        mPendingLeavesText = (TextView) view.findViewById(R.id.pending_value);
        mMonthDetailsContainer = (LinearLayout) view.findViewById(R.id.month_details_container);
        mMonthDetailsContainer.setVisibility(View.GONE);
    }

    private void initializeChart() {

        mChart1 = (PieChart) view.findViewById(R.id.chart1);
        mChart2 = (PieChart) view.findViewById(R.id.chart2);
        mChartTitle = (TextView) view.findViewById(R.id.chart_title);
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        mChartTitle.setText(mChartTitle.getText() + " " + year);
        mChart1.setUsePercentValues(true);
        mChart2.setUsePercentValues(false);
        mChart1.getDescription().setEnabled(false);
        mChart2.getDescription().setEnabled(false);
        mChart1.setExtraOffsets(5, 10, 5, 5);
        mChart2.setExtraOffsets(5, 10, 5, 5);

        mChart1.setDragDecelerationFrictionCoef(0.95f);
        mChart2.setDragDecelerationFrictionCoef(0.95f);

        //mChart.setCenterText(generateCenterSpannableText());

        mChart1.setDrawHoleEnabled(true);
        //mChart2.setDrawHoleEnabled(true);
        mChart1.setHoleColor(Color.WHITE);
        mChart2.setHoleColor(Color.WHITE);

        mChart1.setTransparentCircleColor(Color.WHITE);
        mChart2.setTransparentCircleColor(Color.WHITE);
        mChart1.setTransparentCircleAlpha(110);
        mChart2.setTransparentCircleAlpha(110);

        mChart1.setHoleRadius(58f);
        mChart2.setHoleRadius(58f);
        mChart1.setTransparentCircleRadius(61f);
        mChart2.setTransparentCircleRadius(61f);

        mChart1.setDrawCenterText(true);
        mChart2.setDrawCenterText(true);

        //mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart1.setRotationEnabled(false);
        mChart2.setRotationEnabled(false);
        mChart1.setHighlightPerTapEnabled(true);
        mChart2.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart1.setOnChartValueSelectedListener(this);

        setDataToChart();

        mChart1.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

    }

    private void setDataToChart() {

        Float mult = calculateRange();

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        if (monthWiseLeaveList.size() == 0) {

            view.findViewById(R.id.chart_empty_title).setVisibility(View.VISIBLE);
            mChart1.setVisibility(View.GONE);
        } else {

            view.findViewById(R.id.chart_empty_title).setVisibility(View.GONE);
            mChart1.setVisibility(View.VISIBLE);
            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.

            for (int i = 0; i < monthWiseLeaveList.size(); i++) {
                entries.add(new PieEntry(calculateLeavePercentagePerMonth(monthWiseLeaveList.get(i)),
                        monthWiseLeaveList.get(i).getMonth(),
                        getResources().getDrawable(R.drawable.calendar)));
            }

            PieDataSet dataSet = new PieDataSet(entries, "Month-wise Leaves Taken");

            dataSet.setDrawIcons(false);
            dataSet.setLabel("");

            mChart1.setUsePercentValues(true);
            dataSet.setSliceSpace(3f);
            dataSet.setIconsOffset(new MPPointF(0, 40));
            dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();


            for (int c : createPalleteForMonths())
                colors.add(c);


            dataSet.setColors(colors);
            //dataSet.setSelectionShift(0f);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.WHITE);
            // data.setValueTypeface(mTfLight);
            mChart1.setData(data);

            // undo all highlights
            mChart1.highlightValues(null);

            mChart1.invalidate();
            mChart2.invalidate();
            onNothingSelected();

        }

    }


    private void setDataToCumulativeChart() {

        Float mult = calculateRange();

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        float approved_leaves = 0.0f;
        float rejected_leaves = 0.0f;
        float pending_leaves = 0.0f;
        float cancelled_leaves = 0.0f;
        for (int i = 0; i < monthWiseLeaveList.size() ; i++) {
            approved_leaves = approved_leaves + monthWiseLeaveList.get(i).getApproved();
            rejected_leaves = rejected_leaves + monthWiseLeaveList.get(i).getRejected();
            pending_leaves = pending_leaves + monthWiseLeaveList.get(i).getPending();
            cancelled_leaves = cancelled_leaves + monthWiseLeaveList.get(i).getCancelled();
        }

        entries.add(new PieEntry(approved_leaves,
                AppConstants.APPROVED,
                getResources().getDrawable(R.drawable.calendar)));
        entries.add(new PieEntry(rejected_leaves,
                AppConstants.REJECTED,
                getResources().getDrawable(R.drawable.calendar)));
        entries.add(new PieEntry(pending_leaves,
                AppConstants.PENDING,
                getResources().getDrawable(R.drawable.calendar)));
        entries.add(new PieEntry(cancelled_leaves,
                AppConstants.CANCELLED,
                getResources().getDrawable(R.drawable.calendar)));

        if (monthWiseLeaveList.size() == 0) {
            mChart1.setNoDataText("No leaves taken");
            //mChart1.setNoDataTextColor(getActivity().getColor(R.color.text_color_primary));
            MonthWiseLeave parcelValues = new MonthWiseLeave();
            parcelValues.setMonth(null);

        }

        PieDataSet dataSet = new PieDataSet(entries, "Month-wise Leaves Taken");

        dataSet.setDrawIcons(false);
        dataSet.setLabel("");
        mChart1.setUsePercentValues(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int c : createPalleteForMonths())
            colors.add(c);


        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        // data.setValueTypeface(mTfLight);
        mChart1.setData(data);

        // undo all highlights
        mChart1.highlightValues(null);

        mChart1.invalidate();
        mChart2.invalidate();
    }


    private Float calculateRange() {

        Float sum = 0.0f;
        for (int i=0 ;i < monthWiseLeaveList.size(); i++){
           sum = sum + monthWiseLeaveList.get(i).getApproved() + monthWiseLeaveList.get(i).getPending();
        }
        return sum;
    }


    private Float calculateLeavePercentagePerMonth(MonthWiseLeave monthWiseLeave) {

        Float percentage = 0.0f;
        return  (monthWiseLeave.getApproved()+monthWiseLeave.getPending())/calculateRange();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        switch (navigation) {
            case AppConstants.CUMULATIVE_LEAVE_CHART:
                break;
            case AppConstants.MONTH_LEAVE_CHART:
                for (int i = 0; i < monthWiseLeaveList.size(); i++) {
                    if (monthWiseLeaveList.get(i).getMonth().equalsIgnoreCase(((PieEntry) e).getLabel())) {
                        setMonthData(monthWiseLeaveList.get(i));
                    }
                }
                break;
        }


    }


    private void setMonthData(MonthWiseLeave monthWiseLeave) {

        mMonthSelectedTitle.setText(monthWiseLeave.getMonth());
        mApprovedLeavesText.setText(String.valueOf(monthWiseLeave.getApproved()) + " Days");
        mRejectedLeavesText.setText(String.valueOf(monthWiseLeave.getRejected()) + " Days");
        mCancelledLeavesText.setText(String.valueOf(monthWiseLeave.getCancelled()) + " Days");
        mPendingLeavesText.setText(String.valueOf(monthWiseLeave.getPending()) + " Days");
        mMonthDetailsContainer.setVisibility(View.VISIBLE);

    }


    private void setDataToChart2(MonthWiseLeave monthWiseLeave) {

        //mChart2.setVisibility(View.VISIBLE);
        mChart2.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.


        if (monthWiseLeave.getMonth() != null) {

            if (monthWiseLeave.getApproved() != 0) {
                entries.add(new PieEntry(monthWiseLeave.getApproved(),
                        "Approved",
                        getResources().getDrawable(R.drawable.calendar)));
            }
            if (monthWiseLeave.getPending() != 0) {
                entries.add(new PieEntry(monthWiseLeave.getPending(),
                        "Pending",
                        getResources().getDrawable(R.drawable.calendar)));
            }
            if (monthWiseLeave.getCancelled() != 0) {
                entries.add(new PieEntry(monthWiseLeave.getCancelled(),
                        "Cancelled",
                        getResources().getDrawable(R.drawable.calendar)));
            }
            if (monthWiseLeave.getRejected() != 0) {
                entries.add(new PieEntry(monthWiseLeave.getRejected(),
                        "Rejected",
                        getResources().getDrawable(R.drawable.calendar)));
            }


            mChart2.setCenterText(monthWiseLeave.getMonth());
        }
        mChart2.setCenterTextSize(14f);


        PieDataSet dataSet = new PieDataSet(entries, "Month-wise Leaves Taken");

        dataSet.setDrawIcons(false);
        dataSet.setLabel("");

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);


        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);

        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        // data.setValueTypeface(mTfLight);
        mChart2.setData(data);
        mChart2.setUsePercentValues(false);

        // undo all highlights
        mChart2.highlightValues(null);

        mChart2.invalidate();
    }

    @Override
    public void onNothingSelected() {

        mMonthDetailsContainer.setVisibility(View.GONE);
    }


    private int[] createPalleteForMonths() {

        int[] LIBERTY_COLORS = {
                Color.rgb(120, 144, 156),//Jan - Blue Grey
                Color.rgb(77, 208, 225),//Feb  - Sky Blue
                Color.rgb(117, 117, 117),//Mar - Grey
                Color.rgb(251, 141, 0), //April - Orange
                Color.rgb(128, 203, 196),//May - Teal Blue Green
                Color.rgb(206, 147, 216), //June - Purple
                Color.rgb(255, 112, 67),//July - Red
                Color.rgb(220, 231, 117), //Aug -
                Color.rgb(69, 39, 160), //Sept -
                Color.rgb(255, 213, 79), //Oct - yellow
                Color.rgb(128, 222, 234),//Nov - Blue
                Color.rgb(248, 187, 208)// Dec - Pink Material light
        };

        return LIBERTY_COLORS;
    }


    //bhaskharr9@gmail.com
    //5L#qL2n%
    //259619

}
