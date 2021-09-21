package com.example.mad_assignment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class CustomerBuyProduct extends AppCompatActivity {
    EditText productName, Liter, unitPrice, quantity, totAmount;
    ImageView imageView;
    Button btnBuy;
    String getprodName, getunitPrice, getLiter, getImage, getQuantity, totalAmount;
    double total;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    StorageTask storageTask;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_buy_product);

        productName = findViewById(R.id.et_Productname);
        unitPrice = findViewById(R.id.et_unitPrice);
        Liter = findViewById(R.id.et_liter);
        quantity = findViewById(R.id.et_quantity);
        imageView = findViewById(R.id.productImage);
        totAmount = findViewById(R.id.et_total);
        btnBuy = findViewById(R.id.btn_buy);

        storageReference = FirebaseStorage.getInstance().getReference("my_orders");
        databaseReference = FirebaseDatabase.getInstance().getReference("my_orders");

        Intent intent = getIntent();
        getprodName = intent.getStringExtra("productName");
        getunitPrice = intent.getStringExtra("unitPrice");
        getLiter = intent.getStringExtra("Liter");
        getImage = intent.getStringExtra("image_url");

        //set the intent values
        productName.setText(getprodName);
        unitPrice.setText(getunitPrice);
        Liter.setText(getLiter);
        Picasso
                .get()
                .load(getImage)
                .fit()
                .into(imageView);

        //Buy Button
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(storageTask != null && storageTask.isInProgress()){
                    Toast.makeText(CustomerBuyProduct.this,"Upload In Progress",Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadFile();
                }
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
        totAmount.setText(textResult);
    }




    //Get the file extension of Image
    private  String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //Upload Data to firebase
    private void uploadFile() {
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
            //StorageReference fileReference = storageReference.child(System.currentTimeMillis()+ "."+ getFileExtension(uri));
            //storageTask = fileReference.putFile(uri)
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(CustomerBuyProduct.this,"Added to My orders...",Toast.LENGTH_LONG).show();
                                    myOrderModel addProduct = new myOrderModel(productName.getText().toString().trim(),
                                            uri.toString(),
                                            unitPrice.getText().toString().trim(),
                                            quantity.getText().toString().trim(),
                                            totAmount.getText().toString().trim(),
                                            Liter.getText().toString().trim());
                                    String uploadId = databaseReference.push().getKey();
                                    databaseReference.child(uploadId).setValue(addProduct);

                                }


                            });
                            startActivity(new Intent(CustomerBuyProduct.this, MyOrders.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CustomerBuyProduct.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

    }




}