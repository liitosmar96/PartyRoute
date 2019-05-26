package com.example.partyroute.fragmentos;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventosFragment extends Fragment implements ListView.OnItemClickListener, ListView.OnItemLongClickListener, Response.ErrorListener, Response.Listener<JSONObject> {

    ListView lista;
    Activity activity = getActivity();
    //Obtener datos de eventos
    //Evento[] eventos = new Evento[30];

    List<Evento> eventos = new ArrayList<>();

    SharedPreferences prefs;
    Set<String> favoritos = new HashSet<>();

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

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

        prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        favoritos = prefs.getStringSet("favoritos", new HashSet<String>());

        System.out.println("Preferencias cargadas: " + favoritos);

        final View rootView = inflater.inflate(R.layout.fragment_eventos, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php");

        lista = rootView.findViewById(R.id.lista);
        //lista.setAdapter(new Adaptador(this.getContext(), eventos));
        //lista.setOnItemClickListener(this);

        lista.setOnItemLongClickListener(this);


        System.out.println("Termina onCreate");
        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }

    /**
     * Metodo que obtiene todos los eventos almacenados en la base de datos
     *
     * @param url
     */
    public void cargarWebservice(String url) {
        //String url = "https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php";
        //favoritos = prefs.getStringSet("favoritos", new HashSet<String>());

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

    /**
     * Metodo que procesa la peticion del webservice en caso de ser correcto y muestra los eventos en la lista
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("evento");
        //Log.d("I", jsonArray.toString());
        eventos = cargarEventos(jsonArray);
        Adapter adapter = new Adaptador(this.getContext(), eventos);
        lista.setAdapter(new Adaptador(this.getContext(), eventos));

        for (int i = 0; i < lista.getCount(); i++) {
            Evento e = (Evento) adapter.getItem(i);
            String nombre = e.getNombre();
            System.out.println(i);

            if (favoritos.contains(nombre)) {
                System.out.println("Estaba guardado el nombre: " + nombre);
                View v = lista.getChildAt(i);
                //ImageView img = v.findViewById(R.id.imagenFav);
                //img.setImageResource(R.drawable.estrella_selected);
            }
        }
        progressDialog.hide();
    }

    /**
     * Metodo al que se le pasa un JsonArray y devuelve una lista con los eventos del jsonArray
     *
     * @param jsonArray
     * @return
     */
    public List<Evento> cargarEventos(JSONArray jsonArray) {
        List<Evento> l = new ArrayList<>();
        Log.d("I", "CANTIDAD DE EVENTOS: " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                l.add(new Evento(object.getInt("ID"), object.getString("FECHA"), object.getString("NOMBRE"), object.getString("DESCRIPCION"), object.getString("DIRECCION"), object.getString("EDAD"), object.getString("IMAGEN")));
                Log.d("I", l.get(i).toString());
            } catch (JSONException jse) {
                jse.printStackTrace();
            }
        }
        return l;
    }

    /**
     * Metodo que permite guardar o eliminar los eventos como favoritos y cambiar la imagen de la estrellita de la esquina
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ImageView img = view.findViewById(R.id.imagenFav);
        TextView t = view.findViewById(R.id.lblTitulo);

        SharedPreferences.Editor editor = prefs.edit();

        if (!favoritos.contains(t.getText().toString())) {
            favoritos.add(t.getText().toString());
            img.setImageResource(R.drawable.estrella_selected);
            System.out.println("AÑADIENDO");
            System.out.println(favoritos);
        } else {
            favoritos.remove(t.getText().toString());
            img.setImageResource(R.drawable.estrella);
            System.out.println("ELIMINANDO");
            System.out.println(favoritos);
        }
        editor.putStringSet("favoritos", favoritos);
        editor.commit();
        return true;
    }
}
