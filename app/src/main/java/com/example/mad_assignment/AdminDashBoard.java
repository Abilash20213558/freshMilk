package com.example.mad_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AdminDashBoard extends AppCompatActivity {
private CardView productList,addProduct;
ImageView logOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        productList = findViewById(R.id.list_product);
        addProduct = findViewById(R.id.add_product);
        logOutBtn = findViewById(R.id.logoutBtn);


        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log-Out function
                Toast.makeText(AdminDashBoard.this, "Logging out...", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdminDashBoard.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        productList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashBoard.this,Admin_CustomerProductList.class);
                startActivity(intent);
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminDashBoard.this,Add_Product.class);
                startActivity(intent);
            }
        });
    }
}