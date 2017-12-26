package com.ricardo.elias.alexis.myheartcouch.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ricardo.elias.alexis.myheartcouch.Listeners.MyOnclickListener;
import com.ricardo.elias.alexis.myheartcouch.Model.Leccion;
import com.ricardo.elias.alexis.myheartcouch.R;

import java.util.ArrayList;

/**
 * Created by Alexis on 3/9/2017.
 */

public class LeccionesAdapter extends RecyclerView.Adapter<LeccionesAdapter.LeccionesVH> {
    ;
    private ArrayList<Leccion> mLeccions;
    private Context mContext ;
    private MyOnclickListener mMyOnclickListener;

    public LeccionesAdapter(Context context) {
        mLeccions = new ArrayList<>();
        mContext = context;
        mLeccions = new ArrayList<>();
    }

    @Override
    public LeccionesVH onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_lecciones,parent,false);
        final LeccionesVH viewHolder = new LeccionesVH(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(LeccionesVH holder, int position) {
        Leccion lesson = mLeccions.get(position);
        holder.mTextLeccion.setText(mLeccions.get(position).getTitulo());
        if (lesson.getMisCompleted() == false){
            int gray = mContext.getResources().getColor(R.color.gray);
            holder.mImagePalomita.setColorFilter(gray);
        }else {
            int green = mContext.getResources().getColor(R.color.green);
            holder.mImagePalomita.setColorFilter(green);}
    }

    @Override
    public int getItemCount() {
        return mLeccions.size();
    }

    public class LeccionesVH extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mTextLeccion;
        private ImageView mImagePalomita;
        public LeccionesVH(View itemView) {
            super(itemView);
            mTextLeccion = (TextView) itemView.findViewById(R.id.text_lesson_name);
            mImagePalomita = (ImageView) itemView.findViewById(R.id.image_palomita);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mMyOnclickListener != null) {
                mMyOnclickListener.onItemClick(itemView,getLayoutPosition(),0);
            }
        }
    }

    public void setMyOnclickListener(MyOnclickListener myOnclickListener) {
        mMyOnclickListener = myOnclickListener;
    }

    public void setLeccions(ArrayList<Leccion> leccions) {
        mLeccions = leccions;
    }
}
