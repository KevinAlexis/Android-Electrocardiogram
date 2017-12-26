package com.ricardo.elias.alexis.myheartcouch.LessonsFragments.Lesson1;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ricardo.elias.alexis.myheartcouch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageOne extends Fragment {

    public PageOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_one, container, false);
        return view;
    }

}
