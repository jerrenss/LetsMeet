package com.example.letsmeet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    private EditText etRName, etREmail, etRPassword;
    private Button bRegister;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot().child("Users");
    private DatabaseReference dbr2 = FirebaseDatabase.getInstance().getReference().getRoot().child("UsernamexUID");
    ValueEventListener vel = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            boolean checker = dataSnapshot.hasChild(etRName.getText().toString().trim());
            if (!checker) {
                registerUser();
            } else {
                Toast.makeText(Register.this, "Username Already Taken!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etRName = (EditText) findViewById(R.id.etRName);
        etREmail = (EditText) findViewById(R.id.etREmail);
        etRPassword = (EditText) findViewById(R.id.etRPassword);
        bRegister = (Button) findViewById(R.id.bRegister);
        firebaseAuth = FirebaseAuth.getInstance();



        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etREmail.getText().toString().trim()) || TextUtils.isEmpty(etRPassword.getText().toString().trim()) || TextUtils.isEmpty(etRName.getText().toString().trim())) {
                    Toast.makeText(Register.this, "Please fill in all details!", Toast.LENGTH_LONG).show();
                } else {
                    checkUsername();
                }

            }
        });


    }

    public void registerUser() {
        final ProgressDialog progressDialog = ProgressDialog.show(Register.this, "Please Wait...", "Processing...", true);
        (firebaseAuth.createUserWithEmailAndPassword(etREmail.getText().toString().trim(), etRPassword.getText().toString().trim())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    dbr2.removeEventListener(vel);
                    String UID = FirebaseAuth.getInstance().getUid();
                    dbr.child(UID).setValue("");
                    dbr = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(UID);
                    dbr.child("Name").setValue(etRName.getText().toString().trim());
                    dbr.child("AgendaList").setValue("");
                    dbr.child("LetsChat").setValue("");
                    dbr.child("MySchedule").child("Content").setValue("");
                    dbr.child("MySchedule").child("Conversion").setValue("");
                    dbr2.child(etRName.getText().toString().trim()).setValue(UID);
                    Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(Register.this, MainActivity.class);
                    startActivity(i);
                } else {
                    Log.e("ERROR", task.getException().toString());
                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkUsername() {
        dbr2.addListenerForSingleValueEvent(vel);
    }

}
