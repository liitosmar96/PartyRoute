package com.example.partyroute;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.partyroute.activities.EventosPorUserActivity;
import com.example.partyroute.fragmentos.CuentaUsuarioFragment;
import com.example.partyroute.fragmentos.EventosFragment;
import com.example.partyroute.fragmentos.FavoritosFragment;
import com.example.partyroute.fragmentos.LoginFragment;
import com.example.partyroute.model.Usuario;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String valor = "Inicia sesion primero.";

    TextView correoLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View header = navigationView.getHeaderView(0);
        correoLogged = (TextView) header.findViewById(R.id.correoLogged);
        correoLogged.setText(valor);

        navigationView.setNavigationItemSelectedListener(this);
        cargarFragmento(new EventosFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Si damos la posibilidad de volver hacia atrás salta un error classNotFound
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.cerrarSesion) {
            item.setEnabled(false);
            LOGGED = false;
            cargarFragmento(new LoginFragment());
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean LOGGED = false;


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_eventos) {
            cargarFragmento(new EventosFragment());
        } else if (id == R.id.nav_favoritos) {
            cargarFragmento(new FavoritosFragment());
        } else if (id == R.id.nav_login) {


            if (LOGGED) {
                //Estas lineas actualizan el correo y el usuario en la barra desplegable
                /*
                TextView t = findViewById(R.id.correoLogged);
                t.setText(usuarioLogeado.getCorreo());
                TextView n = findViewById(R.id.nombreLogged);
                n.setText(usuarioLogeado.getNombre());
                */
                cargarFragmento(new CuentaUsuarioFragment());
            } else {
                cargarFragmento(new LoginFragment());
            }


        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            int i = 0;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void cargarFragmento(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.contenedorFragmento, fragment).commit();
    }


    public void mostrarMisEventos(View v) {
        Intent intent = new Intent(this, EventosPorUserActivity.class);
        TextView t = findViewById(R.id.txbCIF);
        intent.putExtra("CIF", t.getText().toString());
        startActivity(intent);
    }


}
