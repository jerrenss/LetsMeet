package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OrganisingOptions extends AppCompatActivity {

    Button bManual, bScheduleSync, bMySchedule, btnMainMenu2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organising_options);
        setTitle("Organising Options");
        bManual = (Button) findViewById(R.id.bManual);
        bScheduleSync = (Button) findViewById(R.id.bScheduleSync);
        bMySchedule = (Button) findViewById(R.id.bMySchedule);
        btnMainMenu2 = (Button) findViewById(R.id.btnMainMenu2);



        bManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganisingOptions.this, Deconflictor.class);
                startActivity(intent);
            }
        });

        bScheduleSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bMySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganisingOptions.this, ViewSchedule.class);
                startActivity(intent);
            }
        });

        bScheduleSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganisingOptions.this, ScheduleSync.class);
                startActivity(intent);
            }
        });

        btnMainMenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganisingOptions.this, MainMenu.class);
                startActivity(intent);
            }
        });
    }
}
