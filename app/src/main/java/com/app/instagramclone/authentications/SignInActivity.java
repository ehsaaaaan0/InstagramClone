package com.app.instagramclone.authentications;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.instagramclone.R;
import com.app.instagramclone.registerfragments.LoginWithEmail;
import com.app.instagramclone.registerfragments.LoginWithPhone;

public class SignInActivity extends AppCompatActivity {
    AppCompatButton phone, email;
    View phonev, emailv;
    TextView signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        phone = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.emailAddress);
        phonev = findViewById(R.id.phonenumberview);
        emailv = findViewById(R.id.emailView);
        signup = findViewById(R.id.login_togo);
        LoginWithEmail loginWithEmail = new LoginWithEmail();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHere, loginWithEmail).commit();
        emailv.setBackgroundColor(Color.parseColor("#000000"));
        phonev.setBackgroundColor(Color.parseColor("#DCDCDC"));

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonev.setBackgroundColor(Color.parseColor("#000000"));
                emailv.setBackgroundColor(Color.parseColor("#DCDCDC"));
                LoginWithPhone loginWithPhone = new LoginWithPhone();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHere, loginWithPhone).commit();
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailv.setBackgroundColor(Color.parseColor("#000000"));
                phonev.setBackgroundColor(Color.parseColor("#DCDCDC"));
                LoginWithEmail loginWithEmail = new LoginWithEmail();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayoutHere, loginWithEmail).commit();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, LoginActivity.class));
            }
        });
    }
}