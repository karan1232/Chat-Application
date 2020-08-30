package com.example.messagingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Registration extends AppCompatActivity {

        private EditText mPhoneNumber,mCountryCode;
        Button mGenerate;
        ProgressBar mProgressbar;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        mPhoneNumber = findViewById(R.id.phone_number);
        mCountryCode = findViewById(R.id.country_code);
        mGenerate = findViewById(R.id.generate_otp);
        mProgressbar = findViewById(R.id.progressBar);


        mGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = mCountryCode.getText().toString();
                String phone_number = mPhoneNumber.getText().toString();

                String complete_phoneNumber = "+" + country_code + "" + phone_number;

                if(TextUtils.isEmpty(country_code) || TextUtils.isEmpty(phone_number))
                {
                    mPhoneNumber.setError("Invalid phone number");
                }
                else
                {
                    mProgressbar.setVisibility(View.VISIBLE);
                    mGenerate.setEnabled(false);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            complete_phoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            Registration.this,
                            mCallbacks
                    );
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Intent otpInteent = new Intent(Registration.this,VerifyOTP.class);
                otpInteent.putExtra("AuthCredentials",s);
                startActivity(otpInteent);
                finish();

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mCurrentUser != null)
        {
            Intent homeInteent = new Intent(Registration.this,MainActivity.class);
            homeInteent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeInteent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeInteent);
            finish();

        }
    }


}
