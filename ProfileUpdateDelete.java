package com.example.mad_assignment;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileUpdateDelete extends AppCompatActivity {
    EditText etName,etEmail,etPassword,etConfirmPwd;
    Button btnUpdate,btnDelete;
    ImageView imgUserPic;
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    FirebaseUser fuser;
    StorageReference storageReference;
    String userId;
    private Uri mImageUri;
    StorageTask storageTask;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update_delete);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_emailId);
        etPassword = findViewById(R.id.et_password);
        etConfirmPwd = findViewById(R.id.et_confirmPassword);
        imgUserPic = findViewById(R.id.imgUserProfile);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        user = fauth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("user_profile");
        //getCurrent userId for Read Data
        userId = fauth.getCurrentUser().getUid();


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFunction();

            }
        });

        //Choose Image
        imgUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");

            }
        });

        //get the image from firebase
        StorageReference profileRef = storageReference.child(fauth.getCurrentUser().getUid() + "/profile_image.png");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgUserPic);
            }
        });

        //Read Data
        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                etName.setText(documentSnapshot.getString("Name"));
                etEmail.setText(documentSnapshot.getString("Email"));
                etPassword.setText(documentSnapshot.getString("Password"));
                etConfirmPwd.setText(documentSnapshot.getString("ConfirmPassword"));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //update Password with auhtentication
                final String password = etPassword.getText().toString();
                user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference documentRe = fstore.collection("users").document(user.getUid());

                        //update all fields
                        Map<String, Object> edited = new HashMap<>();

                        edited.put("Name", etName.getText().toString());
                        edited.put("Email", etEmail.getText().toString());
                        edited.put("Password", etPassword.getText().toString());
                        edited.put("ConfirmPassword", etConfirmPwd.getText().toString());

                        documentRe.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileUpdateDelete.this, "Profile updated...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), CustomerDashboard.class));
                                finish();
                            }
                        });
                        Toast.makeText(ProfileUpdateDelete.this, "Password is changed", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileUpdateDelete.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }



    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    //You are provided with uri of the image . Take this uri and assign it to Picasso
                    //Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //mImageUri = uri.normalizeScheme();
                    Picasso.get().load(uri).fit().into(imgUserPic);
                    uploadImageToFirebase(uri);

                }
            });


    //upload the image to firebase
    private void uploadImageToFirebase(Uri uri) {

        // upload image to firebase storage
        StorageReference fileRef = storageReference.child(fauth.getCurrentUser().getUid() + "/profile_image.png");
        storageTask = fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Picasso.get().load(mImageUri).into(imgUserPic);
                        Toast.makeText(ProfileUpdateDelete.this,"Image uploaded",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteFunction() {
        String currentImageURL = storageReference.child(fauth.getCurrentUser().getUid() + "/profile_image.png").toString();;

        ShowDeleteDialog(currentImageURL);
    }
    private void ShowDeleteDialog(String currentImageURL) {
        userID = fauth.getCurrentUser().getUid();
        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileUpdateDelete.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete this user?");
        //Yes Button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Query mquery = databaseReference.orderByChild("prodName").equalTo(currentProductName);
                fstore.collection("users").document(userID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                     fauth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             Toast.makeText(ProfileUpdateDelete.this,"User Deleted...",Toast.LENGTH_LONG).show();
                             //Delete Image From Storage
                             StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(currentImageURL);
                             mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                 @Override
                                 public void onSuccess(Void unused) {
                                     //Toast.makeText(AdminProductUpdate.this,"Image Deleted",Toast.LENGTH_SHORT).show();
                                     startActivity(new Intent(ProfileUpdateDelete.this, CustomerDashboard.class));
                                     finish();
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull  Exception e) {
                                     Toast.makeText(ProfileUpdateDelete.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                 }
                             });

                         }


                     });
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
}
