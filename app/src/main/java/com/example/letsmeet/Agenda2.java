package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Agenda2 extends AppCompatActivity {

    ListView lvItems;
    ArrayList<String> items = new ArrayList<>();
    ArrayAdapter<String> itemsAdapter;
    String UID = FirebaseAuth.getInstance().getUid();
    private Toolbar toolbar;
    private Button btnMainMenu;
    private DatabaseReference dbrAgendaList = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(UID).child("AgendaList");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda2);
        setTitle("Agenda List");

        btnMainMenu = findViewById(R.id.btnMainMenu);

        lvItems = (ListView) findViewById(R.id.lvItems);

        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);


        dbrAgendaList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateAgenda(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Agenda2.this, MainMenu.class);
                startActivity(intent);
            }
        });
    }


    public void updateAgenda(DataSnapshot dataSnapshot) {
        String content;
        for(DataSnapshot ds : dataSnapshot.getChildren()) {
            content = ds.getValue().toString();
            itemsAdapter.insert(content, 0);
            itemsAdapter.notifyDataSetChanged();
        }
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
                Intent intent = new Intent(Agenda2.this, AddAgenda.class);
                startActivity(intent);
                return true;
            case R.id.itemDelete:
                Intent intent2 = new Intent(Agenda2.this, DeleteAgenda.class);
                startActivity(intent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

