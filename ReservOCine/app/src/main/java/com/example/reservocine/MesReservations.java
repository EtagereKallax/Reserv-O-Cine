package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MesReservations extends AppCompatActivity {

    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DatabaseHelper.FILM, DatabaseHelper.FILM, DatabaseHelper.TITLE, DatabaseHelper.DATE, DatabaseHelper.TIME };

    final int[] to = {R.id.img, R.id.imgFilm, R.id.title, R.id.date_reserv, R.id.time_reserv};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations_emp_list);

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.selectReserv(getIntent().getStringExtra("nom"), getIntent().getStringExtra("prenom"));

        listView = (ListView) findViewById(R.id.lv_reservations);
        listView.setEmptyView(findViewById(R.id.tv_empty));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_mes_reservations, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView titleTextView = (TextView) view.findViewById(R.id.title);
                TextView dateTextView = (TextView) view.findViewById(R.id.date_reserv);
                TextView timeTextView = (TextView) view.findViewById(R.id.time_reserv);

                String title = titleTextView.getText().toString();
                String date = dateTextView.getText().toString();
                String time = timeTextView.getText().toString();

                Intent qrcode_intent = new Intent(getApplicationContext(), QRCode.class);
                qrcode_intent.putExtra("nom", getIntent().getStringExtra("nom"));
                qrcode_intent.putExtra("prenom", getIntent().getStringExtra("prenom"));
                qrcode_intent.putExtra("titre", title);
                qrcode_intent.putExtra("date", date);
                qrcode_intent.putExtra("time", time);
                startActivity(qrcode_intent);
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