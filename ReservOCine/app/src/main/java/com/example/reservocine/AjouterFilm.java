package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;

public class AjouterFilm extends AppCompatActivity implements View.OnClickListener {

    private Button btnUpload;
    private EditText etImage;
    private EditText etTitre;
    private EditText etDuree;
    private Button btnDuree;
    private EditText etDateSortie;
    private Button btnDateSortie;
    private EditText etSynopsis;
    private EditText etDateDebut;
    private Button btnDateDebut;
    private EditText etDateFin;
    private Button btnDateFin;
    private Button btnAjouter;

    private DBManager dbManager;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_film);

        btnUpload = findViewById(R.id.btnUpload);
        etImage = findViewById(R.id.etImage);
        etTitre = findViewById(R.id.etTitre);
        etDuree = findViewById(R.id.etDuree);
        btnDuree = findViewById(R.id.btnDuree);
        etDateSortie = findViewById(R.id.etDateSortie);
        btnDateSortie = findViewById(R.id.btnDateSortie);
        etSynopsis = findViewById(R.id.etSynopsis);
        etDateDebut = findViewById(R.id.etDateDebut);
        btnDateDebut = findViewById(R.id.btnDateDebut);
        etDateFin = findViewById(R.id.etDateFin);
        btnDateFin = findViewById(R.id.btnDateFin);
        btnAjouter = findViewById(R.id.btnAjouter);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir la galerie pour sélectionner une image
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        btnDuree.setOnClickListener(this);
        btnDateSortie.setOnClickListener(this);
        btnDateDebut.setOnClickListener(this);
        btnDateFin.setOnClickListener(this);


        dbManager = new DBManager(this);
        dbManager.open();
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image = etImage.getText().toString();
                String titre = etTitre.getText().toString();
                String duree = etDuree.getText().toString();
                String dateSortie = etDateSortie.getText().toString();
                String synopsis = etSynopsis.getText().toString();
                String dateDebut = etDateDebut.getText().toString();
                String dateFin = etDateFin.getText().toString();

                // Appeler la méthode pour ajouter le film à la base de données
                ajouterFilm(image, titre, duree, dateSortie, synopsis, dateDebut, dateFin);

                Toast.makeText(AjouterFilm.this, "Film ajouté avec succès", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == btnDuree) {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            etDuree.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        } else if(v == btnDateSortie) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            etDateSortie.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } else {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    TimePickerDialog timePickerDialog = new TimePickerDialog(AjouterFilm.this,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    mHour = hourOfDay;
                                    mMinute = minute;

                                    String selectedDateTime = mDay + "-" + (mMonth + 1) + "-" + mYear + " " + mHour + ":" + mMinute;

                                    if(v == btnDateDebut) {
                                        etDateDebut.setText(selectedDateTime);
                                    } else {
                                        etDateFin.setText(selectedDateTime);
                                    }
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    private void ajouterFilm(String image, String titre, String duree, String dateSortie, String synopsis, String dateDebut, String dateFin) {
        dbManager.ajouterFilm(image, titre, duree, dateSortie, synopsis, dateDebut, dateFin, Math.random() * 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Récupérer l'URI de l'image sélectionnée
            Uri selectedImageUri = data.getData();

            try {
                // Convertir l'URI de l'image en un Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Enregistrer le bitmap dans le dossier interne de l'application
                String imagePath = saveImageToInternalStorage(bitmap);

                // Mettre à jour le champ de texte avec le chemin de l'image
                etImage.setText(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Erreur lors de la séletion de l'image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        // Créer un fichier dans le dossier interne de l'application
        File directory = getFilesDir();
        File imageFile = new File(directory, etTitre.getText().toString() + ".jpg");

        try {
            // Convertir le Bitmap en fichier JPEG et l'enregistrer dans le dossier interne
            OutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            // Récupérer le chemin absolu du fichier
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors de l'enregistrement de l'image", Toast.LENGTH_SHORT).show();
        }
        return "";
    }
}