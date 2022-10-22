package com.app.instagramclone.authentications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.instagramclone.EditProfile;
import com.app.instagramclone.EnterOTP;
import com.app.instagramclone.MainActivity;
import com.app.instagramclone.R;
import com.app.instagramclone.model.Register;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UsernameandDateofbirth extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    AppCompatButton submit_final;
    String email_phone, password, full_name, register;
    EditText  user_name;
    TextView dob;
    String verificationID;
    ProgressDialog progressDialog;

    //date picker
    private CheckBox modeDarkDate;
    private CheckBox modeCustomAccentDate;
    private CheckBox vibrateDate;
    private CheckBox dismissDate;
    private CheckBox titleDate;
    private CheckBox showYearFirst;
    private CheckBox showVersion2;
    private CheckBox switchOrientation;
    private CheckBox limitSelectableDays;
    private CheckBox highlightDays;
    private CheckBox defaultSelection;
    private DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usernameand_dateofbirth);
        submit_final = findViewById(R.id.submit_final);
        dob = findViewById(R.id.dob);
        user_name = findViewById(R.id.user_name);

        //check box extra layouts ingnor
        modeDarkDate = findViewById(R.id.mode_dark_date);
        modeCustomAccentDate = findViewById(R.id.mode_custom_accent_date);
        vibrateDate = findViewById(R.id.vibrate_date);
        dismissDate = findViewById(R.id.dismiss_date);
        titleDate = findViewById(R.id.title_date);
        showYearFirst = findViewById(R.id.show_year_first);
        showVersion2 = findViewById(R.id.show_version_2);
        switchOrientation = findViewById(R.id.switch_orientation);
        limitSelectableDays = findViewById(R.id.limit_dates);
        highlightDays = findViewById(R.id.highlight_dates);
        defaultSelection = findViewById(R.id.default_selection);
        //ignore section ends here.

         progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Account....");


        Intent in = getIntent();
        email_phone = in.getStringExtra("email_phone");
        full_name = in.getStringExtra("Full_Name");
        password = in.getStringExtra("password");
        register = in.getStringExtra("register");



        dob.setOnClickListener(v -> {
           picdate();
        });




        submit_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressDialog.show();
                String date = dob.getText().toString().trim();
                String user = user_name.getText().toString().trim();
                if (TextUtils.isEmpty(user)){
                    progressDialog.dismiss();
                    Toast.makeText(UsernameandDateofbirth.this, "Please Enter User name", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Objects.equals(register, "email")) {


                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email_phone, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {


                                SharedPreferences sharedPreferences = getSharedPreferences("register", Context.MODE_PRIVATE);
                                sharedPreferences.edit().putString("user_name", user)
                                        .putString("email_phone", email_phone).putString("dob", date).putString("full_name", full_name).apply();

                                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                int follower = 0;
                                int following = 0;
                                Register register = new Register(email_phone, full_name, password, date, user, id, follower, following);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(id).setValue(register).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressDialog.dismiss();
                                        startActivity(new Intent(UsernameandDateofbirth.this, MainActivity.class));
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(UsernameandDateofbirth.this, "Unable to create account please try again latter", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        Intent i = new Intent(UsernameandDateofbirth.this, EnterOTP.class);
                        i.putExtra("phone", email_phone);
                        i.putExtra("full_name", full_name);
                        i.putExtra("password", password);
                        i.putExtra("date", date);
                        i.putExtra("username", user);
                        startActivity(i);
                    }
                }

            }
        });
    }


    private void picdate() {
        Calendar now = Calendar.getInstance();
        if (defaultSelection.isChecked()) {
            now.add(Calendar.DATE, 7);
        }
            /*
            It is recommended to always create a new instance whenever you need to show a Dialog.
            The sample app is reusing them because it is useful when looking for regressions
            during testing
             */
        if (dpd == null) {
            dpd = DatePickerDialog.newInstance(
                    UsernameandDateofbirth.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {
            dpd.initialize(
                    UsernameandDateofbirth.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        }

        dpd.setThemeDark(modeDarkDate.isChecked());
        dpd.vibrate(vibrateDate.isChecked());
        dpd.dismissOnPause(dismissDate.isChecked());
        dpd.showYearPickerFirst(showYearFirst.isChecked());
        dpd.setVersion(showVersion2.isChecked() ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);
        if (modeCustomAccentDate.isChecked()) {
            dpd.setAccentColor(Color.parseColor("#9C27B0"));
        }
        if (titleDate.isChecked()) {
            dpd.setTitle("DatePicker Title");
        }
        if (highlightDays.isChecked()) {
            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();
            date2.add(Calendar.WEEK_OF_MONTH, -1);
            Calendar date3 = Calendar.getInstance();
            date3.add(Calendar.WEEK_OF_MONTH, 1);
            Calendar[] days = {date1, date2, date3};
            dpd.setHighlightedDays(days);
        }
        if (limitSelectableDays.isChecked()) {
            Calendar[] days = new Calendar[13];
            for (int i = -6; i < 7; i++) {
                Calendar day = Calendar.getInstance();
                day.add(Calendar.DAY_OF_MONTH, i * 2);
                days[i + 6] = day;
            }
            dpd.setSelectableDays(days);
        }
        if (switchOrientation.isChecked()) {
            if (dpd.getVersion() == DatePickerDialog.Version.VERSION_1) {
                dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.HORIZONTAL);
            } else {
                dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.VERTICAL);
            }
        }

        dpd.setOnCancelListener(dialog -> {
            Log.d("DatePickerDialog", "Dialog was cancelled");
            dpd = null;
        });
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dpd = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date =+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        dob.setText(date);
        dpd = null;
    }


//    private void getOtp() {
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
//                        .setPhoneNumber("+92"+email_phone)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(UsernameandDateofbirth.this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//    }
//
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
//    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//            final String code = credential.getSmsCode();
//            if (code!=null){
//                verifyCode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(UsernameandDateofbirth.this, "Try Again", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String verificationId,
//                @NonNull PhoneAuthProvider.ForceResendingToken token) {
//            super.onCodeSent(verificationId, token);
//            verificationID = verificationId;
//        }
//    };
//
//    private void verifyCode(String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
//        signinByCredential(credential);
//    }
//
//    private void signinByCredential(PhoneAuthCredential credential) {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(UsernameandDateofbirth.this, "Account Registered", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}