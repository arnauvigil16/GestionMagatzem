package com.example.gestionmagatzem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static ArticleDataSource bd;

    private adapterTodoListFilter scTasks;

    private static int ACTIVITY_TASK_ADD = 1;
    private static int ACTIVITY_TASK_UPDATE = 2;
    private static int ACTIVITY_TASK_MODIFICAR_STOCK = 3;

    private long idActual;

    private static String[] from = new String[]{bd.ARTICLE_CODI, bd.ARTICLE_DESCRIPCION, bd.ARTICLE_ESTOC};
    private static int[] to = new int[]{R.id.lblTitulo, R.id.lblData, R.id.lblStock};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Magatzem");


        bd = new ArticleDataSource(this);
        cargarDades();



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);
        return true;
    }

    // Opcions per escollir
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnuStock:
                filterStock();
                return true;

            case R.id.mnuSinStock:
                filterNoStock();
                return true;

            case R.id.mnuAll:
                todosArticulos();
                return true;

            case R.id.mnuAfegir:
                afegirArticulo();
                return true;

            case R.id.mnuBuscar:
                historial();
                return true;

            case R.id.mnuTiempo:
                Intent intentTiempo = new Intent(this, Weather.class );
                startActivity(intentTiempo);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Metode per filtrar els articles amb stock
    private void filterStock() {
        Cursor cursorTasks = bd.articleStock();
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();
    }

    // Metode per filtrar els articles sense stock
    private void filterNoStock() {
        Cursor cursorTasks = bd.articleNoStock();
        scTasks.changeCursor(cursorTasks);
        scTasks.notifyDataSetChanged();
    }


    //Metode per mostrar totes les tasques
    private void todosArticulos() {
        Cursor cursorArticles = bd.tots_articles();
        scTasks.changeCursor(cursorArticles);
        scTasks.notifyDataSetChanged();
    }

    public void modificarStock(int codi_operacio, long idTask, int stock, String id_Articulo){

        Bundle bundle = new Bundle();
        bundle.putLong("identificador",idTask);
        bundle.putInt("codi_operacio", codi_operacio);
        bundle.putString("id_articulo", id_Articulo);
        bundle.putInt("stock", stock);

        Intent intentMoviments = new Intent(this, Moviments.class );

        intentMoviments.putExtras(bundle);

        startActivityForResult(intentMoviments,ACTIVITY_TASK_MODIFICAR_STOCK);
    }


    public void historial() {
        Intent intentMoviments = new Intent(this, Historial.class );
        startActivity(intentMoviments);
    }

    //Afegir una tasca, enviant com a id -1, per que sapiga que estem afegint i no modificant
    private void afegirArticulo() {
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent intentArticles = new Intent(this, Articles.class );
        intentArticles.putExtras(bundle);
        startActivityForResult(intentArticles,ACTIVITY_TASK_ADD);
    }

    //Actualitzar una tasca en concret
    private void actualitzarArticle(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        idActual = id;

        Intent intentArticles = new Intent(this, Articles.class );
        intentArticles.putExtras(bundle);
        startActivityForResult(intentArticles,ACTIVITY_TASK_UPDATE);
    }

    //Carregar tots els articles
    private void cargarDades() {

        // Demanem totes les tasques
        Cursor cursorTasks = bd.tots_articles();
        scTasks = new adapterTodoListFilter(this, R.layout.article, cursorTasks, from, to, 1);

        ListView lvDades = findViewById(R.id.llista);

        lvDades.setAdapter(scTasks);

        //Quan es faci click a un article, anar a modificarlo
        lvDades.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                        actualitzarArticle(id);
                }
                }
        );
    }






    //Eliminar un article
    public void deleteTask(final long identificador) {

        // Preguntem si esta segur
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Eliminar l'article?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                bd.eliminarArticle(identificador);

                todosArticulos();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }





    //Accions a fer quan una activity s'acabi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_TASK_ADD) {
            if (resultCode == RESULT_OK) {
                todosArticulos();
            }
        }

        if (requestCode == ACTIVITY_TASK_UPDATE) {
            if (resultCode == RESULT_OK) {
                todosArticulos();
            }
        }

        if (requestCode == ACTIVITY_TASK_MODIFICAR_STOCK){
            if (resultCode == RESULT_OK){
                todosArticulos();
            }
        }
    }



}

class adapterTodoListFilter extends android.widget.SimpleCursorAdapter {

    public adapterTodoListFilter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        MainActivity = (MainActivity)context;
    }

    private static MainActivity MainActivity;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        Cursor linia = (Cursor) getItem(position);

        final int stock = linia.getInt(linia.getColumnIndexOrThrow(ArticleDataSource.ARTICLE_ESTOC));
        final long identificadorArticulo = linia.getLong(linia.getColumnIndexOrThrow(ArticleDataSource.ARTICLE_ID));
        final String codiArticle = linia.getString(linia.getColumnIndexOrThrow(ArticleDataSource.ARTICLE_CODI));

        ImageView imgEliminar = view.findViewById(R.id.imgEliminar);
        ImageView imgAumentar = view.findViewById(R.id.imgAumentarStock);
        ImageView imgRestar  = view.findViewById(R.id.imgRestar);

        imgAumentar.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent().getParent().getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                MainActivity.modificarStock( 0,  identificadorArticulo , stock,  codiArticle);
            }
        });


        imgRestar.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent().getParent().getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                MainActivity.modificarStock( 1,  identificadorArticulo , stock,  codiArticle);
            }
        });


        imgEliminar.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                MainActivity.deleteTask(identificadorArticulo);


            }
        });

        // Pintem el fons de la view segons està completada o no
        if (stock <= 0) {
            view.setBackgroundColor(Color.parseColor("#FFB8B8"));
        }
        else {
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        return view;
    }


}