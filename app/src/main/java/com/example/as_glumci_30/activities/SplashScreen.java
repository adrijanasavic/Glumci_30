package com.example.as_glumci_30.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.as_glumci_30.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {
    public static final int SPLASH_TIMEOUT = 2000;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );

        image = findViewById( R.id.slika );
        InputStream is;
        try {
            is = getAssets().open( "android_pink.jpg" );
            Drawable drawable = Drawable.createFromStream( is, null );
            image.setImageDrawable( drawable );
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Timer().schedule( new TimerTask() {
            @Override
            public void run() {
                startActivity( new Intent( SplashScreen.this, MainActivity.class ) );
                finish();
            }
        }, SPLASH_TIMEOUT );
    }
}