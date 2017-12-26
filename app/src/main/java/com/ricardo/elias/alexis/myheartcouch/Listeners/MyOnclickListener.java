package com.ricardo.elias.alexis.myheartcouch.Listeners;

import android.view.View;

/**
 * Created by Alexis on 3/9/2017.
 */

public interface MyOnclickListener {
        /**
         * Se ejecuta cuando el usuario pulsa un elemento de la lista.
         *
         * @param view vista en la que ocurrió el evento.
         * @param position posición de la vista.
         */
        public void onItemClick(View view, int position, int tag);

        /**
         * Se ejecuta cuando el usuario mantiene presionado por un lapso
         * un elemento de la lista.
         *
         * @param view vista en la que ocurrió el evento.
         * @param position posición de la vista.
         */
        public void onLongItemClick(View view, int position);
}
