package com.example.shadowapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    MediaPlayer mediaPlayer;
    int currentP;
    Animation logoAnime, textAnime;
    ImageView logoImg;
    TextView Text1, Text2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        logoAnime = AnimationUtils.loadAnimation(this,R.anim.logo_anime);
        textAnime = AnimationUtils.loadAnimation(this,R.anim.text_anime);

        videoView = findViewById(R.id.videoView);
        logoImg = findViewById(R.id.imageView2);
        Text1 = findViewById(R.id.textView3);
        Text2 = findViewById(R.id.textView6);

        logoImg.setAnimation(logoAnime);
        Text1.setAnimation(textAnime);
        Text2.setAnimation(textAnime);

        Uri uri = Uri.parse("android.resource://" + getPackageName() +"/"+ R.raw.car);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(mp -> {
            mediaPlayer = mp;
            mediaPlayer.setLooping(true);
            if (currentP != 0) {
                mediaPlayer.seekTo(currentP);
                mediaPlayer.start();
            }
        });
        int WAITING_SCREEN = 6500;
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this,LoginPage.class);
            startActivity(intent);
            finish(); }, WAITING_SCREEN);

    }

}
