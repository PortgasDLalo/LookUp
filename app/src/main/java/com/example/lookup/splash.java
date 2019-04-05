package com.example.lookup;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class splash extends AppCompatActivity {
    private String intento;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    private ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo= connectivityManager.getActiveNetworkInfo();
        imagen=(ImageView)findViewById(R.id.Imagen);
        imagen.setEnabled(false);

        if (networkInfo != null) {
            // Si hay conexión a Internet en este momento

            if (networkInfo.isConnected())
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(splash.this, progressBar.class);
                        splash.this.startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        }

        else
        {
            Toast.makeText(splash.this, "Sin conexión a Internet", Toast.LENGTH_SHORT).show();
            imagen.setEnabled(true);
        }

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagen.setEnabled(false);
                connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo= connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    // Si hay conexión a Internet en este momento

                    if (networkInfo.isConnected())
                    {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(splash.this, progressBar.class);
                                splash.this.startActivity(intent);
                                finish();
                            }
                        }, 2000);
                    }
                }else
                {
                    Toast.makeText(splash.this, "Sin conexión a Internet", Toast.LENGTH_SHORT).show();
                    imagen.setEnabled(true);
                }
            }
        });
    }
}
