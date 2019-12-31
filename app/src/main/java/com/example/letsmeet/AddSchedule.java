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

import java.util.ArrayList;

public class AddSchedule extends AppCompatActivity {

    EditText etScheduleDate, etTimings, etReason;
    Button bAddTiming, bConfirmAdd;
    FirebaseAuth user = FirebaseAuth.getInstance();
    DatabaseReference dbrScheduleContent = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(user.getCurrentUser().getUid()).child("MySchedule").child("Content");
    DatabaseReference dbrScheduleConversion = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(user.getCurrentUser().getUid()).child("MySchedule").child("Conversion");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        setTitle("Add Schedule Details");

        etScheduleDate = (EditText) findViewById(R.id.etScheduleDate);
        etTimings = (EditText) findViewById(R.id.etTimings);
        etReason = (EditText) findViewById(R.id.etReason);
        bAddTiming = (Button) findViewById(R.id.bAddTiming);
        bConfirmAdd = (Button) findViewById(R.id.bConfirmAdd);
        ArrayList<String> tempArray = new ArrayList<>();
        ArrayList<String> tempArray2 = new ArrayList<>();

        bAddTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etTimings.getText().toString().trim()) || TextUtils.isEmpty(etScheduleDate.getText().toString().trim()) || TextUtils.isEmpty(etReason.getText().toString().trim())) {
                    Toast.makeText(AddSchedule.this, "Please fill in all details", Toast.LENGTH_SHORT).show();
                } else if(!etTimings.getText().toString().trim().matches("\\d{2}\\-\\d{2}")) {
                    Toast.makeText(AddSchedule.this, "Please use the correct entry format", Toast.LENGTH_SHORT).show();
                }else {
                    String timings = etTimings.getText().toString().trim();
                    String[] temp = timings.split("-");
                    if(Integer.parseInt(temp[0]) >= Integer.parseInt(temp[1])) {
                        Toast.makeText(AddSchedule.this, "Invalid timings", Toast.LENGTH_SHORT).show();
                    } else {
                        String reason = etReason.getText().toString().trim();
                        tempArray2.add(timings);
                        String finalString = convert(timings) + " (" + reason + ")";
                        tempArray.add(finalString);
                        etTimings.setText("");
                        etReason.setText("");
                        Toast.makeText(AddSchedule.this, "Added!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        bConfirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etScheduleDate.getText().toString().trim())) {
                    Toast.makeText(AddSchedule.this, "Please fill in date", Toast.LENGTH_SHORT).show();
                } else if(tempArray.size() == 0) {
                    Toast.makeText(AddSchedule.this, "Please fill in timings", Toast.LENGTH_SHORT).show();
                } else if(!etScheduleDate.getText().toString().trim().matches("\\d{2}\\-\\d{2}\\-\\d{4}")) {
                    Toast.makeText(AddSchedule.this, "Please use the correct entry format", Toast.LENGTH_SHORT).show();
                } else {
                    String date = etScheduleDate.getText().toString().trim();
                    //check if date is valid

                    //convert date to form yyyy-mm-dd, key is ordered as such.
                    String pieces[] = date.split("-");
                    String newDate = "" + pieces[2] + "-" + pieces[1] + "-" + pieces[0];

                    DatabaseReference newdbr = dbrScheduleContent.child(newDate);
                    String temp = "";
                    for(String s : tempArray) {
                        temp += (s + ",");
                    }

                    newdbr.setValue(temp);
                    etScheduleDate.setText("");
                    tempArray.clear();

                    String forConversion = "";
                    for(String s : tempArray2) {
                        forConversion = forConversion + date + ":" + s + ",";
                    }
                    dbrScheduleConversion.child(newDate).setValue(forConversion);
                    tempArray2.clear();

                    Toast.makeText(AddSchedule.this, "Date Confirmed!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AddSchedule.this, ViewSchedule.class);
                    startActivity(intent);
                }

            }
        });


    }

    private String convert(String rawTimings) {
        String first = "";
        String second = "";
        String temp[] = rawTimings.split("-");
        first = convertEach(temp[0]);
        second = convertEach(temp[1]);
        return first + "-" + second;
    }

    private String convertEach(String number) {
        String temp = "";
        switch(number) {
            case "00":
                temp = "12am";
                break;
            case "01":
                temp = "1am";
                break;
            case "02":
                temp = "2am";
                break;
            case "03":
                temp = "3am";
                break;
            case "04":
                temp = "4am";
                break;
            case "05":
                temp = "5am";
                break;
            case "06":
                temp = "6am";
                break;
            case "07":
                temp = "7am";
                break;
            case "08":
                temp = "8am";
                break;
            case "09":
                temp = "9am";
                break;
            case "10":
                temp = "10am";
                break;
            case "11":
                temp = "11am";
                break;
            case "12":
                temp = "12pm";
                break;
            case "13":
                temp = "1pm";
                break;
            case "14":
                temp = "2pm";
                break;
            case "15":
                temp = "3pm";
                break;
            case "16":
                temp = "4pm";
                break;
            case "17":
                temp = "5pm";
                break;
            case "18":
                temp = "6pm";
                break;
            case "19":
                temp = "7pm";
                break;
            case "20":
                temp = "8pm";
                break;
            case "21":
                temp = "9pm";
                break;
            case "22":
                temp = "10pm";
                break;
            case "23":
                temp = "11pm";
                break;
            case "24":
                temp = "12am";
                break;
            default:
                break;
        }
        return temp;
    }

}
