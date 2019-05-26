package com.example.partyroute.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.partyroute.dao.EventoDaoImpl;
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
    public static String CIF;

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_por_user);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ventana para crear
                Intent intent = new Intent(this, AddEventActivity.class);
                //startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */


        Intent intent = getIntent();
        String cif = intent.getStringExtra("CIF");
        CIF = intent.getStringExtra("CIF");

        requestQueue = Volley.newRequestQueue(this);
        cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/select_eventos_por_cif.php?CIF=" + cif);

        //EventoDaoImpl evento = new EventoDaoImpl();
        lista = findViewById(R.id.listaEventos);
        //List<Evento> eventos = evento.findByCif(this, cif);

        lista.setAdapter(new Adaptador(this, eventos));
        lista.setOnItemClickListener(this);
    }


    /**
     * Metodo para obtener los eventos mediante el php almacenado en el servidor, si no da error hace el metodo onResponse,
     * si da error hace el metodo onErrorResponse
     *
     * @param url
     */
    public void cargarWebservice(String url) {
        //String url = "https://biconcave-concentra.000webhostapp.com/partyroute/get_eventos.php";

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Metodo que muestra el error producido
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    /**
     * Metodo que rellena la lista con los eventos que tiene el usuario registrado en la aplicacion
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        progressDialog.hide();

        JSONArray jsonArray = response.optJSONArray("evento");
        Log.d("I", jsonArray.toString());
        eventos = cargarEventos(jsonArray);
        lista.setAdapter(new Adaptador(this, eventos));

        registerForContextMenu(lista);
    }

    /**
     * Metodo que transforma los eventos del jsonArray en una lista de eventos para poder rellenar la listview
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * Metodo que crea el menu contextual que da opciones sobre cada uno de los eventos
     *
     * @param menu
     * @param v
     * @param contextMenuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo contextMenuInfo) {
        MenuInflater m = getMenuInflater();
        m.inflate(R.menu.menu_contextual_mis_eventos, menu);
        super.onCreateContextMenu(menu, v, contextMenuInfo);
    }

    /**
     * Metodo que abre una activity para crear un evento, recibe una view para poder usar onClick
     *
     * @param v
     */
    public void anadirEvento(View v) {
        Intent intent = new Intent(this, AddEventActivity.class);
        startActivity(intent);
    }

    /**
     * Metodo que procesa la peticion seleccionada en el menu contextual, recibe el item que hemos seleccionado
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Evento e;
        switch (item.getItemId()) {
            case R.id.eliminar:
                e = (Evento) lista.getItemAtPosition(info.position);
                Toast.makeText(getApplicationContext(), "Eliminando " + e.getNombre(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.actualizar:
                e = (Evento) lista.getItemAtPosition(info.position);
                Toast.makeText(getApplicationContext(), "Actualizando " + e.getNombre(), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }


}
