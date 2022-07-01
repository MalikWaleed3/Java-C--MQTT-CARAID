package com.example.shadowapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfilePage extends AppCompatActivity {
    TextView fullName,email;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        fullName = findViewById(R.id.ProfileName);
        email = findViewById(R.id.ProfileEmail);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();

        Button logout  = findViewById(R.id.button10);
        Button returnB  =findViewById(R.id.button3);

        logout.setOnClickListener(v -> openActivity1());
        returnB.setOnClickListener(v -> openActivity2());

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, (documentSnapshot, error) -> {
            assert documentSnapshot != null;
            fullName.setText(documentSnapshot.getString("uFullName"));
            email.setText(documentSnapshot.getString("uEmail"));
        });


    }

    private void openActivity2() {
        Intent intent = new Intent(this, OptionPage.class);
        startActivity(intent);
    }


    public void openActivity1 () {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }

}