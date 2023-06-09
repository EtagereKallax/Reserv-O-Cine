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

    public static final String TABLE_USER = "utilisateur";

    public static final String _IDUSER = "_iduser";
    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    public static final String TABLE_RESERV = "reservation";


    // Database Information
    static final String DB_NAME = "ReservOCine";

    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE_FILMS = "create table " + TABLE_FILMS + "(" +
            _IDFILMS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            IMAGE + " TEXT, " +
            TITRE + " TEXT NOT NULL, " +
            DUREE + " TEXT NOT NULL, " +
            DATE_SORTIE + " TEXT NOT NULL, " +
            SYNOPSIS + " TEXT NOT NULL, " +
            DATE_DEBUT + " TEXT NOT NULL, " +
            DATE_FIN + " TEXT NOT NULL, " +
            RATINGS + " REAL);";

    private static final String CREATE_TABLE_USER = "create table " + TABLE_USER + "(" +
            _IDUSER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NOM + " TEXT NOT NULL, " +
            PRENOM + " TEXT NOT NULL, " +
            EMAIL + " TEXT UNIQUE NOT NULL, " +
            PASSWORD + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FILMS);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        // Supprimer les tables existantes (ou effectuer d'autres opérations de réinitialisation)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        // Recréer la structure de la base de données
        onCreate(db);
    }
}
