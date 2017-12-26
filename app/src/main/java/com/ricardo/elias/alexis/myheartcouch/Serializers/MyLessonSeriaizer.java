package com.ricardo.elias.alexis.myheartcouch.Serializers;

import android.content.Context;
import android.util.Log;

import com.ricardo.elias.alexis.myheartcouch.Model.Leccion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Alexis on 5/1/2017.
 */

public class MyLessonSeriaizer implements Serializable{

    private Context mContext;
    private String mFileName;

    public MyLessonSeriaizer(String fileName, Context context) {
        mFileName = fileName;
        mContext = context;
    }

    public void saveLesson(ArrayList<Leccion> lessonList) throws JSONException, IOException {
        JSONArray array = new JSONArray();
        for (Leccion lesson : lessonList) {
            array.put(lesson.toJson());
        }
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<Leccion> loadList() throws IOException, JSONException {
        BufferedReader reader = null;
        ArrayList<Leccion> arrayList = new ArrayList<>();
        try {
            InputStream inputStream = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jSonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jSonString.append(line);
            }
            JSONArray array = (JSONArray) new JSONTokener(jSonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++) {
                arrayList.add(new Leccion(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException error) {
            Log.i("Error", "File Not Foud");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return arrayList;
    }


}
