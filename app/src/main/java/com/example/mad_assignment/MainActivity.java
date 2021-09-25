package com.example.mad_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {
    EditText loginEmail,loginPassword;
    Button loginBtn;
    TextView signUpBtn;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpBtn  = findViewById(R.id.tv_signUp);
        loginEmail = findViewById(R.id.et_emailId);
        loginPassword   = findViewById(R.id.et_password);
        loginBtn   = findViewById(R.id.btn_logIn);

        fauth = FirebaseAuth.getInstance();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),signup.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login(){
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            loginEmail.setError("Email is Required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            loginPassword.setError("Password is Required");
            return;
        }
        if(password.length() < 6){
            loginPassword.setError("Password must be >= 6 Characters");
            return;
        }
       if((email.equals("admin@gmail.com")) && (password.equals("admin123"))){
            Intent intent = new Intent(MainActivity.this,AdminDashBoard.class);
            startActivity(intent);
            finish();
            Toast.makeText(MainActivity.this,"Logged In as Admin",Toast.LENGTH_SHORT).show();
       }
 else
       {
           Toast.makeText(MainActivity.this,"Incorrect Credentials",Toast.LENGTH_SHORT).show();
       }
       if((!email.equals("admin@gmail.com")) && (!password.equals("admin123"))) {
           //Authenticate the user
           //Authenticate user
           fauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if (task.isSuccessful()) {
                       Toast.makeText(MainActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));

                   } else {
                       Toast.makeText(MainActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                   }

               }
           });
       }
        //Empty EditText
        loginEmail.setText(null);
        loginPassword.setText(null);

    }
}