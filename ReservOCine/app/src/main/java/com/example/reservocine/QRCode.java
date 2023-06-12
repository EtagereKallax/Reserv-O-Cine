package com.example.reservocine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        //Text will be entered here to generate QR code
        TextView tvText = findViewById(R.id.tvText);
        //ImageView for generated QR code
        ImageView imageCode = findViewById(R.id.imageCode);

        tvText.setText(getIntent().getStringExtra("titre") + ", le " + getIntent().getStringExtra("date") + " à " + getIntent().getStringExtra("time"));
        /*btnGenerate.setOnClickListener(v -> {
            //getting text from input text field.
            String myText = etText.getText().toString().trim();
            //initializing MultiFormatWriter for QR code
            MultiFormatWriter mWriter = new MultiFormatWriter();
            try {
                //BitMatrix class to encode entered text and set Width & Height
                BitMatrix mMatrix = mWriter.encode(myText, BarcodeFormat.QR_CODE, 400,400);
                BarcodeEncoder mEncoder = new BarcodeEncoder();
                Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                imageCode.setImageBitmap(mBitmap);//Setting generated QR code to imageView
                // to hide the keyboard
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(etText.getApplicationWindowToken(), 0);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        });*/

        // getting text from text field.
        String myText = tvText.getText().toString().trim();
        // initializing MultiFormatWriter for QR code
        MultiFormatWriter mWriter = new MultiFormatWriter();
        try {
            // BitMatrix class to encode entered text and set Width & Height
            BitMatrix mMatrix = mWriter.encode(myText, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder mEncoder = new BarcodeEncoder();
            Bitmap mBitmap = mEncoder.createBitmap(mMatrix); // creating bitmap of code
            imageCode.setImageBitmap(mBitmap); // Setting generated QR code to imageView
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

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
