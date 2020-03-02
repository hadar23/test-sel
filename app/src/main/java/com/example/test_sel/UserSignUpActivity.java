package com.example.test_sel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class UserSignUpActivity extends AppCompatActivity {
    private  EditText EDT_full_nameET,EDT_identity_numberET,EDT_phone_numberET;
    private  EditText EDT_email,EDT_academy,EDT_start_year,EDT_engineering;

    private Button BTNsign_up;
    private TextView TXT_quest;

    private Firebase mref;
    private boolean b=true;
    private String userId;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private ProgressBar ProgressBar;

    String fullName,identity_num,phone,email,academy,start_year,engineering;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);

        EDT_full_nameET=findViewById(R.id.EDT_full_nameET);
        EDT_identity_numberET=findViewById(R.id.EDT_identity_numberET);
        EDT_phone_numberET=findViewById(R.id.EDT_phone_numberET);
        EDT_email=findViewById(R.id.EDT_email);
        EDT_academy=findViewById(R.id.EDT_academy);
        EDT_start_year=findViewById(R.id.EDT_start_year);
        EDT_engineering=findViewById(R.id.EDT_engineering);

        TXT_quest=findViewById(R.id.TXT_quest);

        BTNsign_up=findViewById(R.id.BTNsign_up);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        ProgressBar=findViewById(R.id.pbarReg);




        final EditText [] editArr={EDT_full_nameET,EDT_identity_numberET,EDT_phone_numberET,EDT_email,EDT_academy,EDT_start_year,EDT_engineering};

        //if user already login
        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            finish();
        }
        //if user press on the text that he has already connect
        TXT_quest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
        //if user want to sign up
        BTNsign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for(EditText ed : editArr) {
//                    while (!isEDempty(ed));
//                }

                // for now
                email="hjhjjh@gmail.com";

                    fullName = EDT_full_nameET.getText().toString();
                    identity_num = EDT_identity_numberET.getText().toString().trim();
                    phone = EDT_phone_numberET.getText().toString();
                    email = EDT_email.getText().toString().trim();
                    academy = EDT_academy.getText().toString().trim();
                    start_year = EDT_start_year.getText().toString().trim();
                    engineering = EDT_engineering.getText().toString().trim();

//pbar should be after all the check
                    ProgressBar.setVisibility(View.VISIBLE);


                    fAuth.createUserWithEmailAndPassword(email, "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserSignUpActivity.this, "user creat", Toast.LENGTH_SHORT).show();
                                userId = fAuth.getCurrentUser().getUid();

                                //add colection named user and creaet new doucument by user id
                                DocumentReference documentRef = fStore.collection("users").document(userId);
                                Map<String, Object> user = new HashMap<>();
                                user = addMapData(fullName, phone, identity_num, email, academy, start_year, engineering);

                                // insert to the cloude
                                documentRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "user profile is create for " + userId);
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            } else {
                                Toast.makeText(UserSignUpActivity.this, "Eror" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                ProgressBar.setVisibility(View.GONE);
                            }

                        }
                    });


                }
//                for(EditText ed : editArr){
//                    isEDempty(ed);
//                }
//                    if(isEDempty(ed))
//                        b=false;
//                }
//                if (b){
//                    User user1= new User("hadar","312323232","9756453232","emailgmailcom","12345","12345","12345");
//                    MyFireBase.setUser(user1);
//                }




        });
    }


    public Map addMapData(String fullName,String phone,String identity_num,String email,String academy,String start_year,String engineering){
        Map<String,Object> user=new HashMap<>();
        user.put("fName",fullName);
        user.put("phone",phone);
        user.put("identity",identity_num);
        user.put("email",email);
        user.put("academy",academy);
        user.put("start_year",start_year);
        user.put("engineering",engineering);
        return user;
    }

    public boolean isEDempty(EditText etText) {
        if(etText.getText().toString().trim().length() == 0) {
            etText.setError("this box is empty");
            return true;
        }
        return false;
    }







}
