package com.example.lookup;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class progressBar extends AppCompatActivity {
    private ProgressBar pr;
    private Handler handler= new Handler();
    private int i = 0;
    private TextView progreso;
    private TextView mensaje;
    private int intentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        pr=(ProgressBar)findViewById(R.id.progressBar);
        progreso=(TextView)findViewById(R.id.porcentaje);
        mensaje=(TextView)findViewById(R.id.mensajes);

        final Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (i<=100)
                {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progreso.setText(i+" %");
                            pr.setProgress(i);
                            if (i==10)
                            {
                                mensaje.setText("Bienvenido a Look Up");
                            }
                            else if (i==40)
                            {
                                mensaje.setText("Checando conexión....");

                            }
                            else if (i==70)
                            {
                                mensaje.setText("Checando Servidores....");
                            }
                            else if (i==90)
                            {
                                mensaje.setText("Ya casi esta....");
                            }
                            else if (i==100)
                            {
                                Intent intent = new Intent(progressBar.this, Login.class);
                                progressBar.this.startActivity(intent);
                                finish();
                            }
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        });
        hilo.start();
    }
}
