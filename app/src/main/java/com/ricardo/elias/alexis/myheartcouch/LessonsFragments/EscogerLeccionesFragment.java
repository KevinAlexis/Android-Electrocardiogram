package com.ricardo.elias.alexis.myheartcouch.LessonsFragments;


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

import com.ricardo.elias.alexis.myheartcouch.Adapters.LeccionesAdapter;
import com.ricardo.elias.alexis.myheartcouch.Listeners.MyOnclickListener;
import com.ricardo.elias.alexis.myheartcouch.Model.Leccion;
import com.ricardo.elias.alexis.myheartcouch.Serializers.LeccionLab;
import com.ricardo.elias.alexis.myheartcouch.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EscogerLeccionesFragment extends Fragment implements MyOnclickListener {

    private LeccionesAdapter mLeccionesAdapter;
    private RecyclerView mRecyclerView;
    private LeccionLab mLeccionLab;
    private ArrayList<Leccion> mLeccions;


    public EscogerLeccionesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mLeccionLab = new LeccionLab(getActivity());
        mLeccionesAdapter = new LeccionesAdapter(getActivity());
        mLeccions = mLeccionLab.getLeccions();
        mLeccionesAdapter.setLeccions(mLeccions);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_general);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mLeccionesAdapter);
        mLeccionesAdapter.setMyOnclickListener(this);
        return view;
    }

    @Override
    public void onItemClick(View view, int position, int tag) {
        Log.i("SomeTag", "Position" + position);

        Fragment fragment = LessonViewerFragment.newInstance(mLeccionLab.getLeccions().get(position));
        FragmentManager manager = getFragmentManager();
        mLeccions.get(position).setMisCompleted(true);
        try {
            mLeccionLab.getSeriaizer().saveLesson(mLeccions);
            Log.i("Serialize", "Saving Data");
        } catch (Exception e) {
            Log.i("Serialize", "Error savig");
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack("yes");
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.fragment_holder, fragment);
        transaction.commit();
    }

    @Override
    public void onLongItemClick(View view, int position) {
    }
}
