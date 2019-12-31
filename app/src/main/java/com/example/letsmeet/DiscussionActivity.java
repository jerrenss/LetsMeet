package com.example.letsmeet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DiscussionActivity extends AppCompatActivity {

    Button btnSendMsg;
    EditText etMsg;
    ListView lvDiscussion;
    ArrayList<String> listConversation = new ArrayList<String>();
    ArrayAdapter arrayAdpt;
    TextView tvChatAs, tvMembers, tvUIDs;
    String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String SelectedTopic;
    private DatabaseReference dbrUser = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(UID);
    private DatabaseReference dbrUsernamexUID = FirebaseDatabase.getInstance().getReference().getRoot().child("UsernamexUID");
    private DatabaseReference dbr, dbrTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        btnSendMsg = (Button) findViewById(R.id.bSend);
        tvChatAs = (TextView) findViewById(R.id.tvChatAs);
        tvMembers = (TextView) findViewById(R.id.tvMembers);
        tvUIDs = (TextView) findViewById(R.id.tvUIDs);
        etMsg = (EditText) findViewById(R.id.etChat);
        lvDiscussion = (ListView) findViewById(R.id.lvChat);
        arrayAdpt = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listConversation);
        lvDiscussion.setAdapter(arrayAdpt);

        SelectedTopic = getIntent().getExtras().get("selected_topic").toString();
        setTitle(SelectedTopic);
        dbrTopic = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(UID).child("LetsChat").child(SelectedTopic).child("Collaborators");
        dbr = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(UID).child("LetsChat").child(SelectedTopic).child("Content");

        dbrUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp = dataSnapshot.child("Name").getValue().toString();
                String text =  "Chatting as: " + temp;
                tvChatAs.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbrTopic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp = "";
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getValue().toString();
                    String adding = name + ", ";
                    temp += adding;
                }
                temp += "are Group members";
                tvMembers.setText(temp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbrUsernamexUID.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> Usernames = getUserNames();
                String temp = "";
                for(String s : Usernames) {
                    String temp2 = dataSnapshot.child(s).getValue().toString().trim() + ", ";
                    temp += temp2;
                }
                temp += "are your UIDs";
                tvUIDs.setText(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToDatabase(getUIDs());

            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                updateConversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.itemClearChat:
                AlertDialog.Builder confirmClear = new AlertDialog.Builder(this);
                confirmClear.setMessage("Are you sure you want to clear this chat?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearChat();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = confirmClear.create();
                alert.setTitle("Warning!");
                alert.show();
                return true;
            case R.id.itemDeleteChat:
                AlertDialog.Builder confirmDelete = new AlertDialog.Builder(this);
                confirmDelete.setMessage("Are you sure you want to delete this chat? All members will leave the group and previous chat history will be cleared entirely. THIS ACTION IS IRREVERSIBLE!").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectingDelete();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert2 = confirmDelete.create();
                alert2.setTitle("Warning!");
                alert2.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    public void updateConversation(DataSnapshot dataSnapshot) {
        String msg, user, conversation;
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            msg = (String) ((DataSnapshot)i.next()).getValue();
            user = (String) ((DataSnapshot)i.next()).getValue();
            conversation = user + ": " + msg;
            int length = listConversation.size();
            arrayAdpt.insert(conversation, length);
            arrayAdpt.notifyDataSetChanged();
        }
    }

    private void sendToDatabase(ArrayList<String> UIDs) {
        DatabaseReference dbrAllMembers;
        for(String s : UIDs) {
            String UserName = (tvChatAs.getText().toString().split(":"))[1].trim();
            String user_msg_key;
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm");
            String dateAndTime = format.format(calendar.getTime());
            Map<String, Object> map = new HashMap<String, Object>();
            dbrAllMembers = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(s).child("LetsChat").child(SelectedTopic).child("Content");
            user_msg_key = dbrAllMembers.push().getKey();
            dbrAllMembers.updateChildren(map);
            DatabaseReference dbr2 = dbrAllMembers.child(user_msg_key);
            Map<String, Object> map2 = new HashMap<>();
            String msg = etMsg.getText().toString();
            String finalmsg = msg + "\n" + dateAndTime;
            map2.put("msg", finalmsg);
            map2.put("user", UserName);
            dbr2.updateChildren(map2);
        }

        etMsg.setText("");
    }

    private ArrayList<String> getUserNames() {
        String temp = tvMembers.getText().toString().trim();
        String[] parts = temp.split(",");
        ArrayList<String> UserNames = new ArrayList<>();
        for(int i = 0; i < parts.length - 1; i++) {
            UserNames.add(parts[i].trim());
        }
        return UserNames;
    }

    private ArrayList<String> getUIDs() {
        String temp = tvUIDs.getText().toString().trim();
        String[] parts = temp.split(",");
        ArrayList<String> UIDs = new ArrayList<>();
        for(int i = 0; i < parts.length - 1; i++) {
            UIDs.add(parts[i].trim());
        }
        return UIDs;
    }

    private void clearChat() {
        DatabaseReference dbrTemp = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(UID).child("LetsChat").child(SelectedTopic).child("Content");
        dbrTemp.setValue("");
        Toast.makeText(this, "Chat Cleared", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }

    //Delete Chat, Only available to Group Admin
    private void deleteChat(ArrayList<String> UIDs) {
        DatabaseReference dbrTemp;
        DatabaseReference dbrTemp2 = FirebaseDatabase.getInstance().getReference().getRoot().child("GroupNames");
        for(String s : UIDs) {
            dbrTemp = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(s).child("LetsChat").child(SelectedTopic);
            dbrTemp.removeValue();
        }
        dbrTemp2.child(SelectedTopic).removeValue();
        Intent intent = new Intent(DiscussionActivity.this, GroupCreation.class);
        startActivity(intent);
    }

    private void selectingDelete() {
        DatabaseReference dbrMember1 = dbrTopic.child("Member1");
        dbrMember1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String temp[] = tvChatAs.getText().toString().trim().split(":");
                if(dataSnapshot.getValue().equals(temp[1].trim())) {
                    deleteChat(getUIDs());
                } else {
                    Toast.makeText(DiscussionActivity.this, "Only Group's Creator " + dataSnapshot.getValue().toString() + " is able to delete chat", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
