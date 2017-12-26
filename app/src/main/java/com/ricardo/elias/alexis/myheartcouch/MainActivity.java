package com.ricardo.elias.alexis.myheartcouch;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ricardo.elias.alexis.myheartcouch.LessonsFragments.EscogerLeccionesFragment;
import com.ricardo.elias.alexis.myheartcouch.Fragments.FragmentECG;
import com.ricardo.elias.alexis.myheartcouch.Fragments.FragHistoriaList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mCurrentFragment;
    private final FragmentManager mFragmentManager = getSupportFragmentManager();
    private static final String TAG_FRAG_ECG = "fragEcG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = new FragmentECG();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_holder,fragment,TAG_FRAG_ECG);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_lecciones:
                mCurrentFragment = null;
                mCurrentFragment = new EscogerLeccionesFragment();
                break;
            case R.id.nav_electrocardiograma:
                mCurrentFragment = null;
                FragmentECG fragmentECG = (FragmentECG) mFragmentManager.findFragmentByTag(TAG_FRAG_ECG);
                if(fragmentECG != null){
                    mCurrentFragment = new FragmentECG();
                }else{
                    mCurrentFragment = null;
                    mCurrentFragment = new FragmentECG();
                }
                break;
            case R.id.nav_testing:
                mCurrentFragment = null;
                mCurrentFragment = new FragHistoriaList();
                break;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_holder, mCurrentFragment);
        transaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
