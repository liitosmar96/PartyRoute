package com.example.partyroute.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyroute.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;


public class AddEventActivity extends AppCompatActivity {

    //API KEY: AIzaSyAKaC_Gg6aHkCGCOT-j3fCAqMJC4_gDPSY

    private static final String CARPETA_PRINCIPAL = "PartyRoute/";
    private static final String CARPETA_IMAGEN = "imagenes";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;

    private static final int COD_FOTO = 20, COD_SELECCIONAR = 10;

    EditText fecha, nombre, descripcion, edad, direccion;
    ImageView imagen;
    Button boton;

    Bitmap bitmap;

    ProgressDialog progressDialog;
    StringRequest stringRequest;
    RequestQueue requestQueue;

    String cif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
        cif = intent.getStringExtra("CIF");
        requestQueue = Volley.newRequestQueue(this);

        fecha = findViewById(R.id.fecha);
        nombre = findViewById(R.id.nombre);
        descripcion = findViewById(R.id.lblDescripcion);
        edad = findViewById(R.id.edadMinima);
        direccion = findViewById(R.id.direccion);

        imagen = findViewById(R.id.imagenNuevoEvento);

        boton = findViewById(R.id.botonAnadir);
    }

    /**
     * Metodo para dar la opcion de elegir si abrir la galeria o la camara
     *
     * @param view
     */
    public void mostrarOpciones(View view) {

        String[] opciones = {"Tomar foto", "Elegir de la galería", "Cancelar"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent hacerFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (hacerFotoIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(hacerFotoIntent, COD_FOTO);
                    }
                } else if (which == 1) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(Intent.createChooser(intent, "Seleccione la aplicación"), COD_SELECCIONAR);
                } else {

                }
            }
        });
        builder.show();
    }

    /**
     * Metodo para procesar el resultado de la accion de elegir imagen de la galeria o camara
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case COD_SELECCIONAR:
                Uri miPath = data.getData();
                imagen.setImageURI(miPath);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), miPath);
                    imagen.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagen.setRotation(90);
                break;
            case COD_FOTO:
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                imagen.setImageBitmap(bitmap);
                break;
        }
        bitmap = redimensionarImagen(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
    }


    /**
     * Metodo que envia por metodo post los parametros para insertar un evento,
     * en ese php tambien se almacena la imagen en el servidor y en la table la url de la imagen
     */
    public void cargarWebService() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        String url = "https://biconcave-concentra.000webhostapp.com/partyroute/insertar_evento.php";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                Log.d("INFO", "Evento creado - " + response);
                Toast.makeText(getApplicationContext(), "Evento creado", Toast.LENGTH_LONG).show();
                try {
                    finish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Log.e("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                String fechaString = fecha.getText().toString();
                String nombreString = nombre.getText().toString();
                String descripcionString = descripcion.getText().toString();
                String edadMinimaString = edad.getText().toString();
                String direccionString = direccion.getText().toString();

                String imagen = convertirImagen(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("cif", cif);
                parametros.put("fecha", fechaString);
                parametros.put("nombre", nombreString);
                parametros.put("descripcion", descripcionString);
                parametros.put("edad", edadMinimaString);
                parametros.put("direccion", direccionString);
                parametros.put("imagen", imagen);

                return parametros;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    /**
     * Metodo para transformar una imagen en un string
     *
     * @param bitmap
     * @return
     */
    private String convertirImagen(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagenString;
    }

    /**
     * Metodo que carga el webservice para añadir un evento
     *
     * @param view
     */
    public void anadirEvento(View view) {
        cargarWebService();
    }

    /**
     * Metodo para reducir el tamaño de una imagen
     *
     * @param bitmap
     * @param anchoNuevo
     * @param altoNuevo
     * @return
     */
    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();

        if (ancho > anchoNuevo || alto > altoNuevo) {
            float escalaAncho = anchoNuevo / ancho;
            float escalaAlto = altoNuevo / alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalaAncho, escalaAlto);

            return Bitmap.createBitmap(bitmap, 0, 0, ancho, alto, matrix, false);

        } else {
            return bitmap;
        }
    }
}


