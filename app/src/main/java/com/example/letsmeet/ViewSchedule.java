package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewSchedule extends AppCompatActivity {

    TextView tvViewSchedule;
    FirebaseAuth user = FirebaseAuth.getInstance();
    private Button btnOrganisingOptions;
    DatabaseReference dbrScheduleContent = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(user.getCurrentUser().getUid()).child("MySchedule").child("Content");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);
        setTitle("My Schedule");

        btnOrganisingOptions = (Button) findViewById(R.id.btnOrganisingOptions);
        tvViewSchedule = (TextView) findViewById(R.id.tvViewSchedule);

        dbrScheduleContent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp = createContent(dataSnapshot);
                tvViewSchedule.setText(temp);
                tvViewSchedule.setMovementMethod(new ScrollingMovementMethod());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnOrganisingOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewSchedule.this, OrganisingOptions.class);
                startActivity(intent);
            }
        });

    }
    private String convertContent(String string) {
        String temp = "";
        String[] temp1 = string.split(",");
        for(String s : temp1) {
            String[] temp2 = s.split(".");
            String finalstring = temp2[0] + " (" + temp2[1] + ")";
            temp += finalstring;
            temp += "\n";
        }

        return temp;
    }

    private String createContent(DataSnapshot dataSnapshot) {
        String temp = "";
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            String date = ds.getKey();
            //swap the date from yyyy-mm-dd to dd-mm-yyyy
            String[] pieces = date.split("-");
            String newDate = "" + pieces[2] + "-" + pieces[1] + "-" + pieces[0];
            temp += newDate;
            temp += "\n";
            String timings = ds.getValue().toString().trim();
            String[] joke = timings.split(",");
            for(String s : joke) {
                temp += s;
                temp += "\n";
            }
            temp += "---------------------------------------------------------------------" + "\n";
        }
        return temp;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.itemAdd:
                Intent intent = new Intent(ViewSchedule.this, AddSchedule.class);
                startActivity(intent);
                return true;
            case R.id.itemDelete:
                Intent intent2 = new Intent(ViewSchedule.this, DeleteSchedule.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
