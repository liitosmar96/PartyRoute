package com.example.partyroute.fragmentos;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.partyroute.activities.AnadirEventoActivity;
import com.example.partyroute.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CuentaUsuarioFragment extends Fragment {

    EditText cif, nombre, txbCorreo;


    public static String correo;
    public static Usuario usuario;

    ProgressDialog progressDialog;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public CuentaUsuarioFragment() {
        // Required empty public constructor
System.out.println("Constructor");
    }


    View vista;

    TextView menuUsuario, menuCorreo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("onCreateView");
        vista = inflater.inflate(R.layout.fragment_cuenta_usuario, container, false);
        cif = vista.findViewById(R.id.txbCIF);
        nombre = vista.findViewById(R.id.txbNombre);
        txbCorreo = vista.findViewById(R.id.txbCorreo);

        /*
        menuUsuario = vista.findViewById(R.id.nombreLogged);
        menuCorreo = vista.findViewById(R.id.correoLogged);
        */


        requestQueue = Volley.newRequestQueue(getContext());
        cargarWebService("https://biconcave-concentra.000webhostapp.com/partyroute/get_usuario.php?CORREO=" + correo);


        Button botonAnadirEvento = vista.findViewById(R.id.cerrarSesion);


        return vista;
    }

    public void cargarWebService(String url) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        //String url = "https://biconcave-concentra.000webhostapp.com/partyroute/login.php?CORREO=" + correo.getText().toString();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                JSONArray json = response.optJSONArray("usuario");
                JSONObject jsonObject = null;

                try {
                    jsonObject = json.getJSONObject(0);
                    String cifObtenido = jsonObject.optString("CIF");
                    String nombreObtenido = jsonObject.optString("NOMBRE");
                    String correoObtenido = jsonObject.optString("CORREO");

                    /*
                    usuario = new Usuario(cifObtenido, nombreObtenido, correoObtenido);
                    MainActivity.usuarioLogeado = usuario;
                    */


                    cif.setText(cifObtenido);
                    nombre.setText(nombreObtenido);
                    txbCorreo.setText(correoObtenido);


                    //menuUsuario.setText(nombreObtenido);
                    //menuCorreo.setText(correoObtenido);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.i("ERROR", error.toString());
                MainActivity.LOGGED = false;
            }
        });
        requestQueue.add(jsonObjectRequest);
    }



}
