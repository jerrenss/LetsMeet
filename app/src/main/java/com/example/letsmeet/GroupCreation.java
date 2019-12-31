package com.example.letsmeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupCreation extends AppCompatActivity {

    ListView lvGroups;
    EditText etAddMembers, etCreateGroup;
    Button bAddMembers, bCreateGroup, bBackToMenu;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid());
    private DatabaseReference dbrLetsChat = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(auth.getCurrentUser().getUid()).child("LetsChat");
    private DatabaseReference dbrUxU = FirebaseDatabase.getInstance().getReference().getRoot().child("UsernamexUID");
    private DatabaseReference dbrGroupNames = FirebaseDatabase.getInstance().getReference().getRoot().child("GroupNames");
    static ArrayList<String> groupNames = new ArrayList<>();
    ArrayList<String> members = new ArrayList<>();
    ArrayAdapter arrayAdpt;
    ArrayList<String> GroupChats = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_creation);
        setTitle("My Groups");
        lvGroups = (ListView) findViewById(R.id.lvGroups);
        etAddMembers = (EditText) findViewById(R.id.etAddMembers);
        etCreateGroup = (EditText) findViewById(R.id.etCreateGroup);
        bAddMembers = (Button) findViewById(R.id.bAddMembers);
        bCreateGroup = (Button) findViewById(R.id.bCreateGroup);
        bBackToMenu = (Button) findViewById(R.id.bBackToMenu);

        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, GroupChats);
        lvGroups.setAdapter(arrayAdpt);

        dbrLetsChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()) {
                    set.add(((DataSnapshot)i.next()).getKey());
                    arrayAdpt.clear();
                    arrayAdpt.addAll(set);
                    arrayAdpt.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp = dataSnapshot.child("Name").getValue().toString();
                members.add(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        lvGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), DiscussionActivity.class);
                i.putExtra("selected_topic", ((TextView)view).getText().toString());
                startActivity(i);
            }
        });

        bAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etAddMembers.getText().toString().trim())) {
                    Toast.makeText(GroupCreation.this, "Please Fill In Member's Username", Toast.LENGTH_SHORT).show();
                } else if(members.contains(etAddMembers.getText().toString().trim())) {
                    Toast.makeText(GroupCreation.this, "User has already been added", Toast.LENGTH_SHORT).show();
                }else {
                    checkUsername();
                }
            }
        });

        bCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(etCreateGroup.getText().toString().trim())) {
                    Toast.makeText(GroupCreation.this, "Please fill in Group Name", Toast.LENGTH_SHORT).show();
                } else if (members.size() == 1) {
                    Toast.makeText(GroupCreation.this, "No other members have been added", Toast.LENGTH_SHORT).show();
                }else {
                    String GroupName = etCreateGroup.getText().toString().trim();
                    if(groupNames.contains(GroupName)) {
                        Toast.makeText(GroupCreation.this, "Please Use Another Group Name", Toast.LENGTH_SHORT).show();
                    } else {
                        groupNames.add(GroupName);
                        dbrGroupNames.child(GroupName).setValue("");
                        getUID(members, GroupName);
                        finish();
                        startActivity(getIntent());
                    }
                }
            }
        });

        bBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupCreation.this, MainMenu.class);
                startActivity(intent);
            }
        });

    }





    private void createGroup(ArrayList<String> members, ArrayList<String> UIDs, String GroupName) {
        DatabaseReference dbrTemp;
        for(String uid : UIDs) {
            dbrTemp = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(uid).child("LetsChat").child(GroupName);
            dbrTemp.child("Collaborators").setValue("");
            dbrTemp.child("Content").setValue("");
            DatabaseReference dbrTemp3 = dbrTemp.child("Collaborators");
            for(int i = 0; i < members.size(); i ++) {
                String memberCount = "Member" + (i + 1);
                dbrTemp3.child(memberCount).setValue(members.get(i));
            }
        }
    }


    private void getUID(ArrayList<String> member, String GroupName) {
        dbrUxU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> tempArray = new ArrayList<>();
                for(String s : member) {
                    String temp = dataSnapshot.child(s).getValue().toString();
                    tempArray.add(temp);
                }

                createGroup(member, tempArray, GroupName);
                Intent refresh = new Intent(GroupCreation.this, GroupCreation.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void checkUsername() {
        String name = etAddMembers.getText().toString().trim();
        dbrUxU.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean checker = dataSnapshot.hasChild(name);
                if(checker) {
                    addMember();
                } else {
                    Toast.makeText(GroupCreation.this, "Username Does Not Exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addMember() {
        String temp = etAddMembers.getText().toString().trim();
        members.add(temp);
        etAddMembers.setText("");
        Toast.makeText(GroupCreation.this, "Current Group Size: " + members.size() + "\n" + memberNames(), Toast.LENGTH_LONG).show();
    }

    private String memberNames() {
        String temp = "";
        for(String s : members) {
            temp += s;
            temp += "\n";
        }
        return temp;
    }

}
