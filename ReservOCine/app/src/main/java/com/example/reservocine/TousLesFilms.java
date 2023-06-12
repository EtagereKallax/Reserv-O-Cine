package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class TousLesFilms extends AppCompatActivity {
    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DatabaseHelper.IMAGE, DatabaseHelper.IMAGE, DatabaseHelper.TITRE, DatabaseHelper.SYNOPSIS, DatabaseHelper.DUREE, DatabaseHelper.RATINGS };

    final int[] to = new int[] {R.id.image ,R.id.imageFilm, R.id.titre, R.id.synopsis, R.id.duree, R.id.ratings};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.fragment_emp_list);
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.selectFilmVoirTout();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_tous_les_films, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView titleTextView = (TextView) view.findViewById(R.id.titre);
                TextView synopsisTextView = (TextView) view.findViewById(R.id.synopsis);
                TextView dureeTextView = (TextView) view.findViewById(R.id.duree);

                String title = titleTextView.getText().toString();
                String synopsis = synopsisTextView.getText().toString();

                ImageView imgFilm = (ImageView) view.findViewById(R.id.imageFilm);
                String imgF = imgFilm.getDrawable().toString();


                Intent modify_intent = new Intent(getApplicationContext(), VoirPlusFilm.class);
                modify_intent.putExtra("imgFilm", imgF);
                modify_intent.putExtra("title", title);
                startActivity(modify_intent);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.reservations:
                if(getIntent().getStringExtra("nom") == null) {
                    intent = new Intent(this, Connexion.class);
                } else {
                    intent = new Intent(this, MesReservations.class);
                    intent.putExtra("nom", getIntent().getStringExtra("nom"));
                    intent.putExtra("prenom", getIntent().getStringExtra("prenom"));
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                }
                startActivity(intent);
                return true;
            case R.id.accueil:
                intent = new Intent(this, MainActivity.class);
                if(getIntent().getStringExtra("nom") == null) {
                    startActivity(intent);
                } else {
                    intent.putExtra("nom", getIntent().getStringExtra("nom"));
                    intent.putExtra("prenom", getIntent().getStringExtra("prenom"));
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                    startActivity(intent);
                }
                return true;
            case R.id.quitter:
                finish();
            case R.id.films:
                intent = new Intent(this, TousLesFilms.class);
                if(getIntent().getStringExtra("nom") == null) {
                    startActivity(intent);
                } else {
                    intent.putExtra("nom", getIntent().getStringExtra("nom"));
                    intent.putExtra("prenom", getIntent().getStringExtra("prenom"));
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                    startActivity(intent);
                }
                return true;
            case R.id.connexion:
                if(getIntent().getStringExtra("nom") == null) {
                    startActivity(new Intent(this, Connexion.class));
                } else {
                    Toast.makeText(this, "Vous êtes déjà connecté", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.AjouterFilm:
                if(getIntent().getStringExtra("nom") == null) {
                    intent = new Intent(this, Connexion.class);
                    startActivity(intent);
                } else if(getIntent().getStringExtra("nom") != "admin" && getIntent().getStringExtra("prenom") == "admin" && getIntent().getStringExtra("email") == "admin@gmail.com") {
                    Toast.makeText(this, "En tant qu'utilisateur, vous ne pouvez pas accéder à cette fonctionnalité", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(this, AjouterFilm.class);
                    intent.putExtra("nom", getIntent().getStringExtra("nom"));
                    intent.putExtra("prenom", getIntent().getStringExtra("prenom"));
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                    startActivity(intent);
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
