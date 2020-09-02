package com.example.gestionmagatzem;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class Historial extends AppCompatActivity {



    private static ArticleDataSource bd;

    private adapterTodoListFilterHistorial scTasks;

    private static String[] from = new String[]{bd.MOVIMENT_CODI,bd.MOVIMENT_DATA, bd.MOVIMENT_QUANTITAT, bd.MOVIMENT_TIPUS};
    private static int[] to = new int[]{R.id.lblCodigo, R.id.lblData, R.id.lblQuantitat, R.id.lblTipusOperacio};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);



        Bundle bundle = getIntent().getExtras();



        setTitle("Historial");

        bd = new ArticleDataSource(this);
        cargarDades();
    }

    private void cargarDades() {

        // Demanem totes les tasques
        Cursor cursorMoviments = bd.tots_moviments();

        scTasks = new adapterTodoListFilterHistorial(this, R.layout.moviment, cursorMoviments, from, to, 1);

        ListView lvMoviments = findViewById(R.id.historial);

        lvMoviments.setAdapter(scTasks);
    }
}

class adapterTodoListFilterHistorial extends android.widget.SimpleCursorAdapter {

    public adapterTodoListFilterHistorial(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);


        return view;
    }
}
