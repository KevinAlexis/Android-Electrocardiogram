package com.ricardo.elias.alexis.myheartcouch.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ricardo.elias.alexis.myheartcouch.R;

/**
 * Created by Alexis on 8/14/2017.
 */

public class DialogConfimar extends DialogFragment {

    DialogInterface.OnClickListener mOnClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_layout,null);
        builder.setView(view);
        builder.setPositiveButton("Entiendo", mOnClickListener);
        return builder.create();
    }
}
