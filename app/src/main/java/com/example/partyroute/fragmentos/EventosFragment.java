package com.example.partyroute.fragmentos;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.partyroute.MainActivity;
import com.example.partyroute.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventosFragment extends Fragment {

    ListView lista;
    Activity activity = getActivity();
    //Obtener datos de eventos
    Object[] objects = new Object[30];

    public EventosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_eventos, container, false);

        lista = rootView.findViewById(R.id.lista);

        lista.setAdapter(new Adaptador(this.getContext(), objects));

        return rootView;
    }

}
