package com.example.partyroute.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.example.partyroute.MainActivity;
import com.example.partyroute.R;
import com.example.partyroute.model.Evento;
import com.squareup.picasso.Picasso;

import org.w3c.dom.ls.LSOutput;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.logging.Logger;

import static java.security.AccessController.getContext;

public class Adaptador extends BaseAdapter {

    private static LayoutInflater layoutInflater = null;
    Context contexto;

    List<Evento> eventos;

    public Adaptador(Context contexto, List<Evento> eventos) {
        this.contexto = contexto;
        this.eventos = eventos;
        layoutInflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View vista = layoutInflater.inflate(R.layout.eventos_lista, parent, false);

        //ImageView imagen = vista.findViewById(R.id.imagen);
        //Bitmap bitmap = BitmapFactory.decodeByteArray(eventos[position].getImagen(), 0, eventos[position].getImagen().length);
        //ImageView imagen;

        TextView titulo = vista.findViewById(R.id.lblTitulo);
        titulo.setText(eventos.get(position).getNombre());
        System.out.println(eventos.get(position).getNombre());

        TextView descripcion = vista.findViewById(R.id.lblDescripcion);
        descripcion.setText(eventos.get(position).getDescripcion());

        TextView edad = vista.findViewById(R.id.lblEdad);
        edad.setText(eventos.get(position).getEdad());

        RatingBar ratingBar = vista.findViewById(R.id.ratingBar);
        ratingBar.setRating(eventos.get(position).getRate());


        //Bitmap obtener_imagen = get_imagen(eventos.get(position).getImagen());
        //imageView.setImageBitmap(obtener_imagen);



        /*
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contexto, MainActivity.class);
                contexto.startActivity(intent);
            }
        });
        */


        ImageView imagen = vista.findViewById(R.id.imagenView);
        Picasso.with(contexto).load(eventos.get(position).getImagen()).error(R.drawable.imagen).fit().centerInside().into(imagen);


        return vista;
    }

    @Override
    public int getCount() {
        return eventos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private Bitmap get_imagen(String url) {
        Bitmap bm = null;
        try {
            URL _url = new URL(url);
            URLConnection con = _url.openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {

        }
        return bm;
    }

    Bitmap bitmap;
/*
    private void cargarWebServiceImagen(String urlImagen) {
        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap=response;//SE MODIFICA
                ImageView campoImagen =
                campoImagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR IMAGEN","Response -> "+error);
            }
        });
        //  request.add(imageRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(imageRequest);
    }
*/
}
