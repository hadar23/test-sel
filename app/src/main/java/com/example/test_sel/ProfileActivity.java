package com.example.test_sel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {

    private TextView TXT_full_name,TXT_identity_number,TXT_phone_number;
    private  TextView TXT_email,TXT_academy,TXT_start_year,TXT_engineering;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TXT_full_name=findViewById(R.id.TXT_full_name);
        TXT_identity_number=findViewById(R.id.TXT_identity_number);
        TXT_phone_number=findViewById(R.id.TXT_phone_number);
        TXT_email=findViewById(R.id.TXT_email);
        TXT_academy=findViewById(R.id.TXT_academy);
        TXT_start_year=findViewById(R.id.TXT_start_year);
        TXT_engineering=findViewById(R.id.TXT_engineering);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        userId=fAuth.getCurrentUser().getUid();

        final DocumentReference documentRef=fStore.collection("users").document(userId);
        documentRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                TXT_full_name.setText(documentSnapshot.getString("fName"));
                TXT_identity_number.setText(documentSnapshot.getString("identity"));
                TXT_phone_number.setText(documentSnapshot.getString("phone"));
                TXT_email.setText(documentSnapshot.getString("email"));
                TXT_academy.setText(documentSnapshot.getString("academy"));
                TXT_start_year.setText(documentSnapshot.getString("start_year"));
                TXT_engineering.setText(documentSnapshot.getString("engineering "));

            }
        });
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();

    }
}
