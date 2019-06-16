package com.example.partyroute.fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.partyroute.R;
import com.example.partyroute.model.Evento;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater layoutInflater = null;

    Context contexto;

    List<Evento> eventos;

    ImageView estrellaFav;
    Set<String> favoritos;

    /**
     * Constructor del adaptador, recibe el contexto y los eventos
     *
     * @param contexto
     * @param eventos
     */
    public Adaptador(Context contexto, List<Evento> eventos) {
        this.contexto = contexto;
        this.eventos = eventos;
        layoutInflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
   }


    /**
     * Metodo que devueve cada una de las vistas que aparecen en la lista de eventos
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = layoutInflater.inflate(R.layout.eventos_lista, parent, false);

        TextView titulo = vista.findViewById(R.id.lblTitulo);
        titulo.setText(eventos.get(position).getNombre());

        TextView descripcion = vista.findViewById(R.id.lblDescripcion);
        descripcion.setText(eventos.get(position).getDescripcion());

        TextView edad = vista.findViewById(R.id.lblEdad);
        edad.setText(eventos.get(position).getEdad());

        TextView direccion = vista.findViewById(R.id.lblDireccion);
        direccion.setText(eventos.get(position).getDireccion());

        estrellaFav = vista.findViewById(R.id.imagenFav);

        SharedPreferences prefs = contexto.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        favoritos = prefs.getStringSet("favoritos", new HashSet<String>());

        if (favoritos.contains(titulo.getText().toString())){
            estrellaFav.setImageResource(R.drawable.estrella_selected);
        }

        ImageView imagen = vista.findViewById(R.id.imagenView);
        Picasso.with(contexto)
                .load(eventos.get(position)
                .getImagen())
                .error(R.drawable.imagen)
                .fit()
                .centerInside()
                .into(imagen);

        TextView fecha = vista.findViewById(R.id.fecha);
        fecha.setText(eventos.get(position).getFecha());
        return vista;
    }

    /**
     * Metodo que devueve el tamaño de la lista
     *
     * @return
     */
    @Override
    public int getCount() {
        return eventos.size();
    }

    /**
     * Metodo que devuelve el objeto de una posicion
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return eventos.get(position);
    }

    /**
     * Metodo que devuelve el id de un objeto de una posicion
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return eventos.get(position).getID();
    }
}
