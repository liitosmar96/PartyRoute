package com.example.partyroute.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyroute.R;
import com.example.partyroute.fragmentos.Adaptador;
import com.example.partyroute.fragmentos.CuentaUsuarioFragment;
import com.example.partyroute.model.Evento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventosPorUserActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject> {

    ListView lista;
    static List<Evento> eventos = new ArrayList<>();
    public static String CIF;

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    StringRequest stringRequest;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_por_user);

        FloatingActionButton fab = findViewById(R.id.fab);

        Intent intent = getIntent();
        final String cif = intent.getStringExtra("CIF");
        CIF = intent.getStringExtra("CIF");

        requestQueue = Volley.newRequestQueue(this);
        cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/select_eventos_por_cif.php?CIF=" + cif);

        lista = findViewById(R.id.listaEventos);

        lista.setAdapter(new Adaptador(this, eventos));

        refreshLayout = findViewById(R.id.RefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/select_eventos_por_cif.php?CIF=" + cif);
            }
        });

    }

    /**
     * Metodo para obtener los eventos mediante el php almacenado en el servidor, si no da error hace el metodo onResponse,
     * si da error hace el metodo onErrorResponse
     *
     * @param url
     */
    public void cargarWebservice(String url) {
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
        Log.e("ERROR", error.toString());
        refreshLayout.setRefreshing(false);

    }

    /**
     * Metodo que rellena la lista con los eventos que tiene el usuario registrado en la aplicacion
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        JSONArray jsonArray = response.optJSONArray("evento");
        Log.d("INFO", jsonArray.toString());
        eventos = cargarEventos(jsonArray);
        lista.setAdapter(new Adaptador(this, eventos));

        registerForContextMenu(lista);

        progressDialog.hide();
        refreshLayout.setRefreshing(false);
    }

    /**
     * Metodo que transforma los eventos del jsonArray en una lista de eventos para poder rellenar la listview
     *
     * @param jsonArray
     * @return
     */
    public List<Evento> cargarEventos(JSONArray jsonArray) {
        List<Evento> l = new ArrayList<>();
        Log.d("INFO", "Obtenidos " + jsonArray.length() + " eventos.");
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                l.add(new Evento(object.getInt("ID"), object.getString("FECHA"), object.getString("NOMBRE"), object.getString("DESCRIPCION"), object.getString("DIRECCION"), object.getString("EDAD"), object.getString("IMAGEN")));
            } catch (JSONException jse) {
                Log.e("ERROR", jse.toString());
            }
        }
        return l;
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
        intent.putExtra("CIF", CuentaUsuarioFragment.cif_usuario);
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
                eliminarEvento(e);
                return true;
            case R.id.actualizar:
                e = (Evento) lista.getItemAtPosition(info.position);
                Toast.makeText(getApplicationContext(), "Funcion no implementada.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }

    /**
     * Metodo que elimina de la base de datos el evento que se le pasa por parametro
     *
     * @param e
     */
    private void eliminarEvento(Evento e) {
        String url = "https://biconcave-concentra.000webhostapp.com/partyroute/eliminar_evento.php?ID=" + e.getID();

        final Evento eTemp = e;

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), eTemp.getNombre() + " eliminado. - " + response, Toast.LENGTH_SHORT).show();
                cargarWebservice("https://biconcave-concentra.000webhostapp.com/partyroute/select_eventos_por_cif.php?CIF=" + CIF);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No se ha podido eliminar el evento", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
