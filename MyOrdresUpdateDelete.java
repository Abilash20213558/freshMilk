package com.example.mad_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class MyOrdresUpdateDelete extends AppCompatActivity {
    TextView productName, quantity, unitPrice, totalAmount;
    ImageView imageView;
    Button updateBtn, deleteBtn;
    String getprodName, getunitPrice, getQty, getImage, getTotalAmount;
    double total;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ordres_update_delete);

        productName = findViewById(R.id.et_name);
        quantity = findViewById(R.id.et_quantity);
        unitPrice = findViewById(R.id.et_unitPrice);
        totalAmount = findViewById(R.id.et_totalAmount);
        imageView = findViewById(R.id.productImage);
        updateBtn = findViewById(R.id.btn_update);
        deleteBtn = findViewById(R.id.btn_delete);

        storageReference = FirebaseStorage.getInstance().getReference("my_orders");
        databaseReference = FirebaseDatabase.getInstance().getReference("my_orders");

        Intent intent = getIntent();
        getprodName = intent.getStringExtra("productName");
        getunitPrice = intent.getStringExtra("unitPrice");
        getQty = intent.getStringExtra("quantity");
        getTotalAmount = intent.getStringExtra("totalAmount");
        getImage = intent.getStringExtra("image_url");

        productName.setText(getprodName);
        unitPrice.setText(getunitPrice);
        totalAmount.setText(getTotalAmount);
        quantity.setText(getQty);
        Picasso
                .get()
                .load(getImage)
                .fit()
                .into(imageView);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                beginUpdate();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFunction();

            }
        });

        //For getting value automatically to the editText
        TextWatcher textWatcher = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                calculate();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }
        };

        quantity.addTextChangedListener(textWatcher);
        unitPrice.addTextChangedListener(textWatcher);

    }

    //Total amount calculation
    public void calculate() {
        double quantityInt = 1;
        double unitInt = 0;

        if (quantity != null)
            quantityInt = Double.parseDouble(!quantity.getText().toString().equals("") ?
                    quantity.getText().toString() : "0");

        if (unitPrice != null)
            unitInt = Double.parseDouble(!unitPrice.getText().toString().equals("") ?
                    unitPrice.getText().toString() : "0");

        total = quantityInt * unitInt;
        String textResult = "Rs."+ total;
        totalAmount.setText(textResult);
    }

    private void deleteFunction() {
        String currentProductName = getprodName;
        String currentImageURL = getImage;

        ShowDeleteDialog(currentProductName,currentImageURL);
    }

    private void ShowDeleteDialog(String currentProductName, String currentImageURL) {
        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MyOrdresUpdateDelete.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to remove from My-Orders?");
        //Yes Button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query mquery = databaseReference.orderByChild("prodName").equalTo(currentProductName);
                mquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(MyOrdresUpdateDelete.this,"Product Deleted...",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MyOrdresUpdateDelete.this, MyOrders.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MyOrdresUpdateDelete.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

                //Delete Image From Storage
                StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentImageURL);
                mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Toast.makeText(AdminProductUpdate.this,"Image Deleted",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(MyOrdresUpdateDelete.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        //No button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginUpdate() {
        deletePreviousImage();

    }
    private void deletePreviousImage() {
        StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(getImage);
        mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //deleted
                uploadImage();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyOrdresUpdateDelete.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void uploadImage() {
        String imageName = System.currentTimeMillis() + ".jpeg";
        //Storage Reference
        StorageReference storageReference2 = storageReference.child(imageName);
        //get bitmap from imageView
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Compress Image
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = storageReference2.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(MyOrdresUpdateDelete.this, "New Image Uploaded...", Toast.LENGTH_SHORT).show();

                //get Url of newly uploaded image
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();
                //Update Database with new Data
                updateDatabase(downloadUri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyOrdresUpdateDelete.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDatabase(String s) {
        String PrName = productName.getText().toString();
        String PrUnitPrice = unitPrice.getText().toString();
        String prQty = quantity.getText().toString();
        String prTot = totalAmount.getText().toString();

        Query query = databaseReference.orderByChild("prodName").equalTo(getprodName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //Update Data
                    ds.getRef().child("prodName").setValue(PrName);
                    ds.getRef().child("unitPrice").setValue(PrUnitPrice);
                    ds.getRef().child("quantity").setValue(prQty);
                    ds.getRef().child("totalAmount").setValue(prTot);
                    ds.getRef().child("imageURL").setValue(s);

                }
                Toast.makeText(MyOrdresUpdateDelete.this, "Updating Successful..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MyOrdresUpdateDelete.this, MyOrders.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}