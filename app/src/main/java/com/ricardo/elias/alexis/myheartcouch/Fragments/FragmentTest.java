package com.ricardo.elias.alexis.myheartcouch.Fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.ricardo.elias.alexis.myheartcouch.R;
import com.ricardo.elias.alexis.myheartcouch.Utils.DynamicDataHandler;
import com.ricardo.elias.alexis.myheartcouch.Utils.PlotSimulator;
import com.ricardo.elias.alexis.myheartcouch.Utils.PlotThread;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTest extends Fragment implements View.OnClickListener{

    private LineChart mLineChart;
    private LineData mData;
    private FloatingActionButton mFloatingActionButton;
    private PlotThread mPlotThread;
    private DynamicDataHandler mDynamicDataHandler;
    private PlotSimulator mPlotSimulator;

    public FragmentTest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_test, container, false);
        mData = new LineData();
        mLineChart = (LineChart) view.findViewById(R.id.line_test);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_play_test);
        mFloatingActionButton.setOnClickListener(this);
        mDynamicDataHandler = new DynamicDataHandler(mLineChart,DynamicDataHandler.MODE_SECONDS);
        mPlotThread = new PlotThread(mDynamicDataHandler,getActivity());
        mPlotSimulator = new PlotSimulator(mPlotThread);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.floating_play_test:
                mPlotThread.onCreate();
                mPlotSimulator.onCreate();
                break;
        }
    }
}
