package com.ricardo.elias.alexis.myheartcouch.Utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ricardo.elias.alexis.myheartcouch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexis on 7/9/2017.
 */

public class LineDataHandler implements AxisValueFormatter{

    private LineChart mLineChart;
    private LineDataSet mLineDataSet;
    private LineData mLineData;
    private ArrayList<Float> mData;
    private float mMaximumRange = 300;

    public LineDataHandler(LineChart lineChart){
        mLineChart = lineChart;
        mLineDataSet = null;
        mLineData = null;
    }

    /**
     * Crea y customiza una serie de  nuevos puntos agregándole colores, etc     *
     * @return un objeto del tipo linedata set con colores customizados , ademas
     * de su transparenia, etc
     */
    private LineDataSet createSet(LineDataSet set) {
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(R.color.black);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        int red = Color.rgb(175, 36, 36);
        set.setFillColor(red);
        set.setHighLightColor(R.color.colorPrimary);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);

        return set;
    }

    /**
     * Se crea un estilo de fondo para el plano cartesiano como los colores de este
     * Se inicializa un label a que diga Electrocardiograma
     */
    public void setStyle() {
        mLineDataSet.setColor(R.color.colorPrimary);
        mLineChart.setDescription("ECG");
        mLineChart.setDrawGridBackground(true);
        mLineChart.setBorderColor(R.color.colorPrimary);
        mLineChart.setDrawBorders(false);
        List<ILineDataSet> sets = mLineChart.getData().getDataSets();
        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            set.setDrawCircles(false);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //set.setDrawFilled(true);
            set.setDrawValues(false);
        }
        //Hace que los datos se refresquen y que sea graficados los datos
        mLineChart.setVisibleXRangeMaximum(mMaximumRange);
        mLineChart.getAxisLeft().setAxisMaxValue(6);
        mLineChart.getAxisLeft().setAxisMinValue(-6);
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });
        YAxis yAxisRight = mLineChart.getAxisRight();
        yAxisRight.setEnabled(false);


        mLineChart.invalidate();


    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return null;
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }

    public void setData(ArrayList<Float> data) {
        mData = data;
        if (mData != null){
            ArrayList<Entry> entries = new ArrayList<>();
            int i = 0;
            for (float x : mData){
                Entry entry = new Entry(i,x);
                entries.add(entry);
                i += 1;
            }
            mLineDataSet = new LineDataSet(entries,"daks");
            mLineDataSet.setColor(R.color.colorPrimary);
            mLineChart.setDrawBorders(false);
            mLineChart.setVisibleXRangeMaximum(800);
            mLineChart.setBorderColor(R.color.colorPrimary);
            mLineData = new LineData(mLineDataSet);
            mLineChart.setData(mLineData);
            mLineChart.getAxisLeft().setAxisMaxValue(5);
            mLineChart.getAxisLeft().setAxisMinValue(-5);
            mLineChart.notifyDataSetChanged();

            setStyle();
        }
    }

    public void setMaximumRange(float maximumRange) {
        mMaximumRange = maximumRange;
    }
}
