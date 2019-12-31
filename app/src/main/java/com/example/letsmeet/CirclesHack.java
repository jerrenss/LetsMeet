package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CirclesHack extends AppCompatActivity {

    private Button bLoginCL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circles_hack);
        setTitle("Employee Login");
        bLoginCL = findViewById(R.id.bLoginCL);

        bLoginCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CirclesHack.this, EnterDetail.class);
                startActivity(i);
            }
        });

    }
}
