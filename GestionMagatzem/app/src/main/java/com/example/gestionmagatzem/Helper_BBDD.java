package com.example.gestionmagatzem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Helper_BBDD extends SQLiteOpenHelper {

    // Versio de la base de dades
    private static final int database_VERSION = 2;

    // Nom base de dades
    private static final String database_NAME = "MagatzemDatabase";


    public Helper_BBDD(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }

    //Taula per els articles
    private String CREATE_MAGATZEM =
            "CREATE TABLE Articles_Magatzem ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "codi TEXT," +
                    "descripcion TEXT," +
                    "pvp REAL," +
                    "estoc REAL)";


    //Taula per els Moviments
    private String CREATE_MOVIMENT =
            "CREATE TABLE Moviment (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "codiArticle TEXT," +
                    "dia TEXT," +
                    "quantitat INTEGER," +
                    "tipus TEXT," +
                    "articleId INTEGER," +
                    "FOREIGN KEY(articleId) REFERENCES Articles_Magatzem(_id))";


    // Crear la base de dades amb el seu format en especific
    @Override
    public void onCreate(SQLiteDatabase db){

        db.execSQL(CREATE_MAGATZEM);
        db.execSQL(CREATE_MOVIMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d("oldVersion", String.valueOf(oldVersion));
        Log.d("newVersion", String.valueOf(newVersion));

        if(oldVersion < 2) {
            db.execSQL(CREATE_MOVIMENT);
        }
    }

}
