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

public class DeleteSchedule extends AppCompatActivity {

    EditText etDateDelete;
    Button bDateDelete;
    FirebaseAuth user = FirebaseAuth.getInstance();
    DatabaseReference dbrScheduleContent = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(user.getCurrentUser().getUid()).child("MySchedule").child("Content");
    DatabaseReference dbrScheduleConversion = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(user.getCurrentUser().getUid()).child("MySchedule").child("Conversion");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_schedule);
        etDateDelete = (EditText) findViewById(R.id.etDateDelete);
        bDateDelete = (Button) findViewById(R.id.bDateDelete);

        bDateDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etDateDelete.getText().toString().trim())) {
                    Toast.makeText(DeleteSchedule.this, "Please fill in date", Toast.LENGTH_SHORT).show();
                } else if(!etDateDelete.getText().toString().trim().matches("\\d{2}\\-\\d{2}\\-\\d{4}")) {
                    Toast.makeText(DeleteSchedule.this, "Please use the correct entry format", Toast.LENGTH_SHORT).show();
                }else {
                    String temp = etDateDelete.getText().toString().trim();
                    //change formate to dd-mm-yyyy
                    String pieces[] = temp.split("-");
                    String newTemp = pieces[2] + "-" + pieces[1] + "-" + pieces[0];
                    dbrScheduleContent.child(newTemp).removeValue();
                    dbrScheduleConversion.child(newTemp).removeValue();
                    etDateDelete.setText("");
                    Intent intent = new Intent(DeleteSchedule.this, ViewSchedule.class);
                    startActivity(intent);
                }

            }
        });
    }
}
