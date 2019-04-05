package com.example.lookup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class LisLocalizados extends Fragment {

    private ListView lista;
    private int id;
    private AsyncHttpClient cliente;
    private Button regresar;
    private int tipo;
    private Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lis_localizados, container, false);

        lista=(ListView)v.findViewById(R.id.listView);
        //regresar=(Button)v.findViewById(R.id.btnregresar);
        cliente = new AsyncHttpClient();

        Bundle args = getArguments();
        id = getArguments().getInt("idloc");
        tipo = getArguments().getInt("tipo");

        final Handler handler = new Handler();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                obtenerLocalizados1();
                handler.postDelayed(this,10000);
            }
        }, 1000);

        //regresar();
        return v;
    }

    private void obtenerLocalizados1()
    {
        String url = "https://localizadorazl1.000webhostapp.com/ObtenerLocalizados.php?";
        String param = "idloc="+id;
        cliente.post(url + param, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200)
                {
                    listarUsuarios1(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    private void listarUsuarios1(String respuesta)
    {
        final ArrayList<localizados> listado = new ArrayList<localizados>();
        try {
            JSONArray jsonArreglo = new JSONArray(respuesta);
            for (int i=0; i<jsonArreglo.length(); i++)
            {
                localizados l = new localizados();
                l.setIdloc(jsonArreglo.getJSONObject(i).getInt("idloc"));
                l.setUsuarioloc1(jsonArreglo.getJSONObject(i).getString("usuarioloc"));
                l.setContraseñaloc1(jsonArreglo.getJSONObject(i).getString("contraseñaloc"));
                l.setNombreloc1(jsonArreglo.getJSONObject(i).getString("nombre"));
                l.setTelefonoloc1(jsonArreglo.getJSONObject(i).getString("telefono"));
                l.setLongitud(jsonArreglo.getJSONObject(i).getDouble("longitud"));
                l.setLatitud(jsonArreglo.getJSONObject(i).getDouble("latitud"));
                l.setDireccionloc(jsonArreglo.getJSONObject(i).getString("direccion"));
                l.setFoto1(jsonArreglo.getJSONObject(i).getString("fotoloc"));
                listado.add(l);
                adapter = new Adapter(getContext(), listado);

            }
            lista.setAdapter(adapter);
            //ArrayAdapter<localizados> a = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listado);
            //lista.setAdapter(a);
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    switch (tipo)
                    {
                        case 1:
                            localizados lo = listado.get(position);
                            Intent intent = new Intent(getActivity(), MapsActivity.class);
                            intent.putExtra("latitud", lo.getLatitud());
                            intent.putExtra("longitud", lo.getLongitud());
                            intent.putExtra("direccion", lo.getDireccionloc());
                            getActivity().startActivity(intent);
                            break;
                        case 2:
                            AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
                            dialogo.setTitle("Importante");
                            dialogo.setMessage("Desea eliminar este dispositivo?");
                            dialogo.setCancelable(false);
                            dialogo.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    localizados lo1 = listado.get(position);
                                    String url = "https://localizadorazl1.000webhostapp.com/eliminarDispositivo.php?";
                                    String parametros="idloc="+lo1.getIdloc()+"&usuarioloc="+lo1.getUsuarioloc1()+"&contraseñaloc="+lo1.getContraseñaloc1();
                                    cliente.post(url + parametros, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                            if (statusCode==200)
                                            {
                                                Toast.makeText(getActivity(), "Dispositivo Eliminado Correctamente", Toast.LENGTH_SHORT).show();
                                                obtenerLocalizados1();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                            Toast.makeText(getActivity(), "Error al Eliminar", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            dialogo.show();
                            break;
                        case 3:
                            localizados lo2 = listado.get(position);

                            AgregarDisp lis = new AgregarDisp();
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", lo2.getIdloc());
                            bundle.putString("usuario", lo2.getUsuarioloc1());
                            bundle.putString("contraseña", lo2.getContraseñaloc1());
                            bundle.putString("nombre", lo2.getNombreloc1());
                            bundle.putString("telefono", lo2.getTelefonoloc1());
                            bundle.putString("foto", lo2.getFoto1());
                            bundle.putInt("dato",2);
                            lis.setArguments(bundle);

                            FragmentManager fm =getFragmentManager();
                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                            fragmentTransaction.add(R.id.contenedor, lis, null);
                            fragmentTransaction.commit();
                            /*Intent intent1 = new Intent(getActivity(), RegDispositivos.class);
                            intent1.putExtra("id", lo2.getIdloc());
                            intent1.putExtra("usuario", lo2.getUsuarioloc1());
                            intent1.putExtra("contraseña", lo2.getContraseñaloc1());
                            intent1.putExtra("nombre", lo2.getNombreloc1());
                            intent1.putExtra("telefono", lo2.getTelefonoloc1());
                            intent1.putExtra("dato", 2);
                            LisLocalizados.this.startActivity(intent1);*/
                            getFragmentManager().beginTransaction().remove(LisLocalizados.this).commit();
                            break;
                    }

                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*public void regresar()
    {
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getActivity().finish();
                getFragmentManager().beginTransaction().remove(LisLocalizados.this).commit();
            }
        });
    }*/
}
