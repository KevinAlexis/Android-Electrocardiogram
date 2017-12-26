package com.ricardo.elias.alexis.myheartcouch.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.ricardo.elias.alexis.myheartcouch.DataBase.DBWaveAdapter;
import com.ricardo.elias.alexis.myheartcouch.Model.Wave;
import com.ricardo.elias.alexis.myheartcouch.R;
import com.ricardo.elias.alexis.myheartcouch.Utils.LineDataHandler;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLoadWave extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private LineChart mLineChart;


    private Wave mWave;
    private int mIdWave;
    private LineDataHandler mDataHandler;

    private DBWaveAdapter mDBWaveAdapter;
    private SeekBar mSeekBar;
    public static final String KEY_WAVE_ID = "key";

    public FragmentLoadWave() {
    }

    public static FragmentLoadWave newInstance(int idWave) {
        Bundle args = new Bundle();
        FragmentLoadWave fragment = new FragmentLoadWave();
        args.putInt(KEY_WAVE_ID, idWave);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_load_wave, container, false);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mIdWave = getArguments().getInt(KEY_WAVE_ID);
        mLineChart = (LineChart) view.findViewById(R.id.linechart_ecg_loaded);
        mDBWaveAdapter = DBWaveAdapter.getInstance(getActivity());

        mWave = mDBWaveAdapter.getWaveWithValues(mIdWave);

        mDataHandler = new LineDataHandler(mLineChart);
        ArrayList<Float> list = mWave.getValues();
        mDataHandler.setData(list);
        mSeekBar.setOnSeekBarChangeListener(this);
        return view;
    }

    private void printValues() {
        ArrayList<Float> list = mWave.getValues();
        for (float x : list) {
            Log.i("values", x + "");
        }
        ArrayList<Entry> entries = new ArrayList<>();
        for (float x : list) {
            Entry entry = new Entry(x, x);
            entries.add(entry);
        }
        LineDataSet set = new LineDataSet(entries, "daks");
        LineData lineData = new LineData(set);
        mLineChart.notifyDataSetChanged();
    }

    //<editor-fold desc="Seek Bar Implementation">
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        Log.i("seekBar changed", i + "");
        mDataHandler.setMaximumRange((float) i * 3);
        mLineChart.setVisibleYRangeMaximum((float) i, YAxis.AxisDependency.LEFT);
        //ArrayList<Float> list = mWave.getValues();
        //mDataHandler.setData(list);
        //mLineChart.setVisibleXRangeMaximum((float) i);
        //mLineChart.notifyDataSetChanged();
        //mLineChart.invalidate();

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mDataHandler.setStyle();
        mLineChart.notifyDataSetChanged();
    }
    //</editor-fold>
}
