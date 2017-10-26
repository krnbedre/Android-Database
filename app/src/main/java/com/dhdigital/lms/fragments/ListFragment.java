package com.dhdigital.lms.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhdigital.lms.R;
import com.dhdigital.lms.activities.LeaveDetailsActivity;
import com.dhdigital.lms.adapters.MyLeavesRecyclerAdapter;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.util.AppConstants;
import com.dhdigital.lms.util.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by admin on 06/10/17.
 */

public class ListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    private static RecyclerView recyclerView;
    private TextView mEmptytextView;
    private View view;
    private String title;
    private ArrayList<LeaveModal> leavesList = new ArrayList<>();

    public ListFragment() {
    }


    @Override
    public void onResume() {
        super.onResume();
        this.leavesList = getArguments().getParcelableArrayList(AppConstants.LEAVES_FRAGMENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_fragment_layout, container, false);
        this.leavesList = getArguments().getParcelableArrayList(AppConstants.LEAVES_FRAGMENT);
        setRecyclerView();
        return view;

    }

    //Setting recycler view
    private void setRecyclerView() {

        recyclerView = (RecyclerView) view
                .findViewById(R.id.recyclerView);
        mEmptytextView = (TextView) view.findViewById(R.id.empty);
        recyclerView.setHasFixedSize(true);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity()));//Linear Items

        MyLeavesRecyclerAdapter adapter = new MyLeavesRecyclerAdapter(getActivity(), leavesList);
        if (leavesList.size() > 0) {
            mEmptytextView.setVisibility(View.GONE);
        } else {
            mEmptytextView.setVisibility(View.VISIBLE);
        }
        recyclerView.setAdapter(adapter);// set adapter on recyclerview
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),this));



    }

    @Override
    public void onItemClick(View childView, int position) {
        LeaveModal leaveModal = leavesList.get(position);
        if (null != leaveModal) {
            GlobalData.gLeaveModal = leaveModal;
            Intent intent = new Intent(getActivity(), LeaveDetailsActivity.class);
            intent.putExtra(AppConstants.NAVIGATION, AppConstants.REQUESTOR);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
