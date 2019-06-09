package com.example.partyroute.fragmentos;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.partyroute.R;
import com.example.partyroute.model.Evento;

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

    static List<Evento> eventos = new ArrayList<>();

    SharedPreferences prefs;
    Set<String> favoritos = new HashSet<>();

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    private SwipeRefreshLayout refreshLayout;

    public EventosFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        favoritos = prefs.getStringSet("favoritos", new HashSet<String>());

        Log.d("INFO", "Preferencias cargadas: " + favoritos);

        final View rootView = inflater.inflate(R.layout.fragment_eventos, container, false);
        requestQueue = Volley.newRequestQueue(getContext());

        cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php");

        lista = rootView.findViewById(R.id.lista);

        lista.setOnItemLongClickListener(this);
        lista.setOnItemClickListener(this);
        refreshLayout = rootView.findViewById(R.id.RefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php");
            }
        });

        return rootView;
    }

    /**
     * Metodo que lanza un intent de Google Maps cuando se pulsa un elemento de la lista
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView direccion = view.findViewById(R.id.lblDireccion);
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + direccion.getText().toString());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    /**
     * Metodo que obtiene todos los eventos almacenados en la base de datos
     *
     * @param url
     */
    public void cargarWebservice(String url) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Metodo que muestra el error producido al conectarse al servidor
     * @param error
     */
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
        refreshLayout.setRefreshing(false);
    }

    /**
     * Metodo al que se le pasa un JsonArray y devuelve una lista con los eventos del jsonArray
     *
     * @param jsonArray
     * @return
     */
    public List<Evento> cargarEventos(JSONArray jsonArray) {
        List<Evento> l = new ArrayList<>();
        Log.d("INFO", "CANTIDAD DE EVENTOS: " + jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                l.add(new Evento(object.getInt("ID"), object.getString("FECHA"), object.getString("NOMBRE"), object.getString("DESCRIPCION"), object.getString("DIRECCION"), object.getString("EDAD"), object.getString("IMAGEN")));
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
        TextView titulo = view.findViewById(R.id.lblTitulo);

        SharedPreferences.Editor editor = prefs.edit();

        if (!favoritos.contains(titulo.getText().toString())) {
            favoritos.add(titulo.getText().toString());
            img.setImageResource(R.drawable.estrella_selected);
        } else {
            favoritos.remove(titulo.getText().toString());
            img.setImageResource(0);
        }
        editor.remove("favoritos").commit();
        editor.putStringSet("favoritos", this.favoritos).commit();
        return true;
    }
}
