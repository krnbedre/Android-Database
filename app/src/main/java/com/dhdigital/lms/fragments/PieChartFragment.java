package com.dhdigital.lms.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.MonthWiseLeave;
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
    private TextView mChartTitle;

    private List<MonthWiseLeave> monthWiseLeaveList = new ArrayList<>();

    public PieChartFragment() {

    }


    @Override
    public void onResume() {

        super.onResume();
        this.monthWiseLeaveList = getArguments().getParcelableArrayList("MONTHWISE_LEAVES");
        setDataToChart();
        mChart2.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.piec_chart_layout, container, false);
        this.monthWiseLeaveList = getArguments().getParcelableArrayList("MONTHWISE_LEAVES");
        initializeChart();

        return view;

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

        mChart1.setDrawHoleEnabled(false);
        mChart2.setDrawHoleEnabled(true);
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

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < monthWiseLeaveList.size() ; i++) {
            entries.add(new PieEntry(calculateLeavePercentagePerMonth(monthWiseLeaveList.get(i)),
                    monthWiseLeaveList.get(i).getMonth(),
                    getResources().getDrawable(R.drawable.calendar)));
        }

        if (monthWiseLeaveList.size() == 0) {
            mChart1.setNoDataText("No leaves taken");
            mChart1.setNoDataTextColor(getActivity().getColor(R.color.text_color_primary));
            MonthWiseLeave parcelValues = new MonthWiseLeave();
            parcelValues.setMonth(null);

            setDataToChart2(parcelValues);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Month-wise Leaves Taken");

        dataSet.setDrawIcons(false);
        dataSet.setLabel("");

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int c : ColorTemplate.MATERIAL_COLORS)
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

        //PieEntry entry = (PieEntry) e.getData();

        for (int i = 0; i < monthWiseLeaveList.size(); i++) {
            if (monthWiseLeaveList.get(i).getMonth().equalsIgnoreCase(((PieEntry) e).getLabel())) {
                setDataToChart2(monthWiseLeaveList.get(i));
            }
        }

    }


    private void setDataToChart2(MonthWiseLeave monthWiseLeave) {

        mChart2.setVisibility(View.VISIBLE);
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

    }
}
