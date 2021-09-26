package com.example.mad_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    public static final String TAG = "TAG";
    Button signUpBtn, signInBtn;
    EditText mmName, mEmail, mPwd, mConfirmPwd;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpBtn = findViewById(R.id.btn_signUp);
        signInBtn = findViewById(R.id.btn_signIn);
        mName = findViewById(R.id.et_name);
        mEmail = findViewById(R.id.et_emailId);
        mPwd = findViewById(R.id.et_password);
        mConfirmPwd = findViewById(R.id.et_confirmPassword);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        //Already user loggedIn
        if (fauth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        //Login Btn
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        //Signup Btn
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString().trim();
                final String password = mPwd.getText().toString().trim();
                final String name = mName.getText().toString().trim();
                final String confirmPwd = mConfirmPwd.getText().toString().trim();

                //set the error if the fields are empty
                if (TextUtils.isEmpty(name)) {
                    mName.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPwd.setError("Password is Required");
                    return;
                }
                if (TextUtils.isEmpty(confirmPwd)) {
                    mConfirmPwd.setError("Confirm Password is Required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                if (password.length() < 6) {
                    mPwd.setError("Password must be >= 6 Characters");
                    return;
                }
                if (!password.equals(confirmPwd)) {
                    Toast.makeText(signup.this, "Both Passwords must be equal..", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Register use in firebase
                fauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signup.this, "Signup Successfully...", Toast.LENGTH_SHORT).show();
                            userID = fauth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);

                            //create Hashmap object as user
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", name);
                            user.put("Email", email);
                            user.put("Password", password);
                            user.put("ConfirmPassword", confirmPwd);


                            //check database
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is Created is for" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            //Intent
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(signup.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                //set the fields empty
                mName.setText(null);
                mEmail.setText(null);
                mPwd.setText(null);
                mConfirmPwd.setText(null);

            }
        });
    }
}
