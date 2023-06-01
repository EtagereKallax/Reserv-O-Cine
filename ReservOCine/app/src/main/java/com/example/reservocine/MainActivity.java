package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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

        Button inscrire;
        inscrire = findViewById(R.id.inscrire);
        inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Inscription.class);
                startActivity(intent);
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
            default:
                return super.onContextItemSelected(item);
        }
    }
}
