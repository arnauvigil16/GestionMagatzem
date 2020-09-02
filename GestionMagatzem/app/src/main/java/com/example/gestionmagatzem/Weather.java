package com.example.gestionmagatzem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.entity.mime.Header;

public class Weather extends AppCompatActivity {


    AsyncHttpClient client = new AsyncHttpClient();
    Dialog dialogo;

    EditText edtCiutat;

    TextView separador1;
    TextView separador2;
    TextView tvTemperaturaGrados;
    TextView tvTempText;
    TextView tvDescripcion;
    TextView tvSensacio;
    TextView tvTemperaturaMax;
    TextView tvTemperaturaMin;
    TextView tvPresioAire;
    TextView tvHumitat;

    TextView tvSensacioTitle;
    TextView tvTempMaxTitle;
    TextView tvTempMinTitle;
    TextView tvPresioTitle;
    TextView tvHumitatTitle;

    ImageView imIconoTiempo;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // deshabilita la barra de notificacions
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edtCiutat = findViewById(R.id.edtCiutat);


        separador1 = findViewById(R.id.tvSeparador1);
        separador2 = findViewById(R.id.tvSeparador2);
        tvTemperaturaGrados = findViewById(R.id.tvTemperatura);
        tvTempText = findViewById(R.id.tvTempText);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvSensacio = findViewById(R.id.tvSensacio);
        tvTemperaturaMax = findViewById(R.id.tvTemperaturaMax);
        tvTemperaturaMin = findViewById(R.id.tvTemperaturaMin);
        tvPresioAire = findViewById(R.id.tvPresioAire);
        tvHumitat = findViewById(R.id.tvHumitat);

        imIconoTiempo = findViewById(R.id.imIconoTiempo);


        tvSensacioTitle = findViewById(R.id.tvSensacioTitle);
        tvTempMaxTitle = findViewById(R.id.tvTempMaxTitle);
        tvTempMinTitle = findViewById(R.id.tvTempMinTitle);
        tvPresioTitle = findViewById(R.id.tvPresioTitle);
        tvHumitatTitle = findViewById(R.id.tvHumitatTitle);


        tvSensacioTitle.setVisibility(View.GONE);
        tvTempMaxTitle.setVisibility(View.GONE);
        tvTempMinTitle.setVisibility(View.GONE);
        tvPresioTitle.setVisibility(View.GONE);
        tvHumitatTitle.setVisibility(View.GONE);
        separador1.setVisibility(View.GONE);
        separador2.setVisibility(View.GONE);





        Button btnAceptarCiutat = findViewById(R.id.btnAceptarCiutat);
        btnAceptarCiutat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                respostaApi();
            }
        });
    }

    public void cargarDades(InfoTemps tiempoClass){

       URL url = null;

        try {
            url = new URL("https://openweathermap.org/img/wn/"+ tiempoClass.getImIconoTiempo() +"@2x.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        imIconoTiempo.setImageBitmap(bmp);


        tvTemperaturaGrados.setText(tiempoClass.getTemperaturaGrados() + "ºC");
        tvTemperaturaMax.setText(tiempoClass.getTemperaturaMax() + "ºC");
        tvTemperaturaMin.setText(tiempoClass.getTemperaturaMin() + "ºC");
        tvSensacio.setText(tiempoClass.getSensacio() + "ºC");
        tvTempText.setText(tiempoClass.getTiempoTexto());
        tvHumitat.setText(tiempoClass.getHumedad() + "%");
        tvPresioAire.setText(tiempoClass.getViento() + " Km/h");
        tvDescripcion.setText(tiempoClass.getDescripcio());

        tvSensacioTitle.setVisibility(View.VISIBLE);
        tvTempMaxTitle.setVisibility(View.VISIBLE);
        tvTempMinTitle.setVisibility(View.VISIBLE);
        tvPresioTitle.setVisibility(View.VISIBLE);
        tvHumitatTitle.setVisibility(View.VISIBLE);
        separador1.setVisibility(View.VISIBLE);
        separador2.setVisibility(View.VISIBLE);


    }


    private void respostaApi(){

        //Dialog
        dialogo = new ProgressDialog(this);
        dialogo.setCancelable(false);
        dialogo.setCanceledOnTouchOutside(false);

        client.setMaxRetriesAndTimeout(0,10000);


        String Url = "https://api.openweathermap.org/data/2.5/weather?q=" + edtCiutat.getText().toString() + "&appid=7a88a0039bd7ade0a5894c796a39be17";
        client.get(this,Url, new AsyncHttpResponseHandler() {


            public void onStart() {
                ((ProgressDialog) dialogo).setMessage("Descargando Datos...");
                dialogo.show();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                ((ProgressDialog) dialogo).setMessage("Procesando Datos...");

                JSONObject weather;
                String str = new String(responseBody);
                InfoTemps infoCiutat;

                try {
                    weather = new JSONObject(str);

                    String TempText = weather.getJSONArray("weather").getJSONObject(0).getString("main");
                    String Icono = weather.getJSONArray("weather").getJSONObject(0).getString("icon");
                    String Descripcion = weather.getJSONArray("weather").getJSONObject(0).getString("description");
                    String Humitat = weather.getJSONObject("main").getString("humidity");
                    Double Grados = weather.getJSONObject("main").getDouble("temp");
                    Double GradosMin = weather.getJSONObject("main").getDouble("temp_min");
                    Double GradosMax = weather.getJSONObject("main").getDouble("temp_max");
                    Double Sensacio = weather.getJSONObject("main").getDouble("feels_like");
                    Double Viento = weather.getJSONObject("wind").getDouble("speed");


                    infoCiutat = new InfoTemps(Icono, Grados, GradosMin, GradosMax, Sensacio, TempText, Humitat, Viento, Descripcion);

                    cargarDades(infoCiutat);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialogo.hide();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                String str = (error.getMessage());
                String valor = "No se ha podido descargar los datos desde el servidor. \n\n" + str;

                dialogo.hide();

                Toast.makeText(getApplicationContext(), valor, Toast.LENGTH_LONG).show();


            }

        });
    }

}
