package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Deconflictor extends AppCompatActivity {

    EditText etOrganiser, etUnavailability;
    Button bAdd, bCalculate, bReset;
    TextView tvCount;
    public int counter = 0;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void deconflict(ArrayList<MyDate> myDateList, ArrayList<Unavailable> unavailableList) {
        int j = 0;
        for(int i = 0; i < unavailableList.size(); i++) {
            while(j < myDateList.size()) {
                if(unavailableList.get(i).same(myDateList.get(j)) && unavailableList.get(i).hasTiming == true) {
                    myDateList.get(j).removeTime(unavailableList.get(i).startTime, unavailableList.get(i).endTime);
                    j = 0;
                    break;
                } else if(unavailableList.get(i).same(myDateList.get(j)) && unavailableList.get(i).hasTiming == false) {
                    myDateList.remove(j);
                    j = 0;
                    break;
                } else {
                    j++;
                }
            }
            j = 0;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deconflictor);
        setTitle("Manual");
        etOrganiser = (EditText) findViewById(R.id.etOrganiser);
        etUnavailability = (EditText) findViewById(R.id.etUnavailability);
        bAdd = (Button) findViewById(R.id.bAdd);
        bCalculate = (Button) findViewById(R.id.bCalculate);
        bReset = (Button) findViewById(R.id.bReset);
        tvCount = (TextView) findViewById(R.id.tvCount);
        ArrayList<Unavailable> unavailableList = new ArrayList<>();


        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etUnavailability.getText().toString().trim())) {
                    Toast.makeText(Deconflictor.this, "Please fill in unavailability!", Toast.LENGTH_LONG).show();
                } else if(!etUnavailability.getText().toString().trim().matches("\\d{2}\\/\\d{2}\\/\\d{4}\\:\\d{2}\\-\\d{2}") && !etUnavailability.getText().toString().trim().matches("\\d{2}\\/\\d{2}\\/\\d{4}") ) {
                    Toast.makeText(Deconflictor.this, "Please use the correct entry format", Toast.LENGTH_SHORT).show();
                } else {
                    String content = etUnavailability.getText().toString().trim();
                    String[] subcontent = content.split(":");
                    if(subcontent.length == 2) {
                        String[] tempDate = subcontent[0].split("/");
                        String[] tempTime = subcontent[1].split("-");
                        Unavailable u = new Unavailable(Integer.parseInt(tempDate[0]), Integer.parseInt(tempDate[1]), Integer.parseInt(tempDate[2]), Integer.parseInt(tempTime[0]), Integer.parseInt(tempTime[1]));
                        unavailableList.add(u);
                        counter++;
                        tvCount.setText(String.valueOf(counter));
                        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        String[] tempDate = subcontent[0].split("/");
                        Unavailable u = new Unavailable(Integer.parseInt(tempDate[0]), Integer.parseInt(tempDate[1]), Integer.parseInt(tempDate[2]));
                        unavailableList.add(u);
                        counter++;
                        tvCount.setText(String.valueOf(counter));
                        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        bCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(etUnavailability.getText().toString().trim()) || TextUtils.isEmpty(etOrganiser.getText().toString().trim())) {
                    Toast.makeText(Deconflictor.this, "Please fill in members' unvailability", Toast.LENGTH_LONG).show();
                } else if(!etOrganiser.getText().toString().trim().matches("\\d{2}\\/\\d{2}\\/\\d{4}\\-\\d{2}\\/\\d{2}\\/\\d{4}")) {
                    Toast.makeText(Deconflictor.this, "Please use the correct entry format", Toast.LENGTH_SHORT).show();
                }else {
                    String content = etOrganiser.getText().toString().trim();
                    String[] startAndEnd = content.split("-");
                    Organiser organiser = new Organiser(startAndEnd);

                    deconflict(organiser.allDates, unavailableList);

                    String para = "";
                    for(MyDate md : organiser.allDates) {
                        para += md.printDay();
                        para += "\n";
                        para += md.printTime();
                    }

                    DatabaseReference dbrResults = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid()).child("Deconflictor");
                    dbrResults.setValue(para);

                    Intent intent = new Intent(Deconflictor.this, Results.class);
                    startActivity(intent);
                }

            }
        });

        bReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unavailableList.clear();
                etOrganiser.setText("");
                etUnavailability.setText("");
                counter = 0;
                tvCount.setText(String.valueOf(counter));
                Toast.makeText(Deconflictor.this, "Data Cleared", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
