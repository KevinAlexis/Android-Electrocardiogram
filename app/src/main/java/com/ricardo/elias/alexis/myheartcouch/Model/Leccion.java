package com.ricardo.elias.alexis.myheartcouch.Model;

import android.support.v4.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Alexis on 3/9/2017.
 */

public class Leccion implements Serializable {

    //region Constants
    private static final String KEY_FRAGMETS = "keyFragments";
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_ISCOMPLETED = "IsCompleted";
    //endregion

    private ArrayList<Fragment> mFragmentsPaginas;
    private String mTitulo;
    private Boolean misCompleted = false;


    //region Construtores
    public Leccion() {
        mFragmentsPaginas = new ArrayList<>();
        mTitulo = "";
        misCompleted = false;
    }

    public Leccion(JSONObject jsonObject) throws JSONException {
        mTitulo = jsonObject.getString(KEY_TITULO);
        misCompleted = jsonObject.getBoolean(KEY_ISCOMPLETED);
        mFragmentsPaginas = new ArrayList<>();
    }
    //endregion

    /**
     * Transforms this oject to json Format
     * @return JsonObject
     * @throws JSONException
     */
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(KEY_TITULO,mTitulo);
        jsonObject.put(KEY_ISCOMPLETED,misCompleted);
        return jsonObject;
    }



    //region Getters Setters
    public ArrayList<Fragment> getFragmentsPaginas() {
        return mFragmentsPaginas;
    }

    public void setFragmentsPaginas(ArrayList<Fragment> fragmentsPaginas) {
        mFragmentsPaginas = fragmentsPaginas;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public void setTitulo(String titulo) {
        mTitulo = titulo;
    }

    public Boolean getMisCompleted() {
        return misCompleted;
    }

    public void setMisCompleted(Boolean misCompleted) {
        this.misCompleted = misCompleted;
    }
    //endregion


}
