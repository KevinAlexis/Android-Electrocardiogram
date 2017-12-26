package com.ricardo.elias.alexis.myheartcouch.Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.ricardo.elias.alexis.myheartcouch.Bluetooth.Bluetooth;
import com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants;
import com.ricardo.elias.alexis.myheartcouch.DataBase.DBWaveAdapter;
import com.ricardo.elias.alexis.myheartcouch.R;
import com.ricardo.elias.alexis.myheartcouch.Utils.DynamicDataHandler;
import com.ricardo.elias.alexis.myheartcouch.Utils.PlotSimulator;
import com.ricardo.elias.alexis.myheartcouch.Utils.PlotThread;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentECG extends Fragment implements View.OnClickListener {

    Bluetooth mBluetooth;
    private PlotThread mPlotThread;
    private DynamicDataHandler mDynamicDataHandler;
    private FloatingActionButton mFloatingActionButton;
    private DBWaveAdapter mDBWaveAdapter;
    private Boolean misPlaying;
    private double mSeconds = 0.0;
    private double mNumeroMuestras = 0.0;
    private int mCurrentId = -1;
    private ArrayList<Float> mValues;
    private static final String ECGTAG = "FragmentECG";
    private ProgressDialog mProgressDialog;
    PlotSimulator plotSimulator;
    public FragmentECG() {
    }

    //region Fragment Cycle and Menu


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * En este método inicializamos las variables miembro de UI además de las variables necesarias
     * para llevar a cabo la gráfica , inicializamos el bluetooth, etc
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ecg, container, false);
        misPlaying = false;

        mDBWaveAdapter = DBWaveAdapter.getInstance(getActivity());
        mValues = new ArrayList<>();
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.button_playbutton);
        final LineChart lineChartECG = (LineChart) view.findViewById(R.id.linechart_ecg);
        mFloatingActionButton.setOnClickListener(this);
        mBluetooth = new Bluetooth(mHandler);
        mDynamicDataHandler = new DynamicDataHandler(lineChartECG,DynamicDataHandler.MODE_SECONDS);
        mPlotThread = new PlotThread(mDynamicDataHandler, getActivity());
        setHasOptionsMenu(true);
        setRetainInstance(true);
        checkIfPlaying();
        plotSimulator = new PlotSimulator(mPlotThread);
        updateSeconds();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle("Espere porfavor.");
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.ecg_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_connect:
                connectService();
                Toast.makeText(getActivity(), "Tratando de conectarse", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_test:
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_playbutton:
                PlayPausa();
                break;
        }
    }
    //endregion

    //region Handler
    /**
     * Aqui inicializamos un Handler que se encarga de recibir los mensajes que vengan de
     * otro thread
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bluetooth.MESSAGE_READ:
                    if (misPlaying) {
                        final byte[] readBuf = (byte[]) msg.obj;
                        int received = readBuf[1] & 0xff;
                        float volts = (received * 5.0f / 255.0f) - 1.5f;
                        Log.i("Number Hex", received + "");
                        mValues.add(volts);
                        mDynamicDataHandler.addValueToGraphic(volts);
                    }
                    break;
                case Bluetooth.STATE_CONNECTED:
                    Toast.makeText(getActivity(), "Conectado", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Cambia el estad del boton play
     */
    public void checkIfPlaying() {
        if (misPlaying) {
            mFloatingActionButton.setImageResource(R.drawable.ic_pause);
        } else {
            mFloatingActionButton.setImageResource(R.drawable.ic_action_play);
        }
    }

    private void updateSeconds() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mSeconds += 1;
                if (mCurrentId != -1) {

                    Log.i("seconds", "segundos: " + mSeconds + "");
                    ArrayList<Float> yValues = mPlotThread.getDynamicDataHandler().getYValues();
                    Log.i("seconds", "grafica: " + yValues.size());
                    Log.i("seconds", "numero muestras: " + mNumeroMuestras);
                }

            }

        }, 0, 1000);//Update text every second
    }
    //endregion


    //region Custom Functions
    private char flag = 0;

    /**
     * Cada vez que se mande a llamar a esta funcion cambiará el estado del thread que
     * grafica, ya sea que se ponga en pausa o play
     */
    private void PlayPausa() {
        switch (flag) {
            case 0:
                int id = mDBWaveAdapter.postNewWave("No name");
                //mPlotThread.onCreate();
                //plotSimulator.onCreate();
                mCurrentId = id;
                misPlaying = true;
                checkIfPlaying();

                flag = 1;
                break;
            case 1:
                misPlaying = !misPlaying;
                //addValuesToDataBase(mCurrentId);
                checkIfPlaying();
                if (!misPlaying){
                    if (mCurrentId != -1){
                        mProgressDialog.show();
                        addValuesToDataBase(mCurrentId, new DialogCompleteInterface() {
                            @Override
                            public void onComplete(Boolean aBoolean) {
                                mProgressDialog.dismiss();
                            }
                        });
                    }
                }
                flag = 2;
                break;
            case 2:
                //mPlotThread.onResume();
                misPlaying = !misPlaying;
                checkIfPlaying();
                flag = 1;
                break;
        }
    }

    /**
     * Con este método activamos el bluetooth y hacemos que se conecte al
     * módulo HC-05
     */
    public void connectService() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                mBluetooth.start();
                mBluetooth.connectDevice("HC-05");
            } else {
            }
        } catch (Exception e) {
        }
    }

    /**
     * Agrega los valores a la base de datos , la condici{on es que primero se hallan almacenado los valores en
     * mValues
     *
     * @param idWave
     */
    public void addValuesToDataBase(final int idWave, final DialogCompleteInterface dialogCompleteInterface) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(DBConstants.APP_TAG,"numero total de valores" + mValues.size());
                for (float y : mValues) {
                    mDBWaveAdapter.postNewvalue(y, idWave);
                    dialogCompleteInterface.onComplete(true);
                }
            }
        });
        thread.start();
    }

    interface DialogCompleteInterface{
         void onComplete(Boolean aBoolean);
    }
    //endregion
}


