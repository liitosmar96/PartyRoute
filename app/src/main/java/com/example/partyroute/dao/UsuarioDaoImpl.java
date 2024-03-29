package com.example.partyroute.dao;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.partyroute.model.Evento;
import com.example.partyroute.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoImpl implements UsuarioDao {

    String URL_DATABASE = "https://biconcave-concentra.000webhostapp.com/partyroute/";
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


    List<Evento> eventos = new ArrayList<>();

    @Override
    public Usuario findByEmail(String email) {
        String url = URL_DATABASE + "get_usuario.php?CORREO=" + email;

        List<Evento> eventos = new ArrayList<>();



        return usuarioEjemplo;
    }

    @Override
    public Usuario findByEmail(Usuario usr) {
        //return findByEmail(usr.getCorreo());
        return null;
    }

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

    /*
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
    */
}
