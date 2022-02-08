package com.example.bloodhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.ComponentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecepientRegistrationActivity extends AppCompatActivity {

    private TextView backButton;
    private CircleImageView profile_image;
    private TextInputEditText registerFullName,registerIdNumber,registerPhoneNumber,registerEmail,registerPassword;
    private Spinner bloodGroupSpinner;
    private Button registerButton;
    private Uri resultUri;
    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepient_registration);

        backButton = findViewById(R.id.backButton);
        profile_image = findViewById(R.id.profile_image);
        registerFullName = findViewById(R.id.register_fullname);
        registerIdNumber = findViewById(R.id.register_ID_number);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerPhoneNumber = findViewById(R.id.register_mobile_number);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        registerButton = findViewById(R.id.register_button);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecepientRegistrationActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,1);
            }
        });




        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = registerEmail.getText().toString().trim();
                final String password = registerPassword.getText().toString().trim();
                final String fullname = registerFullName.getText().toString().trim();
                final String IdNumber = registerIdNumber.getText().toString().trim();
                final String phonenumber = registerPhoneNumber.getText().toString().trim();
                final String bloodgroup = bloodGroupSpinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(email)){
                    registerEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    registerPassword.setError("password is required");
                    return;
                }
                if(TextUtils.isEmpty(fullname)){
                    registerFullName.setError("Name is required");
                    return;
                }
                if(TextUtils.isEmpty(phonenumber)){
                    registerPhoneNumber.setError("Phone number is required");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    registerIdNumber.setError("Id is required");
                    return;
                }
                if(bloodgroup.equals("bloodgroups")){
                    Toast.makeText(RecepientRegistrationActivity.this, "Select your blood group",Toast.LENGTH_SHORT).show();
                    return;
                }

                else{
                    loader.setMessage("Registering...");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RecepientRegistrationActivity.this, "Error"+error,Toast.LENGTH_LONG).show();
                            }
                            else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId);

                                HashMap userInfo = new HashMap();
                                userInfo.put("Id",currentUserId);
                                userInfo.put("name",fullname);
                                userInfo.put("email",email);
                                userInfo.put("idnumber",IdNumber);
                                userInfo.put("phonenumber",phonenumber);
                                userInfo.put("bloodgroup",bloodgroup);
                                userInfo.put("type","recepient");
                                userInfo.put("search","recepient"+bloodgroup);



                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(RecepientRegistrationActivity.this,"Data set successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(RecepientRegistrationActivity.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                                        }
                                        finish();
                                        // loader.dismiss();
                                    }
                                });


                                if(resultUri != null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profile image").child(currentUserId);
                                    Bitmap bitmap = null;


                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    }catch(IOException e){
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20,byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RecepientRegistrationActivity.this,"Image upload failed", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if(taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String imageurl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl",imageurl);
                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(RecepientRegistrationActivity.this,"Image url added", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    Toast.makeText(RecepientRegistrationActivity.this, task.getException().toString(),Toast.LENGTH_LONG).show();
                                                                }
                                                            }
                                                        });


                                                        finish();

                                                    }


                                                });
                                            }
                                        }
                                    });

                                    Intent i = new Intent(RecepientRegistrationActivity.this,MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    loader.dismiss();
                                }


                            }
                        }
                    });
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            resultUri = data.getData();
            profile_image.setImageURI(resultUri);
        }
    }


}