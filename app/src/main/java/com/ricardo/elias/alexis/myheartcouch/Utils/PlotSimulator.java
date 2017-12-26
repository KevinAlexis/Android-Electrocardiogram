package com.ricardo.elias.alexis.myheartcouch.Utils;

import android.util.Log;

/**
 * Created by Alexis on 4/16/2017.
 */

public class PlotSimulator implements Runnable {

    private PlotThread mPlotThread;
    private float angle = 0f;
    private final Object mPauseLock;
    private Thread mThread;
    private boolean mPaused;
    private boolean mIsRunning;

    /**
     * Constructor
     *
     * @param plotThread
     */
    public PlotSimulator(PlotThread plotThread) {
        mPlotThread = plotThread;
        mThread = new Thread(this);
        mPauseLock = new Object();
        mPaused = false;
        mIsRunning = false;
    }

    @Override
    public void run() {
        while (!mIsRunning) {
            try {
                plotSine();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (mPauseLock) {
                while (mPaused)
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                    }
            }
        }
    }

    /**
     * Plot a sawtooth Function
     *
     * @throws InterruptedException
     */
    private void SawTooth() throws InterruptedException {
        float increment = (float) (2 * Math.PI) * 1000 / 44100;
        for (int i = 0; i < 1000; i++) {
            float mathValue = (float) ((2 * ((angle / (2 * Math.PI)) - Math.floor(0.5 + (angle / (2 * Math.PI))))));
            mPlotThread.setADC(mathValue);
            angle += increment;
            Thread.sleep(100);
        }
        angle = 0;
    }

    /**
     * Plot a sine Function yheaaaaa!!!!!
     *
     * @throws InterruptedException
     */
    private void plotSine() throws InterruptedException {
        for (float i = 0.0f; i < 10; i += 0.1) {
            float mathValue = (float) Math.sin(i) * 3;
            Log.i("plotting", mathValue + "");
            mPlotThread.setADC(mathValue);
            Thread.sleep(10);
        }
    }

    /**
     * Plot a sine function
     */
    private void plotSineWithFrecuecy() throws InterruptedException {
        float j = 0.0f;
        for (int i = 0; i < 100; i++) {
            float value = (float) (Math.sin(2 * j));
            mPlotThread.setADC(value);
            Thread.sleep(100);
            j += 0.1;
        }
    }

    /**
     * Plot Random Data
     *
     * @throws InterruptedException
     */
    private void randomData() {
        double random = 0f;
        random = (Math.random() + 100) * 10;
        if (!mPaused) {
            mPlotThread.addValueToBuffer((float) random);
        }
    }

    public void onCreate() {
        mThread.start();
    }

    public Thread getThread() {
        return mThread;
    }

    public void onPause() {
        synchronized (mPauseLock) {
            mPaused = true;
        }
    }
}
