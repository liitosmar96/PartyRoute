package com.example.partyroute.fragmentos;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyroute.MainActivity;
import com.example.partyroute.R;
import com.example.partyroute.model.Evento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventosFragment extends Fragment implements ListView.OnItemClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

    ListView lista;
    Activity activity = getActivity();
    //Obtener datos de eventos
    //Evento[] eventos = new Evento[30];

    List<Evento> eventos = new ArrayList<>();

    public EventosFragment() {
        System.out.println("Constructor");
        // Required empty public constructor
        /*
        for (int i = 0; i < eventos.length; i++) {
            eventos[i] = new Evento();
        }
        */
        //cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_eventos, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php");

        lista = rootView.findViewById(R.id.lista);
        //lista.setAdapter(new Adaptador(this.getContext(), eventos));
        lista.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        startActivity(intent);
    }


    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public void cargarWebservice(String url) {
        //String url = "https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php";

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.hide();

        JSONArray jsonArray = response.optJSONArray("evento");
        Log.d("I", jsonArray.toString());
        eventos = cargarEventos(jsonArray);
        lista.setAdapter(new Adaptador(this.getContext(), eventos));
    }

    public List<Evento> cargarEventos(JSONArray jsonArray) {
        List<Evento> l = new ArrayList<>();
        Log.d("I", "CANTIDAD DE EVENTOS: " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);

                l.add(new Evento(object.getInt("ID"), object.getString("FECHA"), object.getString("NOMBRE"), object.getString("DESCRIPCION"), object.getInt("LATITUD"), object.getInt("LONGITUD"), object.getString("EDAD"), object.getInt("RATE"), object.getString("IMAGEN")));
                Log.d("I", l.get(i).toString());
            } catch (JSONException jse) {
                jse.printStackTrace();
            }
        }
        return l;
    }

}
