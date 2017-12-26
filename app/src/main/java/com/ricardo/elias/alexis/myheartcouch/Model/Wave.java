package com.ricardo.elias.alexis.myheartcouch.Model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alexis on 6/9/2017.
 * Esta clase contiene un arreglo de valores float para ser posteriormente serializados
 */

public class Wave {

    private ArrayList<Float> mValues;
    private Date mDate;
    private int mIdWave;

    public Wave() {
        mValues = new ArrayList<>();
        mDate = new Date();
    }

    //region getters and setters

    public ArrayList<Float> getValues() {
        return mValues;
    }

    public void setValues(ArrayList<Float> values) {
        mValues = values;
    }

    public int getIdWave() {
        return mIdWave;
    }

    public void setIdWave(int IDWave) {
        mIdWave = IDWave;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    //endregion

}
