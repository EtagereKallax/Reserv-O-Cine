package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AjouterFilm extends AppCompatActivity {

    private EditText etImage;
    private EditText etTitre;
    private EditText etDuree;
    private EditText etDateSortie;
    private EditText etSynopsis;
    private EditText etDateDebut;
    private EditText etDateFin;
    private Button btnUpload;
    private Button btnAjouter;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_film);

        etImage = findViewById(R.id.etImage);
        etTitre = findViewById(R.id.etTitre);
        etDuree = findViewById(R.id.etDuree);
        etDateSortie = findViewById(R.id.etDateSortie);
        etSynopsis = findViewById(R.id.etSynopsis);
        etDateDebut = findViewById(R.id.etDateDebut);
        etDateFin = findViewById(R.id.etDateFin);
        btnUpload = findViewById(R.id.btnUpload);
        btnAjouter = findViewById(R.id.btnAjouter);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implémentez la logique d'upload de l'image ici
            }
        });

        dbManager = new DBManager(this);
        dbManager.open();
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image = etImage.getText().toString();
                String titre = etTitre.getText().toString();
                LocalTime duree = LocalTime.parse(etDuree.getText().toString());
                LocalDate dateSortie = LocalDate.parse(etDateSortie.getText().toString());
                String synopsis = etSynopsis.getText().toString();
                LocalDateTime dateDebut = LocalDateTime.parse(etDateDebut.getText().toString());
                LocalDateTime dateFin = LocalDateTime.parse(etDateFin.getText().toString());

                // Appeler la méthode pour ajouter le film à la base de données
                ajouterFilm(image, titre, duree, dateSortie, synopsis, dateDebut, dateFin);

                Toast.makeText(AjouterFilm.this, "Film ajouté avec succès", Toast.LENGTH_SHORT).show();
                finish(); // Fermer l'activité après l'ajout du film
            }
        });
    }

    private void ajouterFilm(String image, String titre, LocalTime duree, LocalDate dateSortie, String synopsis, LocalDateTime dateDebut, LocalDateTime dateFin) {
        dbManager.ajouterFilm(image, titre, duree, dateSortie, synopsis, dateDebut, dateFin, Math.random() * 10);
    }
}