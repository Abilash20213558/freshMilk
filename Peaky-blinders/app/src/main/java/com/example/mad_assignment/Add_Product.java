package com.example.mad_assignment;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Add_Product extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText productName,unitPrice,liter,description;
    private ImageView productImage;
    private Button addProduct;

    private Uri mImageUri;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    StorageTask storageTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productName = findViewById(R.id.et_productName);
        unitPrice = findViewById(R.id.et_unitPrice);
        liter = findViewById(R.id.et_liter);
        description = findViewById(R.id.et_description);
        productImage = findViewById(R.id.productImage);
        addProduct = findViewById(R.id.btn_addProduct);

        storageReference = FirebaseStorage.getInstance().getReference("admin_products");
        databaseReference = FirebaseDatabase.getInstance().getReference("admin_products");


        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(storageTask != null && storageTask.isInProgress()){
                Toast.makeText(Add_Product.this,"Upload In Progress",Toast.LENGTH_SHORT).show();
            }
                else {
                uploadFile();
            }

            }
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    //You are provided with uri of the image . Take this uri and assign it to Picasso
                    //Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    mImageUri = uri.normalizeScheme();
                    Picasso.get().load(mImageUri).fit().into(productImage);
                }
            });

    //Get the file extension of Image
    private  String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

  private void uploadFile(){
      if(mImageUri != null){
          StorageReference fileReference = storageReference.child(System.currentTimeMillis()+ "."+ getFileExtension(mImageUri));
          storageTask = fileReference.putFile(mImageUri)
                  .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                          firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                              @Override
                              public void onSuccess(Uri uri) {
                                  Toast.makeText(Add_Product.this,"Product Added Successfully",Toast.LENGTH_LONG).show();
                                  AdminProductModel addProduct = new AdminProductModel(productName.getText().toString().trim(),
                                          uri.toString(),
                                          unitPrice.getText().toString().trim(),
                                          description.getText().toString().trim(),
                                          liter.getText().toString().trim());
                                  String uploadId = databaseReference.push().getKey();
                                  databaseReference.child(uploadId).setValue(addProduct);

                              }


                          });
                          startActivity(new Intent(Add_Product.this, Admin_CustomerProductList.class));
                          finish();
                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(Add_Product.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                      }
                  });
      }else{
          Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
      }
  }



}