package com.ricardo.elias.alexis.myheartcouch.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ricardo.elias.alexis.myheartcouch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment implements View.OnClickListener{

    private Button mButtonContinuar;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        mButtonContinuar = (Button) view.findViewById(R.id.button_continuar);
        mButtonContinuar.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_continuar:
                FragmentManager manager=getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Fragment fragment =new FragmentECG();
                transaction.replace(R.id.fragment_holder,fragment);
                transaction.commit();
                break;
        }
    }
}
