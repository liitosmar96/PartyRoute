package com.example.partyroute.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyroute.R;
import com.example.partyroute.fragmentos.Adaptador;
import com.example.partyroute.model.Evento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventosPorUserActivity extends AppCompatActivity implements ListView.OnItemClickListener, Response.ErrorListener, Response.Listener<JSONObject> {
    ListView lista;
    List<Evento> eventos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_por_user);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ventana para crear
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Intent intent = getIntent();
        String cif = intent.getStringExtra("CIF");

        requestQueue = Volley.newRequestQueue(this);
        cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/select_eventos_por_cif.php?CIF=" + cif);

        lista = findViewById(R.id.listaEventos);
        lista.setOnItemClickListener(this);
    }

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public void cargarWebservice(String url) {
        //String url = "https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialog.hide();

        JSONArray jsonArray = response.optJSONArray("evento");
        Log.d("I", jsonArray.toString());
        eventos = cargarEventos(jsonArray);
        lista.setAdapter(new Adaptador(this, eventos));
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
