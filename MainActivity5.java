package com.example.madp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity5 extends AppCompatActivity {

    Button verifybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        getSupportActionBar().hide();

        verifybtn = findViewById(R.id.verifybtn);

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity5.this,MainActivity3.class);
                startActivity(i);

                Toast.makeText(MainActivity5.this,"Verified successfully",Toast.LENGTH_SHORT).show();
            }
        });



    }
}