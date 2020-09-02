package com.example.gestionmagatzem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Articles extends AppCompatActivity {

    private long idTask;
    private ArticleDataSource bd;

    TextView tvIdAfegir, tvIdActualitzar, tvDescripcio, tvPVP, tvStock, textViewEstoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_article);

        bd = new ArticleDataSource(this);
        idTask = this.getIntent().getExtras().getLong("id");

        tvIdActualitzar = findViewById(R.id.tvID_articulo);
        tvIdAfegir =  findViewById(R.id.edtID_Articulo);
        tvDescripcio =  findViewById(R.id.edtDescripcion);
        tvPVP =  findViewById(R.id.edtPVP);
        tvStock = findViewById(R.id.edtESTOC);
        textViewEstoc = findViewById(R.id.textViewEstoc);

        setTitle("Actualitzar Articles");

        // Boton Aceptar
        Button btnAceptar = (Button) findViewById(R.id.btnOk);
        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aceptarCambios();
            }
        });



        // Boton Cancelar
        Button  btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelarCambios();
            }
        });


        // Comprovem si estem actualitzant o afegint un article
        if (idTask != -1) {
            cargarDatos();
            tvIdAfegir.setVisibility(View.GONE);
        }
        else {
            tvIdActualitzar.setVisibility(View.GONE);
            textViewEstoc.setVisibility(View.GONE);
            tvStock.setVisibility(View.GONE);
        }
    }

    // Retorn al main, sense fer res
    private void cancelarCambios() {
        Intent mIntent = new Intent();
        mIntent.putExtra("id", idTask);
        setResult(RESULT_CANCELED, mIntent);

        finish();
    }

    // Validar i acceptar els canvis en l'article
    private void aceptarCambios() {

        String id, descripcion;
        float pvp;
        int stock;

        descripcion = tvDescripcio.getText().toString();
        id =  tvIdAfegir.getText().toString();

        //Comprovar si la descripcio esta buida
        if (descripcion.isEmpty()){
            Toast.makeText(this, "La descripció no pot estar buida", Toast.LENGTH_SHORT).show();
           return;
        }
        else {
           descripcion = tvDescripcio.getText().toString();
        }

        //Comprovar si el PVP esta buit o han introduit dades incorrectes
        try{
            String pvpC = tvPVP.getText().toString().replaceAll(",", ".");
            pvp =  Float.valueOf(pvpC);
        }
        catch (Exception e){
            Toast.makeText(this, "El camp PVP està buit o la informació és incorrecte", Toast.LENGTH_SHORT).show();
            return;
        }

        // Comprovem si estem actualitzant o afegint un article
        if (idTask == -1) {

            //Comprovar si ja existeix el id
            EditText ed = findViewById(R.id.edtID_Articulo);
            Cursor c = bd.CodiRepetit(ed.getText().toString());
            if (c.getCount() > 0) {
                Toast.makeText(this, "Ja existeix un article amb aquest id", Toast.LENGTH_SHORT).show();
                return;
            }

            // Afegir l'article a la base de dades
            bd.afegirArticle(id,descripcion,pvp);
        }
        else {


            // Comprovar que el id no esigui buit
            try{
                id =  tvIdAfegir.getText().toString();
            }
            catch (Exception e) {
                Toast.makeText(this, "El camp ID està buit", Toast.LENGTH_SHORT).show();
                return;
            }

            //Actualitzar l'article
            bd.actualitzarArticle(idTask,descripcion,pvp);
        }

        Intent mIntent = new Intent();
        mIntent.putExtra("id", idTask);
        setResult(RESULT_OK, mIntent);

        finish();
    }

    //Cargar les dades amb un cursor on nomes hi ha un sol registre
    private void cargarDatos() {

        Cursor datos = bd.article_unic(idTask);
        datos.moveToFirst();

        // Carreguem les dades en la interfície
        TextView tv;

        tvIdActualitzar.setText(datos.getString(datos.getColumnIndex(ArticleDataSource.ARTICLE_CODI)));

        tv = findViewById(R.id.edtDescripcion);
        tv.setText(datos.getString(datos.getColumnIndex(ArticleDataSource.ARTICLE_DESCRIPCION)));

        tv = findViewById(R.id.edtPVP);
        tv.setText(datos.getString(datos.getColumnIndex(ArticleDataSource.ARTICLE_PVP)));

        tv = findViewById(R.id.edtESTOC);
        tv.setKeyListener(null);
        tv.setText(datos.getString(datos.getColumnIndex(ArticleDataSource.ARTICLE_ESTOC)));
    }
}
