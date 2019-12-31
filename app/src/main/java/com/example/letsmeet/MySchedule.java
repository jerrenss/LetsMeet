package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MySchedule extends AppCompatActivity {

    Button bAddSchedule, bViewSchedule, bDeleteSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);

        bAddSchedule = (Button) findViewById(R.id.bAddSchedule);
        bViewSchedule = (Button) findViewById(R.id.bViewSchedule);
        bDeleteSchedule = (Button) findViewById(R.id.bDeleteSchedule);


        bAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySchedule.this, AddSchedule.class);
                startActivity(intent);
            }
        });

        bViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySchedule.this, ViewSchedule.class);
                startActivity(intent);
            }
        });

        bDeleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MySchedule.this, DeleteSchedule.class);
                startActivity(intent);
            }
        });


    }
}
