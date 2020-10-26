package com.example.eventmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class AddEventDetails extends AppCompatActivity {

    private EditText descriptionText;
    private EditText googleFormLinkText;
    private EditText eventNameText;
    private EditText logoUrl;
    private Button addEvent;
    private Button uploadImage;
    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;
    private StorageReference mStorageRef;
    private StorageReference mStorageRe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_details);
        descriptionText=findViewById(R.id.eventDescription);
        googleFormLinkText=findViewById(R.id.registrationLink);
        eventNameText=findViewById(R.id.eventName);
        addEvent=findViewById(R.id.registerEvent);
        uploadImage=findViewById(R.id.uploadImage);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processEvent();
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }
    private void processEvent() {
        String description=descriptionText.getText().toString();
        String eventName=eventNameText.getText().toString();
        String googleFormLink=googleFormLinkText.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference();
        final DatabaseReference emid = mDatabase.child("club").child(uid);
        final DatabaseReference ch1 = mDatabase.child("club").child("eventdisplay");
        final DatabaseReference ch2 = emid.child("logo_url");
        final DatabaseReference emid2 = emid.child("events");
        String eventName1 = String.valueOf(System.currentTimeMillis());
        final DatabaseReference events = emid2.child(eventName1);
        final DatabaseReference evedis = ch1.child(eventName1);
        final DatabaseReference logo = events.child("event_url");
        final DatabaseReference logo1 = evedis.child("event_url");
        events.child("Description").setValue(description);
        evedis.child("Description").setValue(description);
        events.child("Registration_Link").setValue(googleFormLink);
        evedis.child("name").setValue(eventName);
        events.child("name").setValue(eventName);
        evedis.child("EventUID").setValue(eventName1);
        events.child("EventUID").setValue(eventName1);
        evedis.child("Registration_Link").setValue(googleFormLink);
        String newEventName = String.valueOf(System.currentTimeMillis());
        uploadFile(newEventName);
        ch2.child("logo_url").setValue(newEventName);
        evedis.child("Logo_url").setValue(newEventName);
        events.child("Logo_url").setValue(newEventName);
        evedis.child("event_url").setValue(newEventName);
        events.child("event_url").setValue(newEventName);
                finish();
    }

    private StorageReference uploadFile(String newEventName) {
        if (filePath != null) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String uid = user.getUid();
            mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRe = mStorageRef.child("Events");
            Uri file = Uri.fromFile(new File(String.valueOf(filePath)));
            final StorageReference fileReference = mStorageRe.child(newEventName);
            fileReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
            });
            return fileReference;
        }
        else {
        }
        return null;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
    }
}