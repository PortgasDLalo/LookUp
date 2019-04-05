package com.example.lookup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class AgregarDisp extends Fragment {
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    private EditText usuario;
    private EditText contraseña;
    private EditText nombre;
    private EditText telefono;
    private Button agregar;
    private Button cancelar;
    private int id;
    private int id2;
    private int tipo=2;
    private int dato;
    private ImageView imageView1;
    private Button foto1;
    private String DireccionFoto;


    private Context TheThis;
    private String NameOfFolder = "/LookUp";
    private String NameOfFile = "imagen";

    private AsyncHttpClient cliente;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_agregar_disp, container, false);

        usuario=(EditText)v.findViewById(R.id.usuarioloc);
        contraseña=(EditText)v.findViewById(R.id.contraseñaloc);
        nombre=(EditText)v.findViewById(R.id.nombreloc);
        telefono=(EditText)v.findViewById(R.id.telefonoloc);
        agregar=(Button) v.findViewById(R.id.btnagregarloc);
        cancelar=(Button) v.findViewById(R.id.btncanceloc);
        cliente = new AsyncHttpClient();

        Bundle args = getArguments();
        id2 = getArguments().getInt("idloc");
        dato = getArguments().getInt("dato");

        foto1=(Button)v.findViewById(R.id.foto1);

        imageView1=(ImageView) v.findViewById(R.id.fotodis);

        if (dato==1)
        {
            agregar.setText("Agregar");
        }
        else if (dato==2)
        {
            agregar.setText("Modificar");
            id=getArguments().getInt("id");
            usuario.setText(getArguments().getString("usuario"));
            contraseña.setText(getArguments().getString("contraseña"));
            nombre.setText(getArguments().getString("nombre"));
            telefono.setText(getArguments().getString("telefono"));
            DireccionFoto = getArguments().getString("foto");
            if (DireccionFoto.startsWith("content:"))
            {

                imageView1.setImageURI(Uri.parse(DireccionFoto));
            }else if (DireccionFoto.startsWith("/storage"))
            {
                Bitmap bitmap = BitmapFactory.decodeFile(DireccionFoto);
               imageView1.setImageBitmap(bitmap);
            }
            usuario.setEnabled(false);
        }
        agregarUsuarioloc();
        cancelar1();

        foto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });
        return v;
    }

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones ={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case COD_SELECCIONA:
                if (data==null)
                {
                    Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Uri miPath = data.getData();
                    //Toast.makeText(this, ""+miPath, Toast.LENGTH_LONG).show();
                    DireccionFoto= String.valueOf(miPath);
                    //miImagen.setImageBitmap(BitmapFactory.decodeFile(rutaImagen)); para poner la ruta de la imagen
                    //File imageFile = new File(miPath);
                    imageView1.setImageURI(miPath);
                }
                break;

            case COD_FOTO:
                if (data==null)
                {
                    Toast.makeText(getActivity(), "Cancelado", Toast.LENGTH_SHORT).show();
                }else
                {
                    Bitmap bitmap = (Bitmap)data.getExtras().get("data");

                    imageView1.setImageBitmap(bitmap);
                    //convertir imagen
                    imageView1.buildDrawingCache();
                    Bitmap bmap = imageView1.getDrawingCache();

                    //guardar imagen
                    //Save savefile = new Save();
                    SaveImage(getActivity(), bmap);
                }
                break;
        }


    }

    private void agregarUsuarioloc()
    {
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregar.setEnabled(false);
                if (usuario.getText().toString().isEmpty() || contraseña.getText().toString().isEmpty() || nombre.getText().toString().isEmpty()
                        || telefono.getText().toString().isEmpty() || DireccionFoto.equals(""))
                {
                    Toast.makeText(getActivity(), "Hay Campos Vacios", Toast.LENGTH_SHORT).show();
                    agregar.setEnabled(true);
                }else
                {
                    usuarioloc usu = new usuarioloc();
                    usu.setUsuarioloc(usuario.getText().toString().replace(" ", "%20"));
                    usu.setContraseñaloc(contraseña.getText().toString().replace(" ", "%20"));
                    usu.setNombreloc(nombre.getText().toString().replace(" ", "%20"));
                    usu.setTelefonoloc(telefono.getText().toString().replace(" ", "%20"));
                    usu.setFoto1(DireccionFoto.replaceAll(" ", "%20"));
                    switch (dato)
                    {
                        case 1:
                            addusuario(usu);
                            break;
                        case 2:
                            modificarDispositivo(usu);
                            break;
                    }
                }
            }
        });
    }

    private void addusuario(usuarioloc usu)
    {
        String url="https://localizadorazl1.000webhostapp.com/adduserloc.php?";
        String parametros="idloc="+id2+"&usuarioloc="+usu.getUsuarioloc()+"&contraseñaloc="+usu.getContraseñaloc()+
                "&nombre="+usu.getNombreloc()+"&telefono="+usu.getTelefonoloc()+"&longitud="+null+"&latitud="+null+"&direccion="+null+
                "&tipo="+tipo+"&fotoloc="+DireccionFoto;
        cliente.post(url + parametros, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                usuario.setText("");
                contraseña.setText("");
                nombre.setText("");
                telefono.setText("");
                DireccionFoto="";

                getFragmentManager().beginTransaction().remove(AgregarDisp.this).commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Registro Fallido", Toast.LENGTH_SHORT).show();
                agregar.setEnabled(true);
            }
        });

    }

    private void modificarDispositivo(usuarioloc usu)
    {
        String url="https://localizadorazl1.000webhostapp.com/modificarDispositivos.php?";
        String para="idloc="+id+"&usuarioloc="+usu.getUsuarioloc()+"&contraseñaloc="+usu.getContraseñaloc()+"&nombre="+usu.getNombreloc()+"&telefono="+usu.getTelefonoloc()+
                "&fotoloc="+DireccionFoto;
        cliente.post(url + para, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getActivity(), "Modificación Exitosa", Toast.LENGTH_SHORT).show();
                usuario.setText("");
                contraseña.setText("");
                nombre.setText("");
                telefono.setText("");
                DireccionFoto="";
                getFragmentManager().beginTransaction().remove(AgregarDisp.this).commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Modificación Fallida", Toast.LENGTH_SHORT).show();
                agregar.setEnabled(true);
            }
        });
    }
    private void cancelar1()
    {
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().remove(AgregarDisp.this).commit();
            }
        });
    }

    private void SaveImage(Context context, Bitmap ImageToSave) {

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
}
