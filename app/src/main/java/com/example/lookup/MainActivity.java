package com.example.lookup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private EditText etusuario, etcontraseña, etNombre, etDireccion, etciudad, etestado, etcorreo, etTelefono;
    private Button btnRegistrar;
    private Button btnRegresar;
    private AsyncHttpClient cliente;
    private int tipo=1;
    private ImageView imageView;
    private Button foto;
    private String DireccionFoto;

    private Context TheThis;
    private String NameOfFolder = "/LookUp";
    private String NameOfFile = "imagen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etusuario=(EditText)findViewById(R.id.etusuario);
        etcontraseña=(EditText)findViewById(R.id.etcontraseña);
        etNombre=(EditText)findViewById(R.id.etnombres);
        etDireccion=(EditText)findViewById(R.id.etdireccion);
        etciudad=(EditText)findViewById(R.id.etciudad);
        etestado=(EditText)findViewById(R.id.etestado);
        etcorreo=(EditText)findViewById(R.id.etcorreo);
        etTelefono=(EditText)findViewById(R.id.ettelefono);

        btnRegistrar=(Button)findViewById(R.id.registrar);
        btnRegresar=(Button)findViewById(R.id.cancelar);
        foto=(Button)findViewById(R.id.foto);

        imageView=(ImageView) findViewById(R.id.ImagenView);

        cliente = new AsyncHttpClient();

        botonAlmacenar();
        botonRegresar();

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });


    }

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones ={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Elige una Opcion");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Tomar Foto"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, COD_FOTO);
                }
                else
                {
                    if (opciones[which].equals("Elegir de Galeria"))
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
                    }else
                    {
                        dialog.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case COD_SELECCIONA:
                if (data==null)
                {
                    Toast.makeText(MainActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Uri miPath = data.getData();
                    //Toast.makeText(this, ""+miPath, Toast.LENGTH_LONG).show();
                    DireccionFoto= String.valueOf(miPath);
                    //miImagen.setImageBitmap(BitmapFactory.decodeFile(rutaImagen)); para poner la ruta de la imagen
                    //File imageFile = new File(miPath);
                    imageView.setImageURI(miPath);
                }
                break;

            case COD_FOTO:
                if (data==null)
                {
                    Toast.makeText(MainActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                }else
                {
                    Bitmap bitmap = (Bitmap)data.getExtras().get("data");

                    imageView.setImageBitmap(bitmap);
                    //convertir imagen
                    imageView.buildDrawingCache();
                    Bitmap bmap = imageView.getDrawingCache();

                    //guardar imagen
                    //Save savefile = new Save();
                    SaveImage(MainActivity.this, bmap);
                }
                break;
        }


    }

    private void botonAlmacenar()
    {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegistrar.setEnabled(false);
                if (etusuario.getText().toString().isEmpty() || etcontraseña.getText().toString().isEmpty() ||
                        etNombre.getText().toString().isEmpty() || etDireccion.getText().toString().isEmpty() ||
                        etciudad.getText().toString().isEmpty() || etestado.getText().toString().isEmpty() ||
                        etcorreo.getText().toString().isEmpty() || etTelefono.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Hay Campos Vacios", Toast.LENGTH_SHORT).show();
                    btnRegistrar.setEnabled(true);
                }else
                {
                    Usuario u = new Usuario();
                    u.setUsuario(etusuario.getText().toString());
                    u.setContraseña(etcontraseña.getText().toString());
                    u.setNombre(etNombre.getText().toString().replaceAll(" ", "%20"));
                    u.setDireccion(etDireccion.getText().toString().replaceAll(" ", "%20"));
                    u.setCiudad(etciudad.getText().toString().replaceAll(" ", "%20"));
                    u.setEstado(etestado.getText().toString().replaceAll(" ", "%20"));
                    u.setCorreo(etcorreo.getText().toString().replaceAll(" ", "%20"));
                    u.setTelefono(etTelefono.getText().toString());
                    u.setFoto(DireccionFoto.replaceAll(" ","%20"));
                    agregarUsuario(u);
                }
            }
        });
    }

    private void agregarUsuario(Usuario u)
    {
        String url = "https://localizadorazl1.000webhostapp.com/RegistroUsuarios.php?";
        String parametros = "usuario="+u.getUsuario()+"&contraseña="+u.getContraseña()+"&nombre="+u.getNombre()+"&direccion="+
                u.getDireccion()+"&ciudad="+u.getCiudad()+"&estado="+u.getEstado()+"&correo="+u.getCorreo()+"&telefono="+u.getTelefono()+
                "&foto="+u.getFoto()+"&tipo="+tipo;
        cliente.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200)
                {
                    Toast.makeText(MainActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    etusuario.setText("");
                    etcontraseña.setText("");
                    etNombre.setText("");
                    etDireccion.setText("");
                    etciudad.setText("");
                    etestado.setText("");
                    etcorreo.setText("");
                    etTelefono.setText("");
                    DireccionFoto="";
                    btnRegistrar.setEnabled(true);
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(MainActivity.this, "Registro fallido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SaveImage(Context context, Bitmap ImageToSave) {

        TheThis = context;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
        String CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, NameOfFile + CurrentDateAndTime + ".jpg");
        //Toast.makeText(this, ""+dir+"/"+NameOfFile+CurrentDateAndTime+".jpg", Toast.LENGTH_LONG).show();
        DireccionFoto=dir+"/"+NameOfFile+CurrentDateAndTime+".jpg";
        try {
            FileOutputStream fOut = new FileOutputStream(file);

            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        }

        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }

    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show();
    }

    private void botonRegresar()
    {
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
