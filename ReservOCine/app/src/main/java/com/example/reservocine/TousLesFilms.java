package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;

public class TousLesFilms extends AppCompatActivity { private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DatabaseHelper.TITRE, DatabaseHelper.IMAGE };

    final int[] to = new int[] {R.id.imageFilm, R.id.titre };
    ImageView imageView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_emp_list);
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.selectFilmTendances();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));
        listView.setEmptyView(findViewById(R.id.emptyIMG));

        adapter = new SimpleCursorAdapter(this, R.layout.activity_tous_les_films, cursor, from, to, 0);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView imgTextView = (TextView) view.findViewById(R.id.image);
                TextView titleTextView = (TextView) view.findViewById(R.id.titre);

                String image = imgTextView.getText().toString();
                String title = titleTextView.getText().toString();

                File imgFile = new File(image);
                imageView = (ImageView) view.findViewById(R.id.imageFilm);

                if (imgFile.exists()) {
                    // on below line we are creating an image bitmap variable
                    // and adding a bitmap to it from image file.
                    Bitmap imgBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    // on below line we are setting bitmap to our image view.
                    imageView.setImageBitmap(imgBitmap);
                }

                Intent modify_intent = new Intent(getApplicationContext(), MainActivity.class);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("image", image);

                startActivity(modify_intent);
            }
        });

    }
}
