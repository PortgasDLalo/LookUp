package com.example.lookup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {

    private Context context;
    private ArrayList<localizados> arrayList;

    public Adapter(Context context, ArrayList<localizados> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item,null);
        }
        ImageView foto = (ImageView)convertView.findViewById(R.id.fotodispositivo);
        TextView nombre = (TextView)convertView.findViewById(R.id.tv_nombre);
        TextView telefono = (TextView)convertView.findViewById(R.id.tv_telefono);
        TextView direccion = (TextView)convertView.findViewById(R.id.tv_direccion);

        String foto2 = arrayList.get(position).getFoto1();
        if (foto2.startsWith("content:"))
        {

            foto.setImageURI(Uri.parse(foto2));
        }else if (foto2.startsWith("/storage"))
        {
            Bitmap bitmap = BitmapFactory.decodeFile(foto2);
            foto.setImageBitmap(bitmap);
        }
        nombre.setText(arrayList.get(position).getNombreloc1());
        telefono.setText(arrayList.get(position).getTelefonoloc1());
        direccion.setText(arrayList.get(position).getDireccionloc());
        return convertView;
    }
}
