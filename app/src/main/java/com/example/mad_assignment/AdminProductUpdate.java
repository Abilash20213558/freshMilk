package com.example.mad_assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminProductUpdate extends AppCompatActivity {
    TextView productName, Liter, unitPrice, description;
    ImageView imageView;
    Button update, delete;
    String getprodName, getunitPrice, getLiter, getImage, getDescription;

    StorageReference storageReference;
    DatabaseReference databaseReference;
    private Uri mImageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_update);

        productName = findViewById(R.id.et_prodName);
        unitPrice = findViewById(R.id.et_unitPrice);
        Liter = findViewById(R.id.et_liter);
        description = findViewById(R.id.et_description);
        imageView = findViewById(R.id.productImage);
        update = findViewById(R.id.btn_update);
        delete = findViewById(R.id.btn_delete);

        storageReference = FirebaseStorage.getInstance().getReference("admin_products");
        databaseReference = FirebaseDatabase.getInstance().getReference("admin_products");

        Intent intent = getIntent();
        getprodName = intent.getStringExtra("productName");
        getunitPrice = intent.getStringExtra("unitPrice");
        getLiter = intent.getStringExtra("Liter");
        getDescription = intent.getStringExtra("Description");
        getImage = intent.getStringExtra("image_url");


        productName.setText(getprodName);
        unitPrice.setText(getunitPrice);
        Liter.setText(getLiter);
        description.setText(getDescription);
        Picasso
                .get()
                .load(getImage)
                .fit()
                .into(imageView);


        //Choose Image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mGetContent.launch("image/*");
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                beginUpdate();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFunction();

            }
        });


    }

    private void deleteFunction() {
        String currentProductName = getprodName;
        String currentImageURL = getImage;

        ShowDeleteDialog(currentProductName,currentImageURL);

    }

    private void ShowDeleteDialog(String currentProductName, String currentImageURL) {
        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminProductUpdate.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete this product?");
        //Yes Button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Query mquery = databaseReference.orderByChild("prodName").equalTo(currentProductName);
                mquery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(AdminProductUpdate.this,"Product Deleted...",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminProductUpdate.this, Admin_CustomerProductList.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {
                        Toast.makeText(AdminProductUpdate.this,error.getMessage(),Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AdminProductUpdate.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    //You are provided with uri of the image . Take this uri and assign it to Picasso
                    //Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    mImageUri = uri.normalizeScheme();
                    Picasso.get().load(mImageUri).fit().into(imageView);
                }
            });

    private void beginUpdate() {
        deletePreviousImage();

    }

    private void deletePreviousImage() {
        StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(getImage);
        mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //deleted
                uploadNewImage();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminProductUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadNewImage() {
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
                Toast.makeText(AdminProductUpdate.this, "New Image Uploaded...", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(AdminProductUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDatabase(String s) {
        String PrName = productName.getText().toString();
        String PrUnitPrice = unitPrice.getText().toString();
        String prLiter = Liter.getText().toString();
        String prDes = description.getText().toString();

        Query query = databaseReference.orderByChild("prodName").equalTo(getprodName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //Update Data
                    ds.getRef().child("prodName").setValue(PrName);
                    ds.getRef().child("unitPrice").setValue(PrUnitPrice);
                    ds.getRef().child("liter").setValue(prLiter);
                    ds.getRef().child("description").setValue(prDes);
                    ds.getRef().child("imageURL").setValue(s);

                }
                Toast.makeText(AdminProductUpdate.this, "Updating Successful..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AdminProductUpdate.this, Admin_CustomerProductList.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}