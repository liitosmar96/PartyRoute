package com.example.partyroute.fragmentos;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.partyroute.MainActivity;
import com.example.partyroute.R;
import com.example.partyroute.model.Evento;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventosFragment extends Fragment implements ListView.OnItemClickListener{

    ListView lista;
    Activity activity = getActivity();
    //Obtener datos de eventos
    Evento[] eventos = new Evento[30];

    public EventosFragment() {
        // Required empty public constructor
        for (int i = 0; i < eventos.length; i++) {
            eventos[i] = new Evento();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_eventos, container, false);
        for (int i = 0; i < eventos.length; i++) {
            eventos[i] = new Evento();
        }
        lista = rootView.findViewById(R.id.lista);

        lista.setAdapter(new Adaptador(this.getContext(), eventos));

        lista.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        startActivity(intent);
    }
}
