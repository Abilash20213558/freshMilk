package com.example.madp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity4 extends AppCompatActivity {

    EditText gmail, password;
    Button signupbtn;
    TextView signinbtn;
    FirebaseDatabase db;
    DatabaseReference reference ;
    FirebaseAuth mFirebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        getSupportActionBar().hide();

        mFirebaseAuth = FirebaseAuth.getInstance();
        gmail = findViewById(R.id.gmail);
        password = findViewById(R.id.password);
        signupbtn = findViewById(R.id.signupbtn);
        signinbtn = findViewById(R.id.signinBtn);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dgmail = gmail.getText().toString();
                String dpassword = password.getText().toString();

                if(dgmail.isEmpty()){
                    gmail.setError("Please Enter Number");
                    gmail.requestFocus();
                }else if(dpassword.isEmpty()){
                    password.setError("Please Enter Password");
                    password.requestFocus();
                }else if(dgmail.isEmpty() && dpassword.isEmpty()){
                    Toast.makeText(MainActivity4.this,"Fields are Empty", Toast.LENGTH_SHORT).show();
                }else if(!(dgmail.isEmpty() && dpassword.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(dgmail,dpassword).addOnCompleteListener(MainActivity4.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete( Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity4.this,"SignUp unsuccessful", Toast.LENGTH_SHORT).show();
                            }else {
                                startActivity(new Intent(MainActivity4.this,MainActivity.class));
                            }
                        }
                    });
                }else{
                    Toast.makeText(MainActivity4.this,"Error Occured", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity4.this,MainActivity.class));
            }
        });

    }

}