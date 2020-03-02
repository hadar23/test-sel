package com.example.test_sel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private EditText EDT_phone,EDT_password;

    private Button BTN_sign_up,BTN_sign_in;

    private Button BTN_forgot_password;

    private FirebaseAuth fAuth;
    private ProgressBar ProgressBar;

    private CountryCodePicker codePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EDT_phone=findViewById(R.id.EDT_user_name);
        EDT_password=findViewById(R.id.EDT_password);
        BTN_sign_up=findViewById(R.id.BTN_sign_up);
        BTN_sign_in=findViewById(R.id.BTN_sign_in);
        BTN_forgot_password=findViewById(R.id.BTN_forgot_password);
        codePicker=findViewById(R.id.ccp);


        ProgressBar=findViewById(R.id.pbarLog);
        fAuth=FirebaseAuth.getInstance();


        //sign_up
        BTN_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserSignUpActivity.class));

            }
        });

        //sign_in
        BTN_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //checks

                ProgressBar.setVisibility(View.VISIBLE);

                if(!EDT_phone.getText().toString().isEmpty()&& EDT_phone.getText().toString().length()==10 ){
                    String phonnum="+"+codePicker.getSelectedCountryCode()+EDT_phone.getText().toString();
                    Log.d("tag","phone num is -> "+phonnum);
                    requestOTP(phonnum);
                }else {
                    EDT_phone.setError("phone number is not valid");
                    ProgressBar.setVisibility(View.GONE);
//
                }

//                // autent the user
//                fAuth.signInWithEmailAndPassword("had@gn.com","123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "logges is successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                        } else {
//                            Toast.makeText(LoginActivity.this, "Eror" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            ProgressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });

           }


       });

}

    private void requestOTP(String phonnum) {
        //inside verify we pass the phone num,time in long format, time unit,acitivity we want to callback
        //the time is for the user can resent to otp
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phonnum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, "cannot create account" + e.getMessage(), Toast.LENGTH_SHORT).show();
//
            }
        });
    }
}
