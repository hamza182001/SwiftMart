package com.example.madgrocery_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Screen extends AppCompatActivity {
    public static final String PREF_AUTHENTICATED = "authenticated";
    public static final String PREF_USER_ID = "user_id";
    private static final String PREF_ADDRESS = "address";
    TextInputEditText etEmailLog,etPasswordLog;
    TextView tvSignUp;
    Button btnLoginLog;
    ImageButton ibBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        init();
        ibBack.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          startActivity(new Intent(Login_Screen.this,Registration_Screen.class));
                                          finish();
                                      }
                                  }
        );
        tvSignUp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(Login_Screen.this,Registration_Screen.class));
                                            finish();
                                        }
                                    }
        );
        btnLoginLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = etEmailLog.getText().toString().trim();
                String Password = etPasswordLog.getText().toString().trim();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_info");

                databaseReference.orderByChild("email").equalTo(Email.trim()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {


                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                String storedPassword = userSnapshot.child("password").getValue(String.class);

                                if (storedPassword!=null  && Password.equals(storedPassword)) {
                                    saveUserId(userSnapshot.getKey());

                                    saveAuthenticationStatus(true);

                                    saveUserAddress(userSnapshot.child("address").getValue(String.class));
                                    Intent intent = new Intent(Login_Screen.this, MainActivity.class);
                                    intent.putExtra("address", userSnapshot.child("address").getValue(String.class));
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Toast.makeText(Login_Screen.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {

                            Toast.makeText(Login_Screen.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(Login_Screen.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }


        });
    }
    private void init(){

        etEmailLog=findViewById(R.id.etEmailLog);
        etPasswordLog=findViewById(R.id.etPasswordLog);
        tvSignUp=findViewById(R.id.tvSignUpLog);
        btnLoginLog=findViewById(R.id.btnLoginLog);
        ibBack=findViewById(R.id.ibBack);

    }
    private void saveAuthenticationStatus(boolean authenticated) {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_AUTHENTICATED, authenticated);
        editor.apply();
    }


    private void saveUserAddress(String address) {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_ADDRESS, address);
        editor.apply();
    }
    private void saveUserId(String id){
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_USER_ID, id);
        editor.apply();
    }
}