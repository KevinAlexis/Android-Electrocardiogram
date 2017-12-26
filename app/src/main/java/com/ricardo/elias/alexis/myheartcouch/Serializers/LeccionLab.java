package com.ricardo.elias.alexis.myheartcouch.Serializers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ricardo.elias.alexis.myheartcouch.LessonsFragments.Lesson1.L1_p2;
import com.ricardo.elias.alexis.myheartcouch.LessonsFragments.Lesson1.PageOne;
import com.ricardo.elias.alexis.myheartcouch.Model.Leccion;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alexis on 3/9/2017.
 */

public class LeccionLab implements Serializable {

    private ArrayList<Leccion> mLeccions;
    private MyLessonSeriaizer mSeriaizer;
    private static final String FILENAME="MYFILE";

    public LeccionLab(Context context){
        mSeriaizer = new MyLessonSeriaizer(FILENAME,context);
        try {
            Log.i("Serialize","Loaded");
            mLeccions = mSeriaizer.loadList();
            Log.i("Size of loaded lesso",mLeccions.size()+"");
            addLessonOne();
        } catch (Exception e){
            Log.i("Serialize","General");
            mLeccions = new ArrayList<>();
            generateLessonOne();
            geerateLessonTwo();
        }
    }

    private void generateLessonOne(){
        Leccion leccion1 = new Leccion();
        leccion1.setTitulo("Introducción");
        Fragment pageOne = new PageOne();
        Fragment pageTwo = new L1_p2();
        leccion1.getFragmentsPaginas().add(pageOne);
        leccion1.getFragmentsPaginas().add(pageTwo);
        mLeccions.add(0,leccion1);
    }

    private void addLessonOne(){

        Fragment pageOne = new PageOne();
        Fragment pageTwo = new L1_p2();
        mLeccions.get(0).getFragmentsPaginas().add(pageOne);
        mLeccions.get(0).getFragmentsPaginas().add(pageTwo);
    }

    public void geerateLessonTwo(){
        Leccion leccion = new Leccion();
        leccion.setTitulo("Electrodos");
        mLeccions.add(leccion);}


    public MyLessonSeriaizer getSeriaizer() {
        return mSeriaizer;
    }
    public ArrayList<Leccion> getLeccions() {
        return mLeccions;
    }
}
