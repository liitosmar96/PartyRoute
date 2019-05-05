package com.example.partyroute.dao;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyroute.MainActivity;
import com.example.partyroute.Volleys;
import com.example.partyroute.fragmentos.CuentaUsuarioFragment;
import com.example.partyroute.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class UsuarioDaoImpl implements UsuarioDao, Response.ErrorListener, Response.Listener<JSONObject> {

    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;

    Usuario usuarioEjemplo;

    @Override
    public List<Usuario> findAll() {
        return null;
    }

    @Override
    public Usuario findByID(int id) {
        return null;
    }

    @Override
    public Usuario findByID(Usuario usr) {
        return null;
    }

    @Override
    public Usuario findByEmail(String email) {
        String url = "https://biconcave-concentra.000webhostapp.com/partyroute/get_usuario.php?CORREO=" + email;
        //requestQueue = Volley.newRequestQueue(Volleys.);

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        requestQueue.add(jsonObjectRequest);


        return usuarioEjemplo;
    }

    @Override
    public Usuario findByEmail(Usuario usr) {
        return findByEmail(usr.getCorreo());
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        usuarioEjemplo = new Usuario();
        JSONArray json = response.optJSONArray("usuario");
        JSONObject jsonObject = null;

        try {
            jsonObject = json.getJSONObject(0);
            String cifObtenido = jsonObject.optString("CIF");
            String nombreObtenido = jsonObject.optString("NOMBRE");
            String correoObtenido = jsonObject.optString("CORREO");

            usuarioEjemplo = new Usuario(cifObtenido, nombreObtenido, correoObtenido);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
