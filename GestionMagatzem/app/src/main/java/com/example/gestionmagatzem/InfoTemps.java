package com.example.gestionmagatzem;

public class InfoTemps {

    private String imIconoTiempo;
    private String temperaturaGrados;
    private String temperaturaMin;
    private String temperaturaMax;
    private String sensacio;
    private String tiempoTexto;
    private String humedad;
    private String viento;
    private String descripcio;

    public InfoTemps(String imgTiempo, double Grados, double temperaturaMin, double temperaturaMax, double sensacio, String TempText, String Humitat, double Vent, String descripcio) {
        this.imIconoTiempo = imgTiempo;
        this.temperaturaGrados = conversioTemps(Grados);
        this.temperaturaMin = conversioTemps(temperaturaMin);
        this.temperaturaMax = conversioTemps(temperaturaMax);
        this.sensacio = conversioTemps(sensacio);
        this.tiempoTexto = TempText;
        this.humedad = Humitat;
        this.viento = String.valueOf(Vent);
        this.descripcio = descripcio;
    }

    public String conversioTemps(double temperatura) {

        temperatura = temperatura - 273.15;
        int intTiempo = (int) temperatura;
        String sTiempo = String.valueOf(intTiempo);

        return sTiempo;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public String getImIconoTiempo() {
        return imIconoTiempo;
    }

    public String getTemperaturaGrados() {
        return temperaturaGrados;
    }

    public String getTemperaturaMin() {
        return temperaturaMin;
    }

    public String getTemperaturaMax() {
        return temperaturaMax;
    }

    public String getSensacio() {
        return sensacio;
    }

    public String getTiempoTexto() {
        return tiempoTexto;
    }

    public String getHumedad() {
        return humedad;
    }

    public String getViento() {
        return viento;
    }
}
