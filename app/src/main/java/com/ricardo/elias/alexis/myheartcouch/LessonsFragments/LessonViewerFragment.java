package com.ricardo.elias.alexis.myheartcouch.LessonsFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ricardo.elias.alexis.myheartcouch.Model.Leccion;
import com.ricardo.elias.alexis.myheartcouch.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LessonViewerFragment extends Fragment implements ViewPager.OnPageChangeListener {


    //region COnstates
    private static final String LECCION = "PAGES";
    //endregion

    //region Variables Miembro
    private Button mButton;
    private MyViewPagerAdapter mMyViewPagerAdapter;
    private Leccion mLeccion;

    //endregion

    //region ViewPager SetUp
    private ViewPager mViewPager;
    private LinearLayout mLinearDots;
    private ImageView[] mDots;
    private int mDotsCount;

    //endregion


    public static LessonViewerFragment newInstance(Leccion leccion) {
        Bundle args = new Bundle();
        LessonViewerFragment fragment = new LessonViewerFragment();
        args.putSerializable(LECCION, leccion);
        fragment.setArguments(args);
        return fragment;
    }

    public LessonViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_viewer, container, false);
        mButton = (Button) view.findViewById(R.id.button);
        mLinearDots = (LinearLayout) view.findViewById(R.id.linear_points);
        mLeccion = (Leccion) getArguments().getSerializable(LECCION);
        mMyViewPagerAdapter = new MyViewPagerAdapter(getFragmentManager(), mLeccion);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_lessonviewer);
        mViewPager.setAdapter(mMyViewPagerAdapter);
        mMyViewPagerAdapter.notifyDataSetChanged();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Testing", "Count" + mMyViewPagerAdapter.getCount() + "");
                Log.i("Testing", "Lessons" + mMyViewPagerAdapter.getLeccion().getFragmentsPaginas().size() + "");
                mViewPager.setAdapter(mMyViewPagerAdapter);
                mMyViewPagerAdapter.notifyDataSetChanged();
            }
        });
        mViewPager.addOnPageChangeListener(this);
        setRetainInstance(true);
        addDotsToViewPager();
        mLeccion.setMisCompleted(true);
        return view;
    }

    /**
     * Set Up ViewPagerWhithDots
     */
    private void addDotsToViewPager() {
        mDotsCount = mMyViewPagerAdapter.getCount();
        mDots = new ImageView[mDotsCount];
        if (mDotsCount != 0) {
            for (int j = 0; j < mDotsCount; j++) {
                mDots[j] = new ImageView(getActivity());
                mDots[j].setImageDrawable(getResources().getDrawable(R.drawable.empty_dot));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(4, 0, 4, 0);
                mLinearDots.addView(mDots[j], layoutParams);
            }
            mDots[0].setImageDrawable(getResources().getDrawable(R.drawable.full_dot));
        }
    }

    /**
     * ViewPagerAdapter
     */
    private class MyViewPagerAdapter extends FragmentPagerAdapter {

        Leccion mLeccion;

        public MyViewPagerAdapter(FragmentManager fm, Leccion leccion) {
            super(fm);
            mLeccion = leccion;
        }

        @Override
        public Fragment getItem(int position) {
            return mLeccion.getFragmentsPaginas().get(position);
        }

        @Override
        public int getCount() {
            return mLeccion.getFragmentsPaginas().size();
        }

        public Leccion getLeccion() {
            return mLeccion;
        }
    }

    //region Page Change Listener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        dotsChanged(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void dotsChanged(int inPosition) {
        if (mDotsCount != 0){
            for (int i=0; i<mDotsCount;i++){
                mDots[i].setImageDrawable(getResources().getDrawable(R.drawable.empty_dot));
            }
            mDots[inPosition].setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.full_dot,null));
        }

    }
    //endregion
}
