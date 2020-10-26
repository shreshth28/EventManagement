package com.example.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterStudentActivity extends AppCompatActivity {

    private EditText studentName;
    private EditText studentPhone;
    private EditText studentRoom;
    private EditText studentPass;
    private EditText studentReg;
    private EditText studentEmail;
    private Button studentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);
        studentName=findViewById(R.id.studentName);
        studentPhone=findViewById(R.id.studentPhone);
        studentPass=findViewById(R.id.studentPassword);
        studentRoom=findViewById(R.id.roomNumber);
        studentReg=findViewById(R.id.registrationNumber);
        studentEmail=findViewById(R.id.studentEmail);
        studentSignUp=findViewById(R.id.studentSignUp);
        studentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }
    private void signUp() {
        String name=studentName.getText().toString();
        String email=studentEmail.getText().toString();
        String phno=studentPhone.getText().toString();
        String reg=studentReg.getText().toString();
        String roomno=studentReg.getText().toString();
        String password=studentPass.getText().toString();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mDatabase = database.getReference().child("user").child(uid);
                            mDatabase.child("email").setValue(email);
                            mDatabase.child("name").setValue(name);
                            mDatabase.child("phno").setValue(phno);
                            mDatabase.child("reg").setValue(reg);
                            mDatabase.child("room no").setValue(roomno);
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("deptCheck");;
                            reference.child(uid).setValue(0);

                        } else {
                            Toast.makeText(RegisterStudentActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}