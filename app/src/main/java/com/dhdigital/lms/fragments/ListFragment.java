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

import com.dhdigital.lms.R;
import com.dhdigital.lms.activities.LeaveDetailsActivity;
import com.dhdigital.lms.adapters.MyLeavesRecyclerAdapter;
import com.dhdigital.lms.modal.GlobalData;
import com.dhdigital.lms.modal.LeaveModal;
import com.dhdigital.lms.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 06/10/17.
 */

public class ListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    private View view;
    private String title;
    private ArrayList<LeaveModal> leavesList = new ArrayList<>();

    private static RecyclerView recyclerView;

    public ListFragment() {
    }

    public ListFragment(String title,ArrayList<LeaveModal> list) {
        this.title = title;//Setting tab title
        this.leavesList = list;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.list_fragment_layout, container, false);

        setRecyclerView();
        return view;

    }
    //Setting recycler view
    private void setRecyclerView() {

        recyclerView = (RecyclerView) view
                .findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity()));//Linear Items

        MyLeavesRecyclerAdapter adapter = new MyLeavesRecyclerAdapter(getActivity(), leavesList);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),this));


    }

    @Override
    public void onItemClick(View childView, int position) {
        LeaveModal leaveModal = leavesList.get(position);
        if (null != leaveModal) {
            GlobalData.gLeaveModal = leaveModal;
            Intent intent = new Intent(getActivity(), LeaveDetailsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongPress(View childView, int position) {

    }
}
