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

public class MyOrders extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyOrdersAdapter mAdapter;
    StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ValueEventListener mDBListener;
    private List<myOrderModel> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders_list);

        mRecyclerView = findViewById(R.id.rv_myOrderList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true );
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b7bd04")));
        getWindow().setStatusBarColor(this.getResources().getColor(R.color.second));


        productList =new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("my_orders");

        //Set the values for every adapter from firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    myOrderModel product = postSnapshot.getValue(myOrderModel.class);
                    productList.add(product);
                }
                mAdapter  = new MyOrdersAdapter(MyOrders.this,productList);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void onCancelled( DatabaseError error) {
                Toast.makeText(MyOrders.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}