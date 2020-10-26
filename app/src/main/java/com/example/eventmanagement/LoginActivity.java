package com.example.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button loginButton;
    private Button registerClubButton;
    private EditText emailTextView;
    private EditText passwordTextView;
    private Button areYouStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        areYouStudent=findViewById(R.id.studentLoginBtn);
        mAuth = FirebaseAuth.getInstance();
        loginButton=findViewById(R.id.loginButton);
        registerClubButton=findViewById(R.id.registerClubButton);
        emailTextView=findViewById(R.id.editTextTextEmailAddress);
        passwordTextView=findViewById(R.id.editTextTextPassword);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        registerClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerClubIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerClubIntent);
            }
        });
        areYouStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studentRegScreen=new Intent(LoginActivity.this,RegisterStudentActivity.class);
                startActivity(studentRegScreen);
            }
        });
    }

    private void signIn() {
        String emailid=emailTextView.getText().toString();
        String pswd=passwordTextView.getText().toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth.signInWithEmailAndPassword(emailid, pswd)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            DatabaseReference mDatabase = database.getReference();
                            final DatabaseReference emid =
                                    mDatabase.child("deptCheck").child(uid);
                            emid.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String check = dataSnapshot.getValue().toString();
                                    if (check.equals("0")){
                                        startActivity(new
                                                Intent(LoginActivity.this,StudAccount.class));
                                    }
                                    else{
                                        startActivity(new
                                                Intent(LoginActivity.this,clubAcc.class));
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}