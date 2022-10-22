package com.app.instagramclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.instagramclone.authentications.UsernameandDateofbirth;
import com.app.instagramclone.model.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class EnterOTP extends AppCompatActivity {
    EditText getotp;
    AppCompatButton verifyOTP;
    String phoneNumber,full_name,password, date,username;
    String OTPid;
    FirebaseAuth mAuth;
    ProgressDialog creating ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        getotp = findViewById(R.id.edittext_otp);
        verifyOTP = findViewById(R.id.verifyOTP);
        mAuth = FirebaseAuth.getInstance();
        creating = new ProgressDialog(this);
        creating.setMessage("Creating your Account");

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phone");
        full_name = i.getStringExtra("full_name");
        password = i.getStringExtra("password");
        date = i.getStringExtra("date");
        username = i.getStringExtra("username");
        initiateOTP();

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creating.show();
                if (getotp.getText().toString().isEmpty()){
                    creating.dismiss();
                    Toast.makeText(EnterOTP.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTPid, getotp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void initiateOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        OTPid=s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            int follower =0;
                            int following =0;
                            Register register = new Register(phoneNumber, full_name, password, date, username, id, follower, following);
                            FirebaseDatabase.getInstance().getReference().child("Users").child(id).setValue(register).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    creating.dismiss();
                                    startActivity(new Intent(EnterOTP.this, MainActivity.class));
                                }
                            });
                        } else {
                            creating.dismiss();
                            Toast.makeText(EnterOTP.this, "Try again latter", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}