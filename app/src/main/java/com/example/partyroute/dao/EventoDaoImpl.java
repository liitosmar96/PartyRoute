package com.example.partyroute.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyroute.model.Evento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventoDaoImpl implements EventoDao {

    String URL_DATABASE = "https://biconcave-concentra.000webhostapp.com/partyroute/";
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    EventoDao eventoDao;

    @Override
    public List<Evento> findAll() {
        return null;
    }

    @Override
    public Evento findByID(int id) {
        return null;
    }

    @Override
    public Evento findByID(Evento usr) {
        return null;
    }

    private List<Evento> eventos = new ArrayList<>();

    @Override
    public List<Evento> findByCif(Context context, String cif) {
        String url = URL_DATABASE + "select_eventos_por_cif.php?CIF=" + cif;


        requestQueue = Volley.newRequestQueue(context);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = response.optJSONArray("evento");
                Log.d("I", jsonArray.toString());
                eventos = cargarEventos(jsonArray);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);

        return eventos;
    }

    @Override
    public Evento findByEmail(Evento usr) {
        return null;
    }

    /*
    *
    * Metodo que devuelve una List de Eventos sacados del JSONArray que se le pasa por parametro
    *
    * */
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
}
