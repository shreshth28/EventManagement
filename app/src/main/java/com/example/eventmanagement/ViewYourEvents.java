package com.example.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class ViewYourEvents extends AppCompatActivity {
    private RecyclerView clubEventsRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_your_events);
        clubEventsRV=findViewById(R.id.club_event_list);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        ArrayList<Event>list=new ArrayList<>();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("club").child(user.getUid()).child("events");
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
                    String eventUID= (String) myEvent.get("EventUID");
                    Event event=new Event(name,image,description,regLink,eventUID);
                    list.add(event);
                    Toast.makeText(ViewYourEvents.this, myEvent.toString(), Toast.LENGTH_SHORT).show();
                }
                Event[] data=new Event[list.size()];
                data=list.toArray(data);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ViewYourEvents.this);
                EventsAdapter adapter=new EventsAdapter(data,ViewYourEvents.this);
                clubEventsRV.setLayoutManager(layoutManager);
                clubEventsRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}