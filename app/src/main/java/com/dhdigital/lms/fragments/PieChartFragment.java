package com.dhdigital.lms.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhdigital.lms.R;
import com.dhdigital.lms.modal.LeaveModal;
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
import java.util.List;

/**
 * Created by admin on 11/10/17.
 */

public class PieChartFragment extends Fragment implements OnChartValueSelectedListener {


    private View view;
    private PieChart mChart;

    private List<MonthWiseLeave> monthWiseLeaveList = new ArrayList<>();

    protected String[] mMonths = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    protected Typeface mTfRegular;
    protected Typeface mTfLight;



    public PieChartFragment() {
        this.monthWiseLeaveList = getArguments().getParcelableArrayList("MONTHWISE_LEAVES");
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.piec_chart_layout, container, false);

        initializeChart();

        return view;

    }

    private void initializeChart() {

        mChart = (PieChart) view.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
        //mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(false);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        //mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setDataToChart();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
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
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
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

    }

    @Override
    public void onNothingSelected() {

    }
}
