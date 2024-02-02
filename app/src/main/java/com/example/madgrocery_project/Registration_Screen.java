package com.example.madgrocery_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration_Screen extends AppCompatActivity {
        TextInputEditText etUsername,etEmail,etPassword,etPassword2;
        Button btnSignUp;
        TextView btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        init();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = etUsername.getText().toString().trim();
                String Email = etEmail.getText().toString().trim();
                String Password = etPassword.getText().toString().trim();
                String Password2 = etPassword2.getText().toString().trim();
                if (!Password.equals(Password2)) {
                    Toast.makeText(Registration_Screen.this, "Your passwords do not match", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(Email)) {
                    Toast.makeText(Registration_Screen.this, "Email should be of format example@domain.com", Toast.LENGTH_SHORT).show();
                } else {
                    checkUsernameExists(Username,Email,Password);
                }



            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration_Screen.this,Login_Screen.class));
                finish();
            }
        });
    }

    private void init(){
        etUsername=findViewById(R.id.etUsername);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etPassword2=findViewById(R.id.etPassword2);
        btnSignUp=findViewById(R.id.btnSignUp);
        btnLogin=findViewById(R.id.btnLogin);
    }
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.endsWith(".com") &&
                (email.endsWith("@gmail.com") || email.endsWith("@yahoo.com") ||
                        email.endsWith("@hotmail.com") || email.endsWith("@outlook.com") ||
                        email.endsWith("@protonmail.com") ||
                        email.endsWith("@icloud.com"));
    }
    private void checkUsernameExists(final String username,String email,String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_info");

        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Toast.makeText(Registration_Screen.this, "Username already exists, please choose another one", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(Registration_Screen.this, Location_Screen.class);

                    intent.putExtra("Username",username);
                    intent.putExtra("Email",email);
                    intent.putExtra("Password",password);

                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Registration_Screen.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}