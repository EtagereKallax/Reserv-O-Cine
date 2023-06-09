package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
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
        ajouterFilm = findViewById(R.id.AjouterFilm);
        ajouterFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(MainActivity.this, AjouterFilm.class);
                Intent intent = getIntent();
                if(intent.getStringExtra("nom") == null) {
                    x = new Intent(MainActivity.this, Connexion.class);
                    startActivity(x);
                    finish();
                }
                x.putExtra("nom", intent.getStringExtra("nom"));
                x.putExtra("prenom", intent.getStringExtra("prenom"));
                x.putExtra("email", intent.getStringExtra("email"));
                startActivity(x);
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
                Intent intent = getIntent();
                if(intent.getStringExtra("nom") == null) {
                    modify_intent = new Intent(MainActivity.this, Connexion.class);
                    startActivity(modify_intent);
                    finish();
                }
                modify_intent.putExtra("nom", intent.getStringExtra("nom"));
                modify_intent.putExtra("prenom", intent.getStringExtra("prenom"));
                modify_intent.putExtra("email", intent.getStringExtra("email"));

                startActivity(modify_intent);
                finish();
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
                Intent a = new Intent(this, QRCode.class);
                Intent intent = getIntent();
                if(intent.getStringExtra("nom") == null) {
                    a = new Intent(this, Connexion.class);
                    startActivity(a);
                    finish();
                }
                a.putExtra("nom", intent.getStringExtra("nom"));
                a.putExtra("prenom", intent.getStringExtra("prenom"));
                a.putExtra("email", intent.getStringExtra("email"));
                startActivity(a);
                finish();
                return true;
            case R.id.accueil:
                Intent b = new Intent(this, MainActivity.class);
                Intent intent1 = getIntent();
                if(intent1.getStringExtra("nom") == null) {
                    b = new Intent(this, Connexion.class);
                    startActivity(b);
                    finish();
                }
                b.putExtra("nom", intent1.getStringExtra("nom"));
                b.putExtra("prenom", intent1.getStringExtra("prenom"));
                b.putExtra("email", intent1.getStringExtra("email"));
                startActivity(b);
                finish();
                return true;
            case R.id.quitter:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            case R.id.films:
                Intent c = new Intent(this, TousLesFilms.class);
                Intent intent2 = getIntent();
                if(intent2.getStringExtra("nom") == null) {
                    c = new Intent(this, Connexion.class);
                    startActivity(c);
                    finish();
                }
                c.putExtra("nom", intent2.getStringExtra("nom"));
                c.putExtra("prenom", intent2.getStringExtra("prenom"));
                c.putExtra("email", intent2.getStringExtra("email"));
                startActivity(c);
                finish();
                return true;
            case R.id.connexion:
                startActivity(new Intent(this, Connexion.class));
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
