package com.example.eventmanagement;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EventsAdapter  extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder>{

    private Activity context;
    private Event[] data;
    public EventsAdapter(Event[] data,Activity context)
    {
        this.data=data;
        this.context=context;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.event,parent,false);
        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        String name=data[position].getEventName();
        String description=data[position].getEventDescription();
        String regLink=data[position].getEventRegLink();
        String imgUrl=data[position].getImageLogoUrl();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Events").child(imgUrl);
        Glide.with(context).load(storageReference).into(holder.eventImgView);
        Toast.makeText(context, storageReference.toString(), Toast.LENGTH_SHORT).show();
        holder.eventRegList.setText(regLink);
        holder.eventDescription.setText(description);
        holder.eventName.setText(name);
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delFunction(position);
            }
        });
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delFunction(position);
                Intent addEvent=new Intent(context,AddEventDetails.class);
                context.startActivity(addEvent);
                context.finish();

            }
        });
    }

    private void delFunction(int position) {
        String UID=data[position].getEventUID();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        DatabaseReference df=ref.child("club").child(user.getUid()).child("events").child(UID);
        df.removeValue();
        DatabaseReference dr=ref.child("club").child("eventdisplay").child(UID);
        dr.removeValue();
    }

    @Override
    public int getItemCount() {
        if(data==null)
            return 0;
        return data.length;
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder{
        ImageView eventImgView;
        TextView eventName;
        TextView eventDescription;
        TextView eventRegList;
        Button delBtn;
        Button updateBtn;
        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImgView=itemView.findViewById(R.id.listEventUrlImage);
            eventName=itemView.findViewById(R.id.listEventName);
            eventDescription=itemView.findViewById(R.id.listEventDescription);
            eventRegList=itemView.findViewById(R.id.listEventRegLink);
            delBtn=itemView.findViewById(R.id.delBtn);
            updateBtn=itemView.findViewById(R.id.updateBtn);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
            FirebaseUser user=firebaseAuth.getCurrentUser();
            ref.child("deptCheck").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue().toString().equals("0"))
                    {
                        updateBtn.setVisibility(View.INVISIBLE);
                        delBtn.setVisibility(View.INVISIBLE);
                    }
                    else{
                        updateBtn.setVisibility(View.VISIBLE);
                        delBtn.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
