package com.example.gestionmagatzem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Moviments extends AppCompatActivity {

    CalendarView calendarView;
    TextView tvCalendar, tvId;
    EditText edtStockModificar;
    Button btnAceptarStock;
    String date, id;
    long idTask;

    private ArticleDataSource bd;
    int cantitatStock;
    int codi_operacio, stockFinal, stockOriginal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviments);

        bd = new ArticleDataSource(this);

        Bundle bundle = getIntent().getExtras();

        setTitle("Actualitzar Estoc");

        codi_operacio = bundle.getInt("codi_operacio");
        idTask = bundle.getLong("identificador");
        id = bundle.getString("id_articulo");
        stockOriginal = bundle.getInt("stock");

        calendarView = findViewById(R.id.clCalendario);
        tvCalendar = findViewById(R.id.tvCalendar);
        tvId = findViewById(R.id.tvIdentificador);
        btnAceptarStock = findViewById(R.id.btnAceptarStock);
        edtStockModificar = findViewById(R.id.edtCantitat);


        tvId.setText(id);

        btnAceptarStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprovarDades();
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "/" +  (month + 1) + "/" + dayOfMonth;
            }
        });


    }

    public void comprovarDades(){

        try {
           date.isEmpty();
        }
        catch (Exception e){

            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            date = year + "/" + month + "/" + day;
        }

        String stockModificar = edtStockModificar.getText().toString();

        try {
            cantitatStock = Integer.parseInt(stockModificar);
        }
        catch(Exception e) {
            Toast.makeText(this, "El camp del estoc no pot estar buid", Toast.LENGTH_SHORT).show();
            return;
        }

        if (codi_operacio == 0) {
            stockFinal = stockOriginal + cantitatStock;

            try {
                bd.actualitzarStock(idTask, stockFinal);
                bd.afegirMoviment(id, date, cantitatStock, "E");
            }
            catch(Exception e){
                Toast.makeText(this, "No s'ha pogut introduir les dades correctament", Toast.LENGTH_SHORT).show();
            }

        }
        else if(codi_operacio == 1){

            stockFinal = stockOriginal - cantitatStock;

            try {
                bd.actualitzarStock(idTask, stockFinal);
                bd.afegirMoviment(id, date, cantitatStock, "S");
            }
            catch(Exception e){
                Toast.makeText(this, "No s'ha pogut introduir les dades correctament", Toast.LENGTH_SHORT).show();
            }
        }

        Intent mIntent = new Intent();

        setResult(RESULT_OK, mIntent);

        finish();
    }
}
