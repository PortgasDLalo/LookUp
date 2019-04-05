package com.example.lookup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class Navegador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int id1=0;
    private int tipo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Bundle parametros = this.getIntent().getExtras();
        id1 = parametros.getInt("id");
        //Toast.makeText(this,""+id1, Toast.LENGTH_SHORT).show();

        /****Encabezado***/
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView nombre = (TextView) hView.findViewById(R.id.tvNombreNav);
        ImageView imageView = (ImageView)hView.findViewById(R.id.imageView);
        nombre.setText(parametros.getString("nombre"));
        String foto = parametros.getString("foto");
        if (foto.startsWith("content:"))
        {

            imageView.setImageURI(Uri.parse(foto));
        }else if (foto.startsWith("/storage"))
        {
            Bitmap bitmap = BitmapFactory.decodeFile(foto);
            imageView.setImageBitmap(bitmap);
        }
        navigationView.setNavigationItemSelectedListener(this);
        /***********/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegador, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            tipo = 1;
            LisLocalizados localizados2 = new LisLocalizados();
            Bundle bundle = new Bundle();
            bundle.putInt("idloc", id1);
            bundle.putInt("tipo", tipo);
            localizados2.setArguments(bundle);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, localizados2, null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_gallery) {

            AgregarDisp disp = new AgregarDisp();
            Bundle bundle = new Bundle();
            bundle.putInt("idloc", id1);
            bundle.putInt("dato", 1);
            disp.setArguments(bundle);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, disp, null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_slideshow) {
            tipo = 2;
            LisLocalizados localizados2 = new LisLocalizados();
            Bundle bundle = new Bundle();
            bundle.putInt("idloc", id1);
            bundle.putInt("tipo", tipo);
            localizados2.setArguments(bundle);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, localizados2, null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_manage) {
            tipo = 3;
            LisLocalizados localizados2 = new LisLocalizados();
            Bundle bundle = new Bundle();
            bundle.putInt("idloc", id1);
            bundle.putInt("tipo", tipo);
            localizados2.setArguments(bundle);

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.contenedor, localizados2, null);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
