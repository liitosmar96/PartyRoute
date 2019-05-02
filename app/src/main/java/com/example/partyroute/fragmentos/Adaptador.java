package com.example.partyroute.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.partyroute.R;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater layoutInflater = null;
    Context contexto;

    Object[] eventos;

    public Adaptador(Context contexto, Object[] eventos) {
        this.contexto = contexto;
        this.eventos = eventos;
        layoutInflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = layoutInflater.inflate(R.layout.eventos_lista, null);

        ImageView imagen = vista.findViewById(R.id.imagen);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, null);
                contexto.startActivity(intent);
            }
        });

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
