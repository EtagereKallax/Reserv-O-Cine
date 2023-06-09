package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DatabaseHelper.IMAGE, DatabaseHelper.IMAGE, DatabaseHelper.TITRE, DatabaseHelper.DUREE, DatabaseHelper.RATINGS };

    final int[] to = new int[] {R.id.image ,R.id.imageFilm, R.id.titre, R.id.duree, R.id.ratings};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button ajouterFilm;
        ajouterFilm = findViewById(R.id.voirTout);
        ajouterFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(MainActivity.this, TousLesFilms.class);
                startActivity(x);
                finish();
            }
        });





        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.selectFilmTendances();

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
        switch (item.getItemId()) {
            case R.id.reservations:
                startActivity(new Intent(this, QRCode.class));
                return true;
            case R.id.accueil:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.quitter:
                finish();
            case R.id.films:
                startActivity(new Intent(this, TousLesFilms.class));
                return true;
            case R.id.connexion:
                startActivity(new Intent(this, Connexion.class));
                return true;
            case R.id.AjouterFilm:
                startActivity(new Intent(this, AjouterFilm.class));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
