package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenu extends AppCompatActivity {

    private Button bLogout, bLetsChat, bDeconflictor, bAgenda;
    private TextView tvName;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String user = mAuth.getCurrentUser().getUid();
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(user);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setTitle("Welcome to LetsMeet!");
        tvName = (TextView) findViewById(R.id.tvName);
        bLogout = (Button) findViewById(R.id.bLogout);
        bLetsChat = (Button) findViewById(R.id.bLetsChat);
        bDeconflictor = (Button) findViewById(R.id.bDeconflictor);
        bAgenda = (Button) findViewById(R.id.bAgenda);


        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showName(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                Toast.makeText(MainMenu.this, "Logout Successful", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainMenu.this, MainActivity.class));
            }
        });


        bDeconflictor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, OrganisingOptions.class);
                startActivity(intent);
            }
        });

        bAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Agenda2.class);
                startActivity(intent);
            }
        });

        bLetsChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, GroupCreation.class);
                startActivity(intent);
            }
        });


    }

    private void showName(DataSnapshot dataSnapshot) {
        String temp = dataSnapshot.child("Name").getValue().toString();
        String full = "Welcome, " + temp + "!";
        tvName.setText(full);
    }
}
