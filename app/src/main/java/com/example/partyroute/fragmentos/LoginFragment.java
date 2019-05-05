package com.example.partyroute.fragmentos;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    EditText correo, contrasena;
    Button botonAceptar;

    ProgressDialog progressDialog;

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    public LoginFragment() {
        // Required empty public constructor
    }

    View vista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_login, container, false);

        correo = vista.findViewById(R.id.txbNombre);
        contrasena = vista.findViewById(R.id.txbContrasena);

        botonAceptar = vista.findViewById(R.id.btnAceptar);

        requestQueue = Volley.newRequestQueue(getContext());

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService("https://biconcave-concentra.000webhostapp.com/partyroute/login.php?CORREO=" + correo.getText().toString());
            }
        });

        return vista;
    }

    public void cargarWebService(String url) {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        //String url = "https://biconcave-concentra.000webhostapp.com/partyroute/login.php?CORREO=" + correo.getText().toString();

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

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


    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        Log.i("ERROR", error.toString());
    }

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

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contenedorFragmento, new CuentaUsuarioFragment())
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
