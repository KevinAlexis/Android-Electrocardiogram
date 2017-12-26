package com.ricardo.elias.alexis.myheartcouch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ricardo.elias.alexis.myheartcouch.DataBase.DBWaveAdapter;
import com.ricardo.elias.alexis.myheartcouch.Listeners.MyOnclickListener;
import com.ricardo.elias.alexis.myheartcouch.Model.Wave;
import com.ricardo.elias.alexis.myheartcouch.R;

import java.util.ArrayList;

/**
 * Created by Alexis on 6/9/2017.
 */

public class WavesAdapter extends RecyclerView.Adapter<WavesAdapter.MyViewHolder> {

    private ArrayList<Wave> mWaves;
    private Context mContext;
    private MyOnclickListener mMyOnclickListener;
    private DBWaveAdapter mDBWaveAdapter;

    public WavesAdapter(Context context, ArrayList<Wave> waves) {
        mContext = context;
        mMyOnclickListener = null;
        mWaves = waves;
        mDBWaveAdapter = DBWaveAdapter.getInstance(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_waves, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Wave wave = mWaves.get(position);
        String text = "Electro #";
        holder.mTextNombre.setText(text + wave.getIdWave());
        holder.mTextFecha.setText(wave.getDate().toString());

        holder.mButtonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MyTag", "click Eliminar");
                mMyOnclickListener.onItemClick(holder.mButtonEliminar, position, 1);
                mDBWaveAdapter.deleteWave(wave.getIdWave());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mWaves.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTextNombre;
        private final TextView mTextFecha;
        private Button mButtonEliminar;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextFecha = (TextView) itemView.findViewById(R.id.text_waveDate);
            mTextNombre = (TextView) itemView.findViewById(R.id.text_Wavename);
            mButtonEliminar = (Button) itemView.findViewById(R.id.button_basura);
        }

        @Override
        public void onClick(View view) {
            if (mMyOnclickListener != null) {
                mMyOnclickListener.onItemClick(itemView, getLayoutPosition(), 0);
            }
        }
    }

    //region getters and setters
    public void setMyOnclickListener(MyOnclickListener myOnclickListener) {
        mMyOnclickListener = myOnclickListener;
    }
    //endregion
}
