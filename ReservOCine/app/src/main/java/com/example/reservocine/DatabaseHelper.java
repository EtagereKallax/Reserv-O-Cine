package com.example.reservocine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_FILMS = "films";

    // Table columns
    public static final String _IDFILMS = "_id";
    public static final String IMAGE = "image";
    public static final String TITRE = "titre";
    public static final String DUREE = "duree";
    public static final String DATE_SORTIE = "date_sortie";
    public static final String SYNOPSIS = "synopsis";
    public static final String DATE_DEBUT = "date_debut";
    public static final String DATE_FIN = "date_fin";
    public static final String RATINGS = "ratings";

    // Database Information
    static final String DB_NAME = "ReservOCine";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_FILMS + "(" +
            _IDFILMS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            IMAGE + " TEXT NOT NULL, " +
            TITRE + " TEXT NOT NULL, " +
            DUREE + " TEXT NOT NULL, " +
            DATE_SORTIE + " TEXT NOT NULL, " +
            SYNOPSIS + " TEXT NOT NULL, " +
            DATE_DEBUT + " TEXT NOT NULL, " +
            DATE_FIN + " TEXT NOT NULL, " +
            RATINGS + " REAL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
        onCreate(db);
    }
}