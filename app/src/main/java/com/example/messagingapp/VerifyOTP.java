package com.example.messagingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerifyOTP extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private String mAuthCredentials;
    private EditText OTP;
    private Button VerifyOTP;
    private ProgressBar Progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mAuthCredentials = getIntent().getStringExtra("AuthCredentials");

        Progress = findViewById(R.id.progress);
        OTP = findViewById(R.id.otp);
        VerifyOTP = findViewById(R.id.verify_otp);

        VerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = OTP.getText().toString();

                if(TextUtils.isEmpty(otp))
                {
                    OTP.setError("Please enter the otp");
                    OTP.requestFocus();
                    return;
                }
                else{

                    Progress.setVisibility(View.VISIBLE);
                    VerifyOTP.setEnabled(false);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mAuthCredentials,otp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent profileIntent = new Intent(VerifyOTP.this,ProfileSetup.class);
                            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            profileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(profileIntent);
                            finish();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                OTP.setError("There was an error verifying OTP");
                                OTP.requestFocus();
                                Progress.setVisibility(View.INVISIBLE);
                                VerifyOTP.setEnabled(true);
                                return;
                            }
                        }

                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(mCurrentUser != null)
        {
            sendUserToHome();
        }
    }

    public void sendUserToHome()
    {
        Intent homeInteent = new Intent(VerifyOTP.this,MainActivity.class);
        homeInteent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        homeInteent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeInteent);
        finish();
    }

}
