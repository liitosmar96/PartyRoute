package com.example.partyroute.activities;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.partyroute.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.media.MediaRecorder.VideoSource.CAMERA;
import static java.security.AccessController.getContext;


public class AddEventActivity extends AppCompatActivity {

    //API KEY: AIzaSyAKaC_Gg6aHkCGCOT-j3fCAqMJC4_gDPSY

    private static final String CARPETA_PRINCIPAL = "PartyRoute/";
    private static final String CARPETA_IMAGEN = "imagenes";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;

    private static final int COD_FOTO = 20, COD_SELECCIONAR = 10;

    //private String path;

    EditText fecha, nombre, descripcion, edad, direccion;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        requestQueue = Volley.newRequestQueue(this);

        fecha = findViewById(R.id.fecha);
        nombre = findViewById(R.id.nombre);
        descripcion = findViewById(R.id.descripcion);
        edad = findViewById(R.id.edadMinima);
        direccion = findViewById(R.id.direccion);

        imagen = findViewById(R.id.imagenNuevoEvento);


    }

    Bitmap bitmap;

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
                //imagen.setRotation(90);

                //MediaScannerConnection.scanFile(this, new String[]{path}, null, {Log.i("Path", path)});
                break;
        }
        bitmap = redimensionarImagen(bitmap, 300, 400);
    }

    ProgressDialog progressDialog;
    StringRequest stringRequest;
    RequestQueue requestQueue;

    //JsonObjectRequest jsonObjectRequest;

    /**
     * Metodo que envia por metodo post los parametros para insertar un evento,
     * en ese php tambien se almacena la imagen en el servidor y en la table la url de la imagen
     */
    public void cargarWebService() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        /*
        String fechaString = fecha.getText().toString();
        String nombreString = fecha.getText().toString();
        String descripcionString = fecha.getText().toString();
        String edadMinimaString = fecha.getText().toString();
        String direccionString = fecha.getText().toString();

        String imagen = convertirImagen(bitmap);
        */

        String url = "https://biconcave-concentra.000webhostapp.com/partyroute/insertar_evento.php";//?cif="+EventosPorUserActivity.CIF+"&fecha="+fechaString+"&nombre="+nombreString+"&descripcion="+descripcionString+"&direccion="+descripcionString+"&edad="+edadMinimaString+"&imagen="+imagen;

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equals("correcto")) {
                    Toast.makeText(getApplicationContext(), "Evento insertado", Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(getApplicationContext(), "Evento insertado2", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String fechaString = fecha.getText().toString();
                String nombreString = fecha.getText().toString();
                String descripcionString = fecha.getText().toString();
                String edadMinimaString = fecha.getText().toString();
                String direccionString = fecha.getText().toString();

                String imagen = convertirImagen(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("cif", EventosPorUserActivity.CIF);
                parametros.put("fecha", fechaString);
                parametros.put("nombre", nombreString);
                parametros.put("descripcion", descripcionString);
                parametros.put("edad", edadMinimaString);
                parametros.put("direccion", direccionString);
                parametros.put("imagen", imagen);

                return parametros;
            }
        };


        //jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        //VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
//requestQueue.add(jsonObjectRequest);
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

    /*
    private void solicitudPermisos() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("¿Quieres configurar los permisos de forma manual?");
        alert.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", )
                } else {

                }
            }
        });
    }
*/

    /**
     * Metodo que carga el webservice para añadir un evento
     *
     * @param view
     */
    public void anadirEvento(View view) {
        cargarWebService();
    }

    /*
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getApplicationContext(), "Evento insertado", Toast.LENGTH_SHORT).show();
    }
    */

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


