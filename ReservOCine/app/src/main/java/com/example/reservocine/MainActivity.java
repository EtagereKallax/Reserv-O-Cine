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
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button boutton;
        boutton = findViewById(R.id.QRcode);
        boutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent y = new Intent(MainActivity.this, QRCode.class);
                startActivity(y);
            }
        });

        Button ajouterFilm;
        ajouterFilm = findViewById(R.id.AjouterFilm);
        ajouterFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent x = new Intent(MainActivity.this, AjouterFilm.class);
                startActivity(x);
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
            default:
                return super.onContextItemSelected(item);
        }
    }
}
