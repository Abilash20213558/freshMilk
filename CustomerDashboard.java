package com.example.mad_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CustomerDashboard extends AppCompatActivity {
    private CardView productList,myOrder,profile,logOut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        productList = findViewById(R.id.list_product);
        myOrder = findViewById(R.id.my_orders);
        profile = findViewById(R.id.viewProfile);
        logOut = findViewById(R.id.logout);

        productList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDashboard.this,CustommerProductList.class);
                startActivity(intent);
            }
        });

        myOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDashboard.this,MyOrders.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDashboard.this,ProfileUpdateDelete.class);
                startActivity(intent);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustomerDashboard.this, "Logging out...", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CustomerDashboard.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
hello
