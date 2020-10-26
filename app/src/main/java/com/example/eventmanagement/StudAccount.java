package com.example.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class StudAccount extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_account);
        recyclerView=findViewById(R.id.allEventsRV);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ArrayList<Event> list=new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("club").child("eventdisplay");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> map= (Map<String, Object>) dataSnapshot.getValue();
                for(Map.Entry<String,Object> entry:map.entrySet())
                {
                    Map myEvent= (Map)entry.getValue();
                    String description= (String) myEvent.get("Description");
                    String name= (String) myEvent.get("name");
                    String regLink= (String) myEvent.get("Registration Link");
                    String image=(String) myEvent.get("Logo_url");
                    String UID=(String)myEvent.get("EventUID");
                    Event event=new Event(name,image,description,regLink,UID);
                    list.add(event);
                    Toast.makeText(StudAccount.this, myEvent.toString(), Toast.LENGTH_SHORT).show();
                }
                Event[] data=new Event[list.size()];
                data=list.toArray(data);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(StudAccount.this);
                EventsAdapter adapter=new EventsAdapter(data,StudAccount.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}