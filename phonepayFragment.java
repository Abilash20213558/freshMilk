package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.madp.MainActivity3;
import com.example.madp.MainActivity4;
import com.example.madp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import javax.security.auth.callback.Callback;

public class phonepayFragment extends Fragment {

    FirebaseDatabase db;
    DatabaseReference reference ;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.phonepaylogin_fragment, container, false);

            EditText gmail = root.findViewById(R.id.gmail);
            EditText password = root.findViewById(R.id.password);
            Button loginbtn = root.findViewById(R.id.loginbtn);
            mFirebaseAuth = FirebaseAuth.getInstance();
            TextView signupBtn = root.findViewById(R.id.signupBtn);

            mAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                    FirebaseUser mFirebaseUSer = mFirebaseAuth.getCurrentUser();
                    if( mFirebaseUSer != null){
                        Toast.makeText(getActivity(),"Logged in",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), MainActivity3.class);
                        startActivity(i);

                    }else{
                        Toast.makeText(getActivity(),"Please Login",Toast.LENGTH_SHORT).show();
                    }
                }

            };

            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String dmail = gmail.getText().toString();
                    String dpassword = password.getText().toString();

                    if(dmail.isEmpty()){
                        gmail.setError("Please Enter Number");
                        gmail.requestFocus();
                    }else if(dmail.isEmpty()){
                        password.setError("Please Enter Passeord");
                        password.requestFocus();
                    }else if(dmail.isEmpty() && dpassword.isEmpty()){
                        Toast.makeText(getActivity(),"Fields are Empty", Toast.LENGTH_SHORT).show();
                    }else if(!(dmail.isEmpty() && dpassword.isEmpty())){
                        mFirebaseAuth.signInWithEmailAndPassword(dmail,dpassword).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete( Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(getActivity(),"SignIn unsuccessful", Toast.LENGTH_SHORT).show();
                                }else {
                                    startActivity(new Intent(getActivity(),MainActivity3.class));
                                }

                            }
                        });
                    }else{
                        Toast.makeText(getActivity(),"Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            signupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   startActivity(new Intent(getActivity(), MainActivity4.class));

                }
            });
            return root;
        }

}
