package com.example.lookup;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    private EditText usuario, password;
    private Button login, registrar;
    private AsyncHttpClient cliente;
    private final int REQUEST_ACCESS_FINE = 0;
    private final int REQUEST_ACCESS_INTERNET = 0;
    private final int REQUEST_ACCESS_WIFI = 0;
    private final int REQUEST_ACCESS_NETWORK = 0;
    private final int REQUEST_ACCESS_READE = 0;
    private final int REQUEST_ACCESS_CAMERA = 0;
    private final int REQUEST_ACCESS_WRITE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario=(EditText)findViewById(R.id.usuario);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        registrar=(Button)findViewById(R.id.register);
        cliente = new AsyncHttpClient();

        botonLogin();
        botonRegistrar();
        if (permisos()) {
            //enviar();
        } else {
            Toast.makeText(this, "Habilite los permisos", Toast.LENGTH_LONG).show();

        }
    }

    private boolean permisos() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) ||
                (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) ||
                (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            cargarDialogo();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        }
        return false;
    }


    private void cargarDialogo() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Permisos desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la aplicación");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                }
            }
        });
        dialogo.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length == 4 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                //enviar();
            } else {
                solicitarPermisosManual();
            }

        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"si", "no"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(this);
        alertOpciones.setTitle("Seleccione una opción");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void botonLogin()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.setEnabled(false);
                if (usuario.getText().toString().isEmpty() || password.getText().toString().isEmpty())
                {
                    Toast.makeText(Login.this, "Hay Campos Vacios", Toast.LENGTH_SHORT).show();
                    login.setEnabled(true);
                }else
                {
                    final String usu = usuario.getText().toString().replace(" ","%20");
                    String pas = password.getText().toString().replace(" ","%20");
                    String url = "https://localizadorazl1.000webhostapp.com/entrar.php?usuario="+usu+"&contraseña="+pas;
                    cliente.post(url, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            if (statusCode==200)
                            {
                                String respuesta = new String(responseBody);
                                if (respuesta.equalsIgnoreCase("null"))
                                {
                                    Toast.makeText(Login.this, "Error de Usuario y/o Contraseña", Toast.LENGTH_SHORT).show();
                                    usuario.setText("");
                                    password.setText("");
                                    login.setEnabled(true);
                                }
                                else
                                {
                                    try {
                                        JSONObject jsonObject = new JSONObject(respuesta);
                                        Usuario u = new Usuario();
                                        u.setId(jsonObject.getInt("id"));
                                        u.setUsuario(jsonObject.getString("usuario"));
                                        u.setContraseña(jsonObject.getString("contraseña"));
                                        u.setNombre(jsonObject.getString("nombre"));
                                        u.setDireccion(jsonObject.getString("direccion"));
                                        u.setCiudad(jsonObject.getString("ciudad"));
                                        u.setEstado(jsonObject.getString("estado"));
                                        u.setCorreo(jsonObject.getString("correo"));
                                        u.setTelefono(jsonObject.getString("telefono"));
                                        u.setFoto(jsonObject.getString("foto"));
                                        Intent intent = new Intent(Login.this, Navegador.class);
                                        intent.putExtra("id", jsonObject.getInt("id"));
                                        intent.putExtra("nombre", jsonObject.getString("nombre"));
                                        intent.putExtra("foto", jsonObject.getString("foto"));
                                        login.setEnabled(true);
                                        Login.this.startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }
            }
        });
    }

    private void botonRegistrar()
    {
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                Login.this.startActivity(intent);
            }
        });
    }
}
