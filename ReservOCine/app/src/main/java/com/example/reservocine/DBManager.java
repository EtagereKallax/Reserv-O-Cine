package com.example.reservocine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // Mettre les fonctions ici

    // Exemples
    /* public void insert(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SUBJECT, name);
        contentValue.put(DatabaseHelper.DESC, desc);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    } */

    /* public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.SUBJECT, DatabaseHelper.DESC };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor
    } */

    /* public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SUBJECT, name);
        contentValues.put(DatabaseHelper.DESC, desc);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    } */

    /* public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    } */

    public void ajouterFilm(String image, String titre, String duree, String dateSortie, String synopsis, String dateDebut, String dateFin, double ratings) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.IMAGE, image);
        values.put(DatabaseHelper.TITRE, titre);
        values.put(DatabaseHelper.DUREE, duree);
        values.put(DatabaseHelper.DATE_SORTIE, dateSortie);
        values.put(DatabaseHelper.SYNOPSIS, synopsis);
        values.put(DatabaseHelper.DATE_DEBUT, dateDebut);
        values.put(DatabaseHelper.DATE_FIN, dateFin);
        values.put(DatabaseHelper.RATINGS, ratings);
        database.insert(DatabaseHelper.TABLE_FILMS, null, values);
    }


    public Cursor selectFilmTendances() {
        String[] columns = new String[] { DatabaseHelper._IDFILMS, DatabaseHelper.TITRE, DatabaseHelper.IMAGE, DatabaseHelper.SYNOPSIS, DatabaseHelper.DUREE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_FILMS, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor selectFilmVoirTout() {
        String[] columns = new String[] { DatabaseHelper._IDFILMS, DatabaseHelper.TITRE, DatabaseHelper.IMAGE, DatabaseHelper.SYNOPSIS, DatabaseHelper.DUREE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_FILMS, columns, null, null, null, null, DatabaseHelper.DATE_SORTIE);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor selectFilmVoirPlus(String where, String[] whereValue) {
        String[] columns = new String[] { DatabaseHelper._IDFILMS, DatabaseHelper.TITRE, DatabaseHelper.IMAGE, DatabaseHelper.SYNOPSIS, DatabaseHelper.DUREE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_FILMS, columns, where, whereValue, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }



}
