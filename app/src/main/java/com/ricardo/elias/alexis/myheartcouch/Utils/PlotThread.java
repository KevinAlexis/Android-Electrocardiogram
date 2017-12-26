package com.ricardo.elias.alexis.myheartcouch.Utils;

import android.content.Context;
import android.util.Log;

import com.ricardo.elias.alexis.myheartcouch.DataBase.DBWaveAdapter;

import java.util.ArrayList;

/**
 * Created by Alexis on 2/5/2017.
 */
public class PlotThread implements Runnable {

    //region Variables Miembro
    private static final String TAG = "AppTag";
    private Object mPauseLock;
    private Thread mThread;
    private boolean mIsRunning;
    private boolean mPaused;
    private DynamicDataHandler mDynamicDataHandler;
    private float mADC = 0;
    private int mIdWave;
    private ArrayList<Float> mBuffer;
    private DBWaveAdapter mDBWaveAdapter;

    //endregion

    /**
     * COnstructor que inicializa las variables para la creacion de un objeto de
     * este tipo
     *
     * @param dynamicDataHandler Se necesita un datahandler proveniente de la interfaz de usuario
     *                           en esta clase se referencía un objeto del mismo tipo como variable miembro    *
     */
    public PlotThread(DynamicDataHandler dynamicDataHandler, Context context) {
        mADC = 0;
        mDynamicDataHandler = dynamicDataHandler;
        mThread = new Thread(this);
        mIsRunning = false;
        mPaused = false;
        mPauseLock = new Object();
        mBuffer = new ArrayList<>();
        mIdWave = -1;
        mDBWaveAdapter = DBWaveAdapter.getInstance(context);
    }

    /**
     * Hace que se grafique con la función plotting , pero si el pauselock
     * se vuelve true , se pone este thread en pausa
     */
    @Override
    public void run() {
        while (!mIsRunning) {
            ploting();
            //addValueToBuffer(mADC);
            synchronized (mPauseLock) {
                while (mPaused)
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
            }
        }
    }

    //region Custom Functions

    /**
     * Grafica , cuenta hasta un número arbitrario , le pasa a quien se encarga de graficar los datos
     * el data handler el valor que ha recibido del bluetooth
     */
    public void ploting() {
        try {
            Thread.sleep(100);
            mDynamicDataHandler.addValueToGraphic(mADC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Ciclo de Vida

    /**
     * Inicializa el threead
     */
    public void onCreate() {
        mThread.start();
        Log.i(TAG, "OncreatedThread");
    }

    /**
     * Se encarga de pausar el thread actual
     */
    public void OnPause() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }

    /**
     * Si el thread está pausado hace que sea resumido
     */
    public void onResume() {
        synchronized (mPauseLock) {
            mPaused = false;
            mPauseLock.notifyAll();
        }
    }
    //endregion

    //region Getters Setter

    /**
     * Recive el valor del adc proveniente del ATMEGA48
     *
     * @param ADC es el valor de adc proveniente del microcontrolador
     */
    public void setADC(float ADC) {
        this.mADC = ADC;
    }

    /**
     * Setter del id a donde se introduciran los valores en la base de datos
     */
    public void setIdWave(int idWave) {
        mIdWave = idWave;
    }

    public void addValueToBuffer(float ADC) {
        mBuffer.add(ADC);
        if (mBuffer.size() == 10) {
            float adcPromedy = 0;
            for (float x : mBuffer) {
                adcPromedy += x;
            }
            adcPromedy = adcPromedy / mBuffer.size();
            Log.i("promedy", adcPromedy + "");
            this.setADC(adcPromedy);
            ploting();
            mBuffer.clear();
        }
    }
    //endregion

    public DynamicDataHandler getDynamicDataHandler() {
        return mDynamicDataHandler;
    }
}
