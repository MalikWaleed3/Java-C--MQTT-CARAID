package com.example.shadowapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LoginPage extends AppCompatActivity {
    EditText uEmail, uPassword;
    Button uLoginBtn;
    TextView uCreateBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    MainActivity main;
    MediaPlayer help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_login_page);

        help = MediaPlayer.create(this, R.raw.loginhelper);
        Button pressHelpStart = (Button) this.findViewById(R.id.soundhelp2);
        Button pressHelpStop = (Button) this.findViewById(R.id.soundOn);

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

        uEmail = findViewById(R.id.loginemail);
        uPassword = findViewById(R.id.loginpassword);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        uLoginBtn = findViewById(R.id.LoginButton);
        uCreateBtn = findViewById(R.id.textView2);

        uLoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String email = uEmail.getText().toString().trim();
                String password = uPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    uEmail.setError("Email is Required. ");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    uPassword.setError("Password is Required. ");
                    return;
                }

                if (password.length() < 6) {
                    uPassword.setError("Password Must be >= 6 Characters.");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            stopMedia();
                            Toast.makeText(LoginPage.this, "Logged in Successfully ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), OptionPage.class));

                        } else
                            Toast.makeText(LoginPage.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        uCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMedia();
                startActivity(new Intent(getApplicationContext(), RegisterPage.class));

            }
        });
    }
    public void stopMedia() {
        if (help.isPlaying()) {
            help.stop();
        }

    }

}