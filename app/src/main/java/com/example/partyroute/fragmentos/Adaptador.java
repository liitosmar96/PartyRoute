package com.example.partyroute.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.partyroute.MainActivity;
import com.example.partyroute.R;
import com.example.partyroute.model.Evento;

import org.w3c.dom.ls.LSOutput;

import java.util.logging.Logger;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater layoutInflater = null;
    Context contexto;

    Evento[] eventos;

    public Adaptador(Context contexto, Evento[] eventos) {
        this.contexto = contexto;
        this.eventos = eventos;
        layoutInflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = layoutInflater.inflate(R.layout.eventos_lista, parent, false);

        //ImageView imagen = vista.findViewById(R.id.imagen);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(eventos[position].getImagen(), 0, eventos[position].getImagen().length);
        //ImageView imagen;

        TextView titulo = vista.findViewById(R.id.lblTitulo);
        titulo.setText(eventos[position].getNombre() + " " + position);
        System.out.println(eventos[position].getNombre());

        TextView descripcion = vista.findViewById(R.id.lblDescripcion);
        descripcion.setText(eventos[position].getDescripcion() + " " + position);

        TextView edad = vista.findViewById(R.id.lblEdad);
        edad.setText(eventos[position].getEdad());

        RatingBar ratingBar = vista.findViewById(R.id.ratingBar);
        ratingBar.setRating((eventos[position].getRate() / (float) 5) * 10);

        /*
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, MainActivity.class);
                contexto.startActivity(intent);
            }
        });
        */

        return vista;
    }

    @Override
    public int getCount() {
        return eventos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
