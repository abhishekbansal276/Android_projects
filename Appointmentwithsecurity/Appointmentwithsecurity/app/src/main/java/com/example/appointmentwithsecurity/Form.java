package com.example.appointmentwithsecurity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Random;

public class Form extends AppCompatActivity {
    private Button choose_btn, upload_btn;
    private ImageView imageView;
    private Uri filepath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private EditText name;
    private EditText phone;
    private EditText email;
    private int count=0;
    private String passcode;
    private int rando1;
    private int rando2;
    private int rando3;
    private int rando4;
    private int rando5;
    private int rando6;
    private int rando7;
    private int rando8;

    private String Want_to_meet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        choose_btn = (Button) findViewById(R.id.choose_img);
        upload_btn = (Button) findViewById(R.id.upload_img);
        imageView = (ImageView) findViewById(R.id.image1);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Bundle extra = getIntent().getExtras();
        Want_to_meet = extra.getString("meet");

        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choose_image();
            }
        });

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_image();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Form.this, Home.class));
            }
        });

        Button go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_work();
            }
        });
    }

    public void choose_image(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select image"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void upload_image(){
        if(filepath==null){
            new AlertDialog.Builder(Form.this).setTitle("Please choose your photo").setMessage("").setNeutralButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
        if(filepath!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            Random rand = new Random();
            String str = "abcdefghijklmnopqrstuvwxyz";
            rando1 = rand.nextInt(10);
            rando2 = rand.nextInt(10);
            rando3 = rand.nextInt(10);
            rando4 = rand.nextInt(10);
            rando5 = rand.nextInt(26);
            rando6 = rand.nextInt(26);
            rando7 = rand.nextInt(26);
            rando8 = rand.nextInt(26);

            passcode = String.valueOf(rando1) + str.charAt(rando5) + String.valueOf(rando2) + str.charAt(rando6) + String.valueOf(rando3) + str.charAt(rando7) + String.valueOf(rando4) + str.charAt(rando8);

            String name_of_visitor = passcode+".jpg";
            StorageReference reference = storageReference.child(name_of_visitor);
            reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(Form.this, "Image uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                }
            });
            count++;
        }
    }

    public void finish_work(){
        name = findViewById(R.id.na);
        phone = findViewById(R.id.pn);
        email = findViewById(R.id.ema);

        if(name.getText().toString().matches("")){
            name.setError("Name cannot be empty");
            name.requestFocus();
        }

        if(phone.getText().toString().matches("")){
            phone.setError("Phone number cannot be empty");
            phone.requestFocus();
        }

        if(email.getText().toString().matches("")){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }

        else{
            if(filepath==null){
                new AlertDialog.Builder(Form.this).setTitle("Please upload your photo").setMessage("").setNeutralButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }

            else{
                if(count==0){
                    new AlertDialog.Builder(Form.this).setTitle("Please upload your photo").setMessage("").setNeutralButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }

                else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Visitors").child(String.valueOf(passcode)).child("Name");
                    myRef.setValue(name.getText().toString());

                    myRef = database.getReference("Visitors").child(String.valueOf(passcode)).child("Phone");
                    myRef.setValue(phone.getText().toString());

                    myRef = database.getReference("Visitors").child(String.valueOf(passcode)).child("Want to meet");
                    myRef.setValue(Want_to_meet.toString());

//        Snackbar.make(rootLayout, "Please nothe the code: " + passcode, Snackbar.LENGTH_INDEFINITE).show();
                    new AlertDialog.Builder(Form.this).setTitle("Your appointment booked").setMessage("we will send meeting timings through mail or " +
                            "phone number\nPlease note your 8 digits code\n\n"+passcode).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Form.this, Home.class));
                        }
                    }).show();
                }
            }
        }
    }
}