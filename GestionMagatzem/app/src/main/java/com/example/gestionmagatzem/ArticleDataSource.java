package com.example.gestionmagatzem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ArticleDataSource {

    public static final String TABLE_MAGATZEM = "Articles_Magatzem";
    public static final String ARTICLE_ID = "_id";
    public static final String ARTICLE_CODI = "codi";
    public static final String ARTICLE_DESCRIPCION = "descripcion";
    public static final String ARTICLE_PVP = "pvp";
    public static final String ARTICLE_ESTOC = "estoc";

    public static final String TABLE_MOVIMENTS = "Moviment";
    public static final String MOVIMENT_ID = "_id";
    public static final String MOVIMENT_CODI = "codiArticle";
    public static final String MOVIMENT_DATA = "dia";
    public static final String MOVIMENT_QUANTITAT = "quantitat";
    public static final String MOVIMENT_TIPUS = "tipus";
    public static final String MOVIMENT_ARTICLEID = "articleId";



    private Helper_BBDD dbHelper;
    private SQLiteDatabase dbW, dbR;


    //Constructor
    public ArticleDataSource(Context ctx) {
        dbHelper = new Helper_BBDD(ctx);

        dbW = dbHelper.getWritableDatabase();
        dbR = dbHelper.getReadableDatabase();
    }

    //Metode per retornar tots els articles de la base de dades
    public Cursor tots_articles() {
        return dbR.query(TABLE_MAGATZEM, new String[]{ARTICLE_ID,ARTICLE_CODI,ARTICLE_DESCRIPCION,ARTICLE_PVP,ARTICLE_ESTOC},
                null, null,
                null, null, ARTICLE_ID);
    }


    //Metode per retornar tots els moviments de la base de dades
    public Cursor tots_moviments() {
        return dbR.query(TABLE_MOVIMENTS, new String[]{MOVIMENT_ID,MOVIMENT_CODI,MOVIMENT_DATA,MOVIMENT_QUANTITAT,MOVIMENT_TIPUS},
                null, null,
                null, null, MOVIMENT_CODI);
    }

    //Metode per retornar una tasca concreta
    public Cursor article_unic(long id) {
        return dbR.query(TABLE_MAGATZEM, new String[]{ARTICLE_ID,ARTICLE_CODI,ARTICLE_DESCRIPCION,ARTICLE_PVP,ARTICLE_ESTOC},
                ARTICLE_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
    }

    //Metode per que no es repeteixi el codi
    public Cursor CodiRepetit(String itemCode) {
        return dbR.query(TABLE_MAGATZEM, new String[]{ARTICLE_ID,ARTICLE_CODI,ARTICLE_DESCRIPCION,ARTICLE_PVP,ARTICLE_ESTOC},
                ARTICLE_CODI+ "=?", new String[]{String.valueOf(itemCode)},
                null, null, null);
    }

    //Metode per retornar els articles amb stock
    public Cursor articleStock() {
        return dbR.query(TABLE_MAGATZEM, new String[]{ARTICLE_ID,ARTICLE_CODI,ARTICLE_DESCRIPCION,ARTICLE_PVP,ARTICLE_ESTOC},
                ARTICLE_ESTOC + ">" + 0, null,
                null, null, ARTICLE_ID);
    }

    //Metode per retornar els articles sense stock
    public Cursor articleNoStock() {
        return dbR.query(TABLE_MAGATZEM, new String[]{ARTICLE_ID,ARTICLE_CODI,ARTICLE_DESCRIPCION,ARTICLE_PVP,ARTICLE_ESTOC},
                ARTICLE_ESTOC + "<=" + 0, null,
                null, null, ARTICLE_ID);
    }











    //Metode per afegir un article a la base de dades
    public void afegirArticle(String codi, String descripcio, float pvp){
        ContentValues values = new ContentValues();
        values.put(ARTICLE_CODI, codi);
        values.put(ARTICLE_DESCRIPCION, descripcio);
        values.put(ARTICLE_PVP,pvp);
        values.put(ARTICLE_ESTOC,0);

        dbW.insert(TABLE_MAGATZEM,null,values);
    }

    //Metode per actualitzar un article de la base de dades
    public void actualitzarArticle(long id, String descripcio, float pvp) {
        ContentValues values = new ContentValues();
        values.put(ARTICLE_DESCRIPCION, descripcio);
        values.put(ARTICLE_PVP,pvp);

        dbW.update(TABLE_MAGATZEM,values, ARTICLE_ID + " = ?", new String[] { String.valueOf(id) });
    }


    //Metode per afegir un moviment a la base de dades
    public void afegirMoviment(String codi, String dia, int quantitat, String tipus){
        ContentValues values = new ContentValues();
        values.put(MOVIMENT_CODI, codi);
        values.put(MOVIMENT_DATA, dia);
        values.put(MOVIMENT_QUANTITAT,quantitat);
        values.put(MOVIMENT_TIPUS,tipus);

        dbW.insert(TABLE_MOVIMENTS,null,values);
    }

    //Metode per actualitzar l'estoc de un article de la base de dades
    public void actualitzarStock(long id, int stock) {
        ContentValues values = new ContentValues();
        values.put(ARTICLE_ESTOC, stock);
        dbW.update(TABLE_MAGATZEM,values, ARTICLE_ID + " = ?", new String[] { String.valueOf(id) });
    }

    //Metode per eliminar un article en concret de la base de dades
    public void eliminarArticle(long id) {
        dbW.delete(TABLE_MAGATZEM, ARTICLE_ID + " = ?", new String[] { String.valueOf(id) });
    }
}
