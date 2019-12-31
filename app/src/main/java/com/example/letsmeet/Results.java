package com.example.letsmeet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Results extends AppCompatActivity {

    TextView tvResults;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference dbrResults = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid()).child("Deconflictor");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        tvResults = (TextView) findViewById(R.id.tvResults);

        dbrResults.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String para = dataSnapshot.getValue().toString().trim();
                tvResults.setText(para);
                tvResults.setMovementMethod(new ScrollingMovementMethod());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
