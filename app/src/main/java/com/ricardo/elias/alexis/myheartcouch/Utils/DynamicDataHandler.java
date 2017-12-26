package com.ricardo.elias.alexis.myheartcouch.Utils;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants;
import com.ricardo.elias.alexis.myheartcouch.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alexis on 2/5/2017.
 * <p>
 * Segun la documentacion para agregar nuevos a valores a la gráfica
 * YourData[] dataObjects = ...;
 * List<Entry> entries = new ArrayList<Entry>();
 * for (YourData data : dataObjects) {
 * turn your data into Entry objects
 * entries.add(new Entry(data.getValueX(), data.getValueY())); ]
 * <p>
 * DataSet objects hold data which belongs together, and allow individual styling of that data.
 * <p>
 * LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
 * dataSet.setColor(...);
 * dataSet.setValueTextColor(...); // styling, ...
 * }
 * <p>
 * Segun la documentacion para agregar nuevos a valores a la gráfica
 * YourData[] dataObjects = ...;
 * List<Entry> entries = new ArrayList<Entry>();
 * for (YourData data : dataObjects) {
 * turn your data into Entry objects
 * entries.add(new Entry(data.getValueX(), data.getValueY())); ]
 * <p>
 * DataSet objects hold data which belongs together, and allow individual styling of that data.
 * <p>
 * LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
 * dataSet.setColor(...);
 * dataSet.setValueTextColor(...); // styling, ...
 * }
 */

/**
 * Segun la documentacion para agregar nuevos a valores a la gráfica
 YourData[] dataObjects = ...;
 List<Entry> entries = new ArrayList<Entry>();
 for (YourData data : dataObjects) {
 turn your data into Entry objects
 entries.add(new Entry(data.getValueX(), data.getValueY())); ]

 DataSet objects hold data which belongs together, and allow individual styling of that data.

 LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
 dataSet.setColor(...);
 dataSet.setValueTextColor(...); // styling, ...
 }
 */

/**
 * Esta clase se encarga de manejar los datos de entradad y graficarlos
 */
public class DynamicDataHandler implements AxisValueFormatter {

    private LineChart mLineChart;
    private LineDataSet mLineDataSet;
    private LineData mLineData;
    private ArrayList<Float> mYValues;
    private String mXformat;
    private float mMaxXRange;
    private XLabelManager mXLabelManager;
    private int mMode;
    public static final int MODE_SAMPLES = 0;
    public static final int MODE_SECONDS = 1;

    /**
     * COnstructor de esta clase
     * Se inicializa el LineCHart
     * Se inicializan los datos de entrada a nulos
     *
     * @param chart Requiere un LineChart que provendrá en donde se contenga la interfaz
     *              de usuaio
     */
    public DynamicDataHandler(LineChart chart,int mode) {
        mLineChart = chart;
        mYValues = new ArrayList<>();
        mLineDataSet = createSet();
        mLineData = new LineData(mLineDataSet);
        mLineChart.setData(mLineData);
        mXformat = "";
        mMaxXRange = 300.0f;
        mXLabelManager = new XLabelManager();
        mMode = mode;
        if(mMode == MODE_SECONDS){mXLabelManager.onResume();}
        setStyle();

    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return "2";
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }

    /**
     * Se crea un estilo de fondo para
     * l plano cartesiano como los colores de este
     * Se inicializa un label a que diga Electrocardiograma
     */
    private void setStyle() {
        mLineDataSet.setColor(R.color.colorPrimary);
        mLineChart.invalidate();
        mLineChart.setDescription("ECG");
        mLineChart.setDrawGridBackground(true);
        mLineChart.setBorderColor(R.color.colorPrimary);
        mLineChart.setDrawBorders(false);
        mLineChart.getAxisLeft().setAxisMaxValue(6);
        mLineChart.getAxisLeft().setAxisMinValue(-6);
        /**
         * List<ILineDataSet> sets = mLineChart.getData().getDataSets();
         for (ILineDataSet iSet : sets) {
         LineDataSet set = (LineDataSet) iSet;
         set.setDrawCircles(false);
         set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
         //set.setDrawFilled(true);
         set.setDrawValues(false);
         }
         *
         */
        //Hace que los datos se refresquen y que sea graficados los datos
        mLineChart.invalidate();
    }

    /**
     * Se llama cuando recién se crea la gráfica
     * Crea y customiza una serie de  nuevos puntos agregándole colores, etc     *
     * @return un objeto del tipo linedata set con colores customizados , ademas
     * de su transparenia, etc
     */
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "ECG");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(R.color.black);
        //set.setFillColor(R.color.red);
        //set.setCircleColor(Color.WHITE);
        set.setLineWidth(3f);
        //set.setCircleRadius(4f);
        set.setFillAlpha(65);
        int red = Color.rgb(175, 36, 36);
        //set.setFillColor(red);
        set.setDrawFilled(false);
        set.setHighLightColor(R.color.colorPrimary);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        //set.setValueTextColor(Color.WHITE);
        //set.setValueTextSize(9f);

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
        set.setDrawValues(false);
        return set;
    }

    public ArrayList<Float> getYValues() {
        return mYValues;
    }

    public void addValueToGraphic(float y_value) {
        //Pregunta si hay datos disponibles
        LineData data = mLineChart.getData();
        mYValues.add(y_value);
        //Si ya hay datos
        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            //agrega un nuevo valor con la coordenada x del tiempo moviéndose
            //y la coordenada y como parámetró en la posición 0
            data.addEntry(new Entry(set.getEntryCount(), y_value), 0);
            data.notifyDataChanged();
            mLineChart.notifyDataSetChanged();
            mLineChart.setVisibleXRangeMaximum(mMaxXRange);
            mLineChart.moveViewToX(data.getEntryCount());
        }
    }

    public void setXformat(String xformat) {
        mXformat = xformat;
    }

    public void setMaxXRange(float maxXRange) {
        mMaxXRange = maxXRange;
    }

    public class XLabelManager {
        private int mSeconds;
        Timer timer;

        public XLabelManager() {
            timer = new Timer();
            mSeconds = 0;
        }

        public void onResume(){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mSeconds += 1;
                    Log.i(DBConstants.APP_TAG, "segundos: " + mSeconds + "");
                }
            }, 0, 1000);//Update text every second
        }
        public void onPause(){
            timer.cancel();
        }

    }
}
