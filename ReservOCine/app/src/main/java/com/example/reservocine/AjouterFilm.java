package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
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
import android.app.NotificationChannel;
import android.app.NotificationManager;


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



                String CHANNEL_ID = "my_channel_id";


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel =
                            new NotificationChannel(CHANNEL_ID, "Mascot Notification",
                                    NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                Intent intent = new Intent(AjouterFilm.this, VoirPlusFilm.class);
                intent.putExtra("title", titre);


                Notification notification = new NotificationCompat.Builder(AjouterFilm.this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Un nouveau film est disponible!")
                        .setContentText(titre)
                        .build();

                int notificationId = 1; // ID de notification arbitraire

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(notificationId, notification);



                Toast.makeText(AjouterFilm.this, "Film ajouté avec succès", Toast.LENGTH_SHORT).show();
                Intent accueil = new Intent(AjouterFilm.this, MainActivity.class);
                accueil.putExtra("nom", getIntent().getStringExtra("nom"));
                accueil.putExtra("prenom", getIntent().getStringExtra("prenom"));
                accueil.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(accueil);
                finish();
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
                            if(minute < 10) {
                                etDuree.setText(hourOfDay + "h0" + minute);
                            } else {
                                etDuree.setText(hourOfDay + "h" + minute);
                            }
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
                            if(dayOfMonth < 10) {
                                etDateSortie.setText("0" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            } else if (monthOfYear < 10) {
                                etDateSortie.setText(dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year);
                            } else if (dayOfMonth < 10 && monthOfYear < 10) {
                                etDateSortie.setText("0" + dayOfMonth + "/" + "0" + (monthOfYear + 1) + "/" + year);
                            } else {
                                etDateSortie.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
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

                                    String selectedDateTime = "";

                                    if(mDay < 10) {
                                        selectedDateTime = "0" + mDay + "/" + (mMonth + 1) + "/" + mYear + " " + mHour + ":" + mMinute;
                                    } else if(mMonth < 10) {
                                        selectedDateTime = mDay + "/" + "0" + (mMonth + 1) + "/" + mYear + " " + mHour + ":" + mMinute;
                                    } else if(mDay < 10 && mMonth < 10) {
                                        selectedDateTime = "0" + mDay + "/" + "0" + (mMonth + 1) + "/" + mYear + " " + mHour + ":" + mMinute;
                                    } else if(mHour < 10) {
                                        selectedDateTime = mDay + "/" + (mMonth + 1) + "/" + mYear + " 0" + mHour + ":" + mMinute;
                                    } else if(mDay < 10 && mHour < 10) {
                                        selectedDateTime = "0" + mDay + "/" + (mMonth + 1) + "/" + mYear + " 0" + mHour + ":" + mMinute;
                                    } else if(mMonth < 10 && mHour < 10) {
                                        selectedDateTime = mDay + "/0" + (mMonth + 1) + "/" + mYear + " 0" + mHour + ":" + mMinute;
                                    } else if(mDay < 10 && mMonth < 10 && mHour < 10) {
                                        selectedDateTime = "0" + mDay + "/0" + (mMonth + 1) + "/" + mYear + " 0" + mHour + ":" + mMinute;
                                    } else if(mMinute < 10) {
                                        selectedDateTime = mDay + "/" + (mMonth + 1) + "/" + mYear + " " + mHour + ":0" + mMinute;
                                    } else if(mDay < 10 && mMinute < 10) {
                                        selectedDateTime = "0" + mDay + "/" + (mMonth + 1) + "/" + mYear + " " + mHour + ":0" + mMinute;
                                    } else if(mMonth < 10 && mMinute < 10) {
                                        selectedDateTime = mDay + "/0" + (mMonth + 1) + "/" + mYear + " " + mHour + ":0" + mMinute;
                                    } else if(mDay < 10 && mMonth < 10 && mMinute < 10) {
                                        selectedDateTime = "0" + mDay + "/0" + (mMonth + 1) + "/" + mYear + " " + mHour + ":0" + mMinute;
                                    } else if(mHour < 10 && mMinute < 10) {
                                        selectedDateTime = mDay + "/" + (mMonth + 1) + "/" + mYear + " 0" + mHour + ":0" + mMinute;
                                    } else if(mDay < 10 && mHour < 10 && mMinute < 10) {
                                        selectedDateTime = "0" + mDay + "/" + (mMonth + 1) + "/" + mYear + " 0" + mHour + ":0" + mMinute;
                                    } else if(mMonth < 10 && mHour < 10 && mMinute < 10) {
                                        selectedDateTime = mDay + "/0" + (mMonth + 1) + "/" + mYear + " 0" + mHour + ":0" + mMinute;
                                    } else if(mDay < 10 && mMonth < 10 && mHour < 10 && mMinute < 10) {
                                        selectedDateTime = "0" + mDay + "/0" + (mMonth + 1) + "/" + mYear + " 0" + mHour + ":0" + mMinute;
                                    } else {
                                        selectedDateTime = mDay + "/" + (mMonth + 1) + "/" + mYear + " " + mHour + ":" + mMinute;
                                    }

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
        dbManager.ajouterFilm(image, titre, duree, dateSortie, synopsis, dateDebut, dateFin, Math.round((Math.random() * 10) * 10) / 10);
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
