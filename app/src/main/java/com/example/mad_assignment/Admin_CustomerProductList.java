package com.example.mad_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Admin_CustomerProductList extends AppCompatActivity  {

    private RecyclerView mRecyclerView;
    private AdminProductAdapter mAdapter;
    StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;
    private  List<AdminProductModel> productList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_product_list);

        mRecyclerView = findViewById(R.id.rv_adminCusProductlist);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       getSupportActionBar().setDisplayHomeAsUpEnabled(true );
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#069c53")));
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.main));


        productList =new ArrayList<> ();

        databaseReference = FirebaseDatabase.getInstance().getReference("admin_products");

        //Set the values for every adapter from firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    AdminProductModel product = postSnapshot.getValue(AdminProductModel.class);
                    productList.add(product);
                }
                mAdapter  = new AdminProductAdapter(Admin_CustomerProductList.this,productList);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(Admin_CustomerProductList.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }



}