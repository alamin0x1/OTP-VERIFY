package com.example.otpverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendotpActivity extends AppCompatActivity {

    private EditText inputMobile;
    private Button buttongetOTP ;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendotp);

        inputMobile = findViewById(R.id.inputMobile);
        buttongetOTP = findViewById(R.id.buttonIdOTP);

        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        buttongetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputMobile.getText().toString().isEmpty()){
                    Toast.makeText(SendotpActivity.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                buttongetOTP.setVisibility(View.INVISIBLE);


                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+88" + inputMobile.getText().toString(),        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        SendotpActivity.this,               // Activity (for callback binding)
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                progressBar.setVisibility(View.GONE);
                                buttongetOTP.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                buttongetOTP.setVisibility(View.VISIBLE);
                                Toast.makeText(SendotpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                progressBar.setVisibility(View.GONE);
                                buttongetOTP.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(),VerifyActivity.class);
                                intent.putExtra("mobile",inputMobile.getText().toString());
                                intent.putExtra("verificationId", verificationId);
                                startActivity(intent);
                            }
                        });

            }
        });
    }
}