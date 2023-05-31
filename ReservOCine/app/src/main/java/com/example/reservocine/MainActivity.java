package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    }

}
