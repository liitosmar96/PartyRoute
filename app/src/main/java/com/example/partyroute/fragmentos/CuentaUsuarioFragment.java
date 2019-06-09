package com.example.partyroute.fragmentos;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyroute.MainActivity;
import com.example.partyroute.R;
import com.example.partyroute.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CuentaUsuarioFragment extends Fragment implements View.OnClickListener {

    EditText cif, nombre, txbCorreo;

    public static String correo;
    public static Usuario usuario;

    ProgressDialog progressDialog;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    View vista;

    public static String cif_usuario;

    public CuentaUsuarioFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        vista = inflater.inflate(R.layout.fragment_cuenta_usuario, container, false);
        cif = vista.findViewById(R.id.txbCIF);
        nombre = vista.findViewById(R.id.txbNombre);
        txbCorreo = vista.findViewById(R.id.txbCorreo);

        Button botonCerrar = vista.findViewById(R.id.cerrarSesion);
        botonCerrar.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(getContext());
        cargarWebService("https://biconcave-concentra.000webhostapp.com/partyroute/get_usuario.php?CORREO=" + correo);

        return vista;
    }

    /**
     * Metodo que rellena los textview con los datos del usuario que se ha registrado, recibe la url donde esta el php
     * al que le pasa por parametro un correo, y devuelve un json con los datos del usuario
     *
     * @param url
     */
    public void cargarWebService(String url) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                JSONArray json = response.optJSONArray("usuario");
                JSONObject jsonObject;

                try {
                    jsonObject = json.getJSONObject(0);
                    String cifObtenido = jsonObject.optString("CIF");
                    cif_usuario = cifObtenido;
                    String nombreObtenido = jsonObject.optString("NOMBRE");
                    String correoObtenido = jsonObject.optString("CORREO");

                    cif.setText(cifObtenido);
                    nombre.setText(nombreObtenido);
                    txbCorreo.setText(correoObtenido);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("INFO", "Usuario logueado");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("ERROR", error.toString());
                MainActivity.LOGGED = false;
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Metodo que desloguea al usuario actual y carga la ventana de login
     * @param v
     */
    @Override
    public void onClick(View v) {
        MainActivity.LOGGED = false;
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedorFragmento, new LoginFragment()).commit();
    }
}
