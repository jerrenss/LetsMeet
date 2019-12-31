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

public class AddAgenda extends AppCompatActivity {

    Button bAddAgenda;
    EditText etTitle, etItem;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference dbrAgendaList = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid()).child("AgendaList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agenda);
        setTitle("Add Agenda");

        bAddAgenda = (Button) findViewById(R.id.btnAddAgenda);
        etItem = (EditText) findViewById(R.id.etItem);
        etTitle = (EditText) findViewById(R.id.etTitle);

        bAddAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etTitle.getText().toString().trim())) {
                    Toast.makeText(AddAgenda.this, "Please fill in Title", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etItem.getText().toString().trim())) {
                    Toast.makeText(AddAgenda.this, "Please fill in Agenda", Toast.LENGTH_SHORT).show();
                } else {
                    String title = etTitle.getText().toString().trim();
                    if(title.contains(",") || title.contains("/") || title.contains(".") || title.contains("?")) {
                        Toast.makeText(AddAgenda.this, "Unsuitable punctutation used in Agenda Title", Toast.LENGTH_SHORT).show();
                    } else {
                        String item = etItem.getText().toString();
                        String finalContent = title + "\n" + item;
                        dbrAgendaList.child(title).setValue(finalContent);
                        Toast.makeText(AddAgenda.this, "Agenda Successfully Added!", Toast.LENGTH_SHORT).show();
                        etTitle.setText("");
                        etItem.setText("");
                        Intent intent = new Intent(AddAgenda.this, Agenda2.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
