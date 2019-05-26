package com.example.partyroute.fragmentos;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {

    //Elementos del fragment
    public EditText correo, contrasena;
    Button botonAceptar;
    CheckBox checkBox;

    //Para mostrar mientras cargan los elementos
    ProgressDialog progressDialog;

    //Para obtener datos de la base de datos
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    //Para guardar las preferencias
    SharedPreferences prefs;

    View vista;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Metodo onCreate que inicializa las variables de las vistas y las preferencias del usuario
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_login, container, false);

        prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        correo = vista.findViewById(R.id.txbNombre);
        contrasena = vista.findViewById(R.id.txbContrasena);
        checkBox = vista.findViewById(R.id.cbRecordar);

        //Intentamos recoger las preferencias que hay guardadas
        correo.setText(prefs.getString("email", "root@root.com"));
        contrasena.setText(prefs.getString("clave", "root"));
        checkBox.setChecked(prefs.getBoolean("checked", true));

        botonAceptar = vista.findViewById(R.id.btnAceptar);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si marcamos la casilla las preferencias se guardan
                if (checkBox.isChecked()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("checked", true);
                    editor.putString("email", correo.getText().toString());
                    editor.putString("clave", contrasena.getText().toString());
                    editor.commit();
                } else {//Si no, se guardan en blanco
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("checked", false);
                    editor.putString("email", "");
                    editor.putString("clave", "");
                    editor.commit();
                }
                cargarWebService("https://biconcave-concentra.000webhostapp.com/partyroute/login.php?CORREO=" + correo.getText().toString());
            }
        });

        return vista;
    }

    /**
     * Metodo que carga el webservice para obtener la clave del usuario que se quiere registrar, en caso de no haber error va a onResponse
     *
     * @param url
     */
    public void cargarWebService(String url) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Metodo que cifra un string en md5
     *
     * @param input
     * @return
     */
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que muestra el error ocurrido si lo hubiera
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

    /**
     * Metodo que comprueba que la clave almacenada es igual a la clave escrita, y si lo es carga un fragment con los datos del usuario
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        progressDialog.hide();

        Usuario usuario = new Usuario();
        JSONArray json = response.optJSONArray("clave");
        JSONObject jsonObject = null;
        try {
            jsonObject = json.getJSONObject(0);
            String claveObtenida = jsonObject.optString("CLAVE");

            if (claveObtenida.equals(getMD5(contrasena.getText().toString()))) {
                MainActivity.LOGGED = true;
                Toast.makeText(getContext(), "Loging CORRECTO", Toast.LENGTH_SHORT).show();

                CuentaUsuarioFragment.correo = correo.getText().toString();

                /*
                MenuItem item = getActivity().findViewById(R.id.cerrarSesion);
                item.setEnabled(true);
                */

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedorFragmento, new CuentaUsuarioFragment()).remove(this)
                        .addToBackStack(null)
                        .commit();


            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
