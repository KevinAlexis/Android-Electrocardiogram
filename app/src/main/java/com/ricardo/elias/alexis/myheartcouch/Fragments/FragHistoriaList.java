package com.ricardo.elias.alexis.myheartcouch.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ricardo.elias.alexis.myheartcouch.Adapters.WavesAdapter;
import com.ricardo.elias.alexis.myheartcouch.DataBase.DBWaveAdapter;
import com.ricardo.elias.alexis.myheartcouch.Listeners.MyOnclickListener;
import com.ricardo.elias.alexis.myheartcouch.Model.Wave;
import com.ricardo.elias.alexis.myheartcouch.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragHistoriaList extends Fragment implements MyOnclickListener {

    private DBWaveAdapter mDBWaveAdapter;
    private RecyclerView mRecyclerWaves;
    private WavesAdapter mAdapter;
    private FragmentManager mManager;
    private ArrayList<Wave> mWaves;

    public FragHistoriaList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        mDBWaveAdapter = DBWaveAdapter.getInstance(getActivity());
        mWaves = mDBWaveAdapter.getAllWaves();
        mAdapter = new WavesAdapter(getActivity(), mWaves);
        mAdapter.setMyOnclickListener(this);
        mRecyclerWaves = (RecyclerView) view.findViewById(R.id.recycler_waves);
        mRecyclerWaves.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerWaves.setAdapter(mAdapter);
        return view;
    }

    //region Custom Functions
    private void generateDummyData() {
        for (int i = 0; i < 1500; i++) {
            mDBWaveAdapter.postNewvalue((float) i, 1);
        }
    }
    //endregion

    //region MyOnclick Delegate


    @Override
    public void onItemClick(View view, int position, int tag) {
        if (tag == 0) {
            Wave wave = mWaves.get(position);
            Fragment fragment = FragmentLoadWave.newInstance(wave.getIdWave());
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_holder, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (tag == 1) {
            DialogConfimar dialogConfimar = new DialogConfimar();
            dialogConfimar.show(getActivity().getFragmentManager(), "");
            for (Wave wave : mWaves) {
                Wave selectedWave = mWaves.get(position);
                if (wave.getIdWave() == selectedWave.getIdWave()) {
                    Log.i("Selecting Wave", "Some message");
                }
            }
            mWaves.remove(position);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLongItemClick(View view, int position) {
    }
    //endregion
}
