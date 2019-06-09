package com.example.partyroute.fragmentos;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.partyroute.R;
import com.example.partyroute.model.Evento;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Fragmento que muestra los eventos seleccionados como favoritos en el fragmento de todos los eventos
 * A simple {@link Fragment} subclass.
 */
public class FavoritosFragment extends Fragment {

    List<Evento> eventos = new ArrayList<>();
    ListView lista;
    Set<String> favoritos;

    public FavoritosFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_favoritos, container, false);

        List<Evento> eventosFavoritos = new ArrayList<>();

        lista = rootView.findViewById(R.id.lista);
        eventos = EventosFragment.eventos;

        SharedPreferences prefs = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        favoritos = prefs.getStringSet("favoritos", new HashSet<String>());

        for (int i = 0; i < eventos.size(); i++) {
            if (favoritos.contains(eventos.get(i).getNombre())) {
                eventosFavoritos.add(eventos.get(i));
            }
        }
        lista.setAdapter(new Adaptador(this.getContext(), eventosFavoritos));

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView direccion = view.findViewById(R.id.lblDireccion);
                Uri uri = Uri.parse("geo:0,0?q=" + direccion.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        return rootView;
    }
}
