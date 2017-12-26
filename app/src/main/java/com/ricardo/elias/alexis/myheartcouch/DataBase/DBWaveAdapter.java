package com.ricardo.elias.alexis.myheartcouch.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ricardo.elias.alexis.myheartcouch.Model.Wave;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import static com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants.TABLE_VALUES;
import static com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants.TABLE_WAVE;
import static com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants.VALUES_ID;
import static com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants.VALUES_WAVE_ID;
import static com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants.VALUE_MAGNITUD;
import static com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants.WAVE_DATE;
import static com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants.WAVE_ID;
import static com.ricardo.elias.alexis.myheartcouch.DataBase.DBConstants.WAVE_NAME;


/**
 * Created by Alexis on 7/7/2017.
 */

public class DBWaveAdapter {

    /*Unica Instancia*/
    private static DBWaveAdapter sInstance;
    private DBHelper mDBHelper;


    public DBWaveAdapter(Context context) {
        mDBHelper = new DBHelper(context);
    }

    public SQLiteDatabase getDB() {
        return mDBHelper.getWritableDatabase();
    }

    public static DBWaveAdapter getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBWaveAdapter(context);
        }
        return sInstance;
    }

    //region Waves Queries
    public int postNewWave(String name) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WAVE_NAME, name);
        int id = (int) database.insert(TABLE_WAVE, null, values);
        return id;
    }

    public ArrayList<Wave> getAllWaves() {
        ArrayList<Wave> waves = new ArrayList<>();
        String columns[] = {WAVE_ID, WAVE_DATE};
        Cursor c = getDB().query(TABLE_WAVE, columns, null, null, null, null, null);
        while (c.moveToNext()) {
            Wave wave = new Wave();
            wave.setIdWave(c.getInt(c.getColumnIndex(WAVE_ID)));
            //Date date  = new Date(c.getLong(c.getColumnIndex(WAVE_DATE)));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date = null;
            try {
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                date = dateFormat.parse(c.getString(c.getColumnIndex(WAVE_DATE)));
            }
            catch (ParseException e) {e.printStackTrace();}
            wave.setDate(date);
            waves.add(wave);
        }
        return waves;
    }

    public void deleteWave (int id ){
        SQLiteDatabase sqLiteDatabase = mDBHelper.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_WAVE,WAVE_ID + " = '" + id + "'",null);
    }
    //endregion

    //region Values Queries
    public void postNewvalue(float valor, int idWave) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VALUE_MAGNITUD, valor);
        values.put(VALUES_WAVE_ID, idWave);
        database.insert(TABLE_VALUES, null, values);
    }

    public Wave getWaveWithValues(int id) {
        Wave wave = new Wave();
        ArrayList<Float> values = new ArrayList<>();
        String columns[] = {VALUES_WAVE_ID, VALUE_MAGNITUD};
        Cursor c = getDB().query(TABLE_VALUES, columns, VALUES_WAVE_ID + " = '" + id + "'", null, null, null, null);
        while (c.moveToNext()) {
            wave.setIdWave(c.getInt(c.getColumnIndex(VALUES_WAVE_ID)));
            values.add(c.getFloat(c.getColumnIndex(VALUE_MAGNITUD)));
        }
        wave.setValues(values);
        return wave;
    }
    //endregion

    //region HelperClass
    private static class DBHelper extends SQLiteOpenHelper {
        private static final int DBVERSION = 1;
        private static final String DBNAME = "waves";

        public DBHelper(Context context) {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            final String WavesTable = "CREATE TABLE " + TABLE_WAVE + "(" +
                    WAVE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    WAVE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    WAVE_NAME + " VARCHAR(255));";

            final String ValuesWaveTable = "CREATE TABLE " + TABLE_VALUES + "(" +
                    VALUES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    VALUES_WAVE_ID + " INTEGER," +
                    VALUE_MAGNITUD + " DOUBLE);";
            sqLiteDatabase.execSQL(WavesTable);
            sqLiteDatabase.execSQL(ValuesWaveTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }
    //endregion
}
