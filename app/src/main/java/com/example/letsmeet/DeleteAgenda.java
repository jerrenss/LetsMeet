package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAgenda extends AppCompatActivity {


    Button bDeleteAgenda;
    EditText etDelete;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference dbrAgendaList = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid()).child("AgendaList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_agenda);
        setTitle("Delete Agenda");


        bDeleteAgenda = (Button) findViewById(R.id.bDeleteAgenda);
        etDelete = (EditText) findViewById(R.id.etDelete);

        bDeleteAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etDelete.getText().toString().trim())) {
                    Toast.makeText(DeleteAgenda.this, "Please fill in Agenda Title", Toast.LENGTH_SHORT).show();
                } else {
                    String temp = etDelete.getText().toString().trim();
                    dbrAgendaList.child(temp).removeValue();
                    etDelete.setText("");
                    Toast.makeText(DeleteAgenda.this, "Agenda Deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteAgenda.this, Agenda2.class);
                    startActivity(intent);
                }

            }
        });
    }
}
