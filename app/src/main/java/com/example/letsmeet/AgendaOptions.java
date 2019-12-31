package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AgendaOptions extends AppCompatActivity {

    Button bAddToList, bViewList, bDeleteFromList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_options);
        setTitle("Options");

        bAddToList = (Button) findViewById(R.id.bAddToList);
        bViewList = (Button) findViewById(R.id.bViewList);
        bDeleteFromList = (Button) findViewById(R.id.bDeleteFromList);

        bAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgendaOptions.this, AddAgenda.class);
                startActivity(intent);

            }
        });

        bViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgendaOptions.this, Agenda2.class);
                startActivity(intent);
            }
        });

        bDeleteFromList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgendaOptions.this, DeleteAgenda.class);
                startActivity(intent);
            }
        });

    }
}
