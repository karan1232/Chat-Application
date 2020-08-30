package com.example.messagingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetup extends AppCompatActivity {

    private ImageButton ProfileImage;
    private Button CompleteProfile;
    private EditText about;

    private static final int IMAGE_PICKER = 1000;
    private static final int PERMISION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        ProfileImage = findViewById(R.id.profileImage);
        CompleteProfile = findViewById(R.id.completeProfile);
        about = findViewById(R.id.aboutme);

        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions,PERMISION_CODE);
                    }
                    else {
                            pickImageFromGallery();
                    }
                }
                else{
                    pickImageFromGallery();
                }
            }
        });

        CompleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sAbout = about.getText().toString();

                if(TextUtils.isEmpty(sAbout))
                {
                    about.setError("Please write something about yourself");
                    about.requestFocus();
                }

                Intent mainActivity = new Intent(ProfileSetup.this,MainActivity.class);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
                finish();
            }
        });
    }

    private void pickImageFromGallery()
    {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery,IMAGE_PICKER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISION_CODE:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    pickImageFromGallery();
                }
                else{
                    Toast.makeText(this,"Please grant storage Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && resultCode == IMAGE_PICKER) {
            ProfileImage.setImageURI(data.getData());
        }
    }

}
