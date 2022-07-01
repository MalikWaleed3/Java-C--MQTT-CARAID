package com.example.shadowapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class OptionPage extends AppCompatActivity {

    CardView cardController;
    CardView cardMic;
    CardView cardJoystick;
    CardView cardProfile;
    MediaPlayer help;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_page);

        help = MediaPlayer.create(this, R.raw.optionhelper);
        Button pressHelpStart = (Button) this.findViewById(R.id.soundhelp2);
        Button pressHelpStop = (Button) this.findViewById(R.id.soundOn);

        cardController = findViewById(R.id.cardController);
        cardMic = findViewById(R.id.cardMic);
        cardJoystick = findViewById(R.id.cardJoystick);
        cardProfile = findViewById(R.id.cardProfile);


        pressHelpStart.setOnClickListener(v -> {
            help.start();
            pressHelpStart.setVisibility(View.INVISIBLE);
            pressHelpStop.setVisibility(View.VISIBLE);
            help.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    pressHelpStop.setVisibility(View.INVISIBLE);
                    pressHelpStart.setVisibility(View.VISIBLE);
                }
            });
        });
        pressHelpStop.setOnClickListener(v -> {
            help.stop();
            try {
                help.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pressHelpStop.setVisibility(View.INVISIBLE);
            pressHelpStart.setVisibility(View.VISIBLE);
        });

        cardController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMedia();
                ControllerPage();}
        });
        cardMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMedia();
                VoiceCommand();}

        });
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                stopMedia();
                ProfilePage();}
        });
        cardJoystick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joystick();
            }
        });

    }
    public void ControllerPage () {

        Intent intent = new Intent(this, ControllerPage.class);
        startActivity(intent);
    }
    public void VoiceCommand () {

        Intent intent = new Intent(this, VoiceCommand.class);
        startActivity(intent);
    }
    public void ProfilePage () {

        Intent intent = new Intent(this, ProfilePage.class);
        startActivity(intent);
    }
    public void stopMedia() {
        if (help.isPlaying()) {
            help.stop();
        }

    }
    public void joystick () {
        Intent intent = new Intent(this, JoystickPage.class);
        startActivity(intent);
    }
}



