package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MovieReservation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner dateSpinner;
    private Spinner timeSpinner;
    private Button reserveButton;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reservation);

        dateSpinner = findViewById(R.id.dateSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        reserveButton = findViewById(R.id.reserveButton);

        dbManager = new DBManager(this);
        dbManager.open();

        List<String> dateList;

        // Populate date spinner with data
        try {
            dateList = selectDates(getIntent().getStringExtra("title"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String imgFilm = "/data/user/0/com.example.reservocine/files/" +
                getIntent().getStringExtra("title") +
                ".jpg";

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                dateList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        // Populate time spinner with data
        List<String> timeList = new ArrayList<>();

        timeList.add("14:00");
        timeList.add("17:30");
        timeList.add("21:00");

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, timeList);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);

        // Set listeners for spinners
        dateSpinner.setOnItemSelectedListener(this);
        timeSpinner.setOnItemSelectedListener(this);

        // Set listener for reserve button
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle reservation button click
                String selectedDate = dateSpinner.getSelectedItem().toString();
                String selectedTime = timeSpinner.getSelectedItem().toString();
                saveReservationToDatabase(getIntent().getStringExtra("nom"),
                        getIntent().getStringExtra("prenom"),
                        getIntent().getStringExtra("imgFilm"),
                        getIntent().getStringExtra("title"),
                        selectedDate, selectedTime);
            }
        });
    }

    private void saveReservationToDatabase(String name, String surname, String film, String title, String date, String time) {
        dbManager.reserve(name, surname, film, title, date, time);
        Toast.makeText(this, "Vous avez réserver pour le film " + film + " pour le " + date + " à " + time, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MovieReservation.this, MesReservations.class);
        intent.putExtra("nom", name);
        intent.putExtra("prenom", surname);
        intent.putExtra("email", getIntent().getStringExtra("email"));
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Handle spinner item selection
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Handle when nothing is selected in the spinner
    }

    private List<String> selectDates(String titre) throws ParseException {
        Cursor date = dbManager.selectDate(titre);
        List<String> dates = new ArrayList<>();
        String date_debut = date.getString(6);
        String date_fin = date.getString(7);

        if (date_debut.length() >= 10) {
            date_debut = date_debut.substring(0, 10);
            dates.add(date_debut);
        }

        if (date_fin.length() >= 10) {
            date_fin = date_fin.substring(0, 10);
            dates.add(date_fin);
        }

        if (dates.size() == 2) {
            Date debut = new SimpleDateFormat("dd/MM/yyyy").parse(date_debut);
            Date fin = new SimpleDateFormat("dd/MM/yyyy").parse(date_fin);

            long nbJours = TimeUnit.DAYS.convert(fin.getTime() - debut.getTime(), TimeUnit.MILLISECONDS);
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 1; i < nbJours; i++) {
                calendar.setTime(debut);
                calendar.add(Calendar.DAY_OF_YEAR, i);
                dates.add(sdf.format(calendar.getTime()));
            }
        }

        return dates;
    }


    /*
    private List<String> selectTimes(String titre) {
        Cursor time = dbManager.selectDate(titre);
        List<String> times = new ArrayList<>();
        String time_debut = "14:00";
        times.add(time_debut);
        String duree = time.getString(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime temps;
        try {
            temps = LocalTime.parse(time_debut, formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
        int minutes = temps.getHour() * 60 + temps.getMinute();
        while(!temps.isAfter(LocalTime.of(21, 0))) {
            temps.plusMinutes(minutes);
            int minute = temps.getMinute();
            int remainder = minutes % 30;
            if(remainder != 0) {
                int minutesToRound = 30 + remainder;
                temps = temps.plusMinutes(minutesToRound);
            }
            times.add(temps.format(formatter));
        }
        return times;
    }
     */

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
