package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Connexion extends AppCompatActivity {

    private EditText etMail, etMdp;
    private Button btnConnecter, btnInscription;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        etMail = findViewById(R.id.etMail);
        etMdp = findViewById(R.id.etMdp);
        btnConnecter = findViewById(R.id.btnConnecter);
        btnInscription = findViewById(R.id.btnInscription);

        dbManager = new DBManager(this);
        dbManager.open();

        btnConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etMail.getText().toString();
                String password = etMdp.getText().toString();

                if(email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Connexion.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT);
                    return;
                }

                List<String> extras = selectUser(email, password);

                // Vérifier les critères de connexion avec la base de données
                if(extras != null) {
                    // Créer l'intention de passer à l'activité d'accueil avec les informations de l'utilisateur
                    Intent intent = new Intent(Connexion.this, MainActivity.class);
                    intent.putExtra("nom", extras.get(0));
                    intent.putExtra("prenom", extras.get(1));
                    intent.putExtra("email", extras.get(2));
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Passer à l'activité d'inscription
                Intent intent = new Intent(Connexion.this, Inscription.class);
                startActivity(intent);
            }
        });
    }

    private List<String> selectUser(String email, String password) {
        Cursor user = dbManager.selectUser(email);
        String nom, prenom, mail, mdp;
        if(user == null) {
            Toast.makeText(this, "Aucun utilisateur ne possède cet email. Veuillez vous inscrire.", Toast.LENGTH_SHORT);
            Intent intent = new Intent(Connexion.this, Inscription.class);
            startActivity(intent);
            finish();
        } else {
            nom = user.getString(0);
            prenom = user.getString(1);
            mail = user.getString(2);
            mdp = user.getString(3);

            if(Objects.equals(mdp, hashPassword(password))) {
                Toast.makeText(this, "Connexion de l'utilisateur effectué. Bienvenue " + nom + " " + prenom, Toast.LENGTH_SHORT);
                ArrayList<String> extras = new ArrayList<>();
                extras.add(nom);
                extras.add(prenom);
                extras.add(mail);
                return extras;
            } else {
                Toast.makeText(this, "Mot de passe invalide", Toast.LENGTH_SHORT);
                return null;
            }
        }
        return null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b : hashedBytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Méthode de vérification des informations d'identification dans la base de données
    private boolean isValidCredentials(String email, String password) {
        return true;
    }
}