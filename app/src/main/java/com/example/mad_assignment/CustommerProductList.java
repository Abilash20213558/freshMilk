package com.example.mad_assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CustommerProductList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private CustomerProductAdapter mAdapter;
    StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;
    private List<AdminProductModel> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custommer_product_list);

        mRecyclerView = findViewById(R.id.rv_CustomerProductlist);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true );
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b7bd04")));
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.second));

        productList =new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("admin_products");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    AdminProductModel product = postSnapshot.getValue(AdminProductModel.class);
                    productList.add(product);
                }
                mAdapter  = new CustomerProductAdapter(CustommerProductList.this,productList);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(CustommerProductList.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}