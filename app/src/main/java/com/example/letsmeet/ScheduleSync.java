package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScheduleSync extends AppCompatActivity {

    EditText etRangeOfDates, etSyncUsername;
    Button bAddOtherSchedule, bResetSync, bSync;
    TextView tvAllSchedule, tvTemp;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference dbrUxU = FirebaseDatabase.getInstance().getReference().getRoot().child("UsernamexUID");
    private DatabaseReference dbrAllUsersSynced = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid()).child("MySchedule").child("AllUsersSynced");
    ArrayList<String> wholeListOfUnavailability = new ArrayList<>();

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
        setContentView(R.layout.activity_schedule_sync);
        setTitle("Schedule Sync");

        etRangeOfDates = findViewById(R.id.etRangeOfDates);
        etSyncUsername = findViewById(R.id.etSyncUsername);
        bAddOtherSchedule = findViewById(R.id.bAddOtherSchedule);
        bResetSync = findViewById(R.id.bResetSync);
        bSync = findViewById(R.id.bSync);
        tvAllSchedule = findViewById(R.id.tvAllSchedule);
        tvTemp = findViewById(R.id.tvTemp);


        dbrAllUsersSynced.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    String temp = "Users Synced: ";
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        temp += "\n";
                        temp += ds.getKey();
                    }
                    tvAllSchedule.setText(temp);
                } else {
                    tvAllSchedule.setText("No Schedules Added");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        bAddOtherSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etSyncUsername.getText().toString().trim())) {
                    Toast.makeText(ScheduleSync.this, "Please fill in Username", Toast.LENGTH_SHORT).show();
                } else {
                    String username = etSyncUsername.getText().toString().trim();
                    checkUsername(username);
                }




            }
        });

        bResetSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholeListOfUnavailability.clear();
                etRangeOfDates.setText("");
                etSyncUsername.setText("");
                dbrAllUsersSynced.removeValue();
            }
        });

        bSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etRangeOfDates.getText().toString().trim())) {
                    Toast.makeText(ScheduleSync.this, "Please Fill In Meeting Dates", Toast.LENGTH_SHORT).show();
                } else if(!etRangeOfDates.getText().toString().trim().matches("\\d{2}\\/\\d{2}\\/\\d{4}\\-\\d{2}\\/\\d{2}\\/\\d{4}")) {
                    Toast.makeText(ScheduleSync.this, "Please use the correct entry format", Toast.LENGTH_SHORT).show();
                } else {
                    String rangeOfDates = etRangeOfDates.getText().toString().trim();
                    String[] startAndEnd = rangeOfDates.split("-");
                    Organiser organiser = new Organiser(startAndEnd);
                    ArrayList<Unavailable> unavail = convertStringArray(wholeListOfUnavailability);
                    deconflict(organiser.allDates, unavail);

                    String para = "";
                    for(MyDate md : organiser.allDates) {
                        para += md.printDay();
                        para += "\n";
                        para += md.printTime();
                    }

                    DatabaseReference dbrResults = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid()).child("Deconflictor");
                    dbrResults.setValue(para);

                    Intent intent = new Intent(ScheduleSync.this, Results.class);
                    startActivity(intent);
                }

            }
        });


    }

    private void getUID(String username) {
        dbrUxU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String UID = dataSnapshot.child(username).getValue().toString().trim();
                getContent(UID);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getContent(String UID) {
        DatabaseReference otherdbr = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(UID).child("MySchedule").child("Conversion");
        otherdbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp = "";
                if(dataSnapshot.hasChildren()) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String content = ds.getValue().toString().trim();
                        temp += content;
                    }
                    wholeListOfUnavailability.add(temp);
                } else {}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void printAll(ArrayList<String> unavailability) {
        String temp = "";
        for(String s : unavailability) {
            String[] pieces = s.split(",");
            for(String t : pieces) {
                temp += t;
                temp += "\n";
            }
        }
        tvAllSchedule.setText(temp);
        tvAllSchedule.setMovementMethod(new ScrollingMovementMethod());
    }

    private ArrayList<Unavailable> convertStringArray(ArrayList<String> unavailability) {
        ArrayList<String> allPieces = new ArrayList<>();
        ArrayList<Unavailable> temp = new ArrayList<>();
        for(String s : unavailability) {
            String[] pieces = s.split(",");
            for(String t : pieces) {
                allPieces.add(t);
            }
        }

        for(String st : allPieces) {
            String[] subcontent = st.split(":");
            String[] tempDate = subcontent[0].split("-");
            String[] tempTime = subcontent[1].split("-");
            Unavailable u = new Unavailable(Integer.parseInt(tempDate[0]), Integer.parseInt(tempDate[1]), Integer.parseInt(tempDate[2]), Integer.parseInt(tempTime[0]), Integer.parseInt(tempTime[1]));
            temp.add(u);
        }

        return temp;
    }


    private void checkUsername(String username) {
        dbrUxU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean checker = dataSnapshot.hasChild(username);
                if(checker) {
                    getUID(username);
                    dbrAllUsersSynced.child(username).setValue("");
                    Toast.makeText(ScheduleSync.this, "" + username + "'s Schedule Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ScheduleSync.this, "Username Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
