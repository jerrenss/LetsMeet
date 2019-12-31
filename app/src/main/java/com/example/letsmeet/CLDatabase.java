package com.example.letsmeet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class CLDatabase extends AppCompatActivity {

    private Button bSESCL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cldatabase);
        setTitle("Circles Life");

        bSESCL = findViewById(R.id.bSESCL);

        bSESCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                delay();
                alert();
            }
        });

    }

    public void delay() {
        ProgressDialog dialog2 = ProgressDialog.show(CLDatabase.this, "",
                "Loading. Please wait...", true);
        dialog2.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog2.dismiss();
            }
        }, 3000);
    }

    public void alert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(CLDatabase.this);
        dialog.setTitle("Predicted SES").setMessage("High").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
}
