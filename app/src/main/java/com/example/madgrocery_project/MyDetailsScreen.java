package com.example.madgrocery_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyDetailsScreen extends AppCompatActivity {
    TextView tvEmail,tvUsername,tvPassword,tvAddress;
    Button editInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details_screen);
        tvEmail=findViewById(R.id.textViewEmail);
        tvUsername=findViewById(R.id.textViewUsername);
        tvPassword=findViewById(R.id.textViewPassword);
        tvAddress=findViewById(R.id.textViewAddress);
        editInfo=findViewById(R.id.btnEditDetails);
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");

        DatabaseReference user_info= FirebaseDatabase.getInstance().getReference().child("Users_info").child(userId);
        user_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String userEmail = dataSnapshot.child("email").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String userPassword = dataSnapshot.child("password").getValue(String.class);
                    String userAddress = dataSnapshot.child("address").getValue(String.class);


                    tvEmail.setText("Email: "+userEmail);
                    tvUsername.setText("Username: "+username);
                    tvAddress.setText("Address: "+userAddress);
                    tvPassword.setText("Password: "+userPassword);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(v,userId);
            }
        });
    }
    public void showEditDialog(View view,String userid) {
        // Create an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Details");

        // Inflate a layout for the AlertDialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_details, null);
        builder.setView(dialogView);

        // Find EditText elements in the dialog layout
        EditText etNewEmail = dialogView.findViewById(R.id.etNewEmail);
        EditText etNewUsername = dialogView.findViewById(R.id.etNewUsername);
        EditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);
        EditText etNewAddress = dialogView.findViewById(R.id.etNewAddress);

        // Set the current details in the EditText elements
        etNewEmail.setText(tvEmail.getText().toString().replace("Email: ", ""));
        etNewUsername.setText(tvUsername.getText().toString().replace("Username: ", ""));
        etNewPassword.setText(tvPassword.getText().toString().replace("Password: ", ""));
        etNewAddress.setText(tvAddress.getText().toString().replace("Address: ", ""));

        // Set positive and negative buttons for the AlertDialog
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Implement the logic to save the edited details to the database
                // Retrieve the edited details from EditText elements
                String newEmail = etNewEmail.getText().toString().trim();
                String newUsername = etNewUsername.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String newAddress = etNewAddress.getText().toString().trim();

                // Update the TextView elements with the new details
                tvEmail.setText("Email: " + newEmail);
                tvUsername.setText("Username: " + newUsername);
                tvAddress.setText("Address: " + newAddress);
                tvPassword.setText("Password: " + newPassword);


                updateDetailsInDatabase(userid, newEmail, newUsername, newPassword, newAddress);
            }
        });
        builder.setNegativeButton("Cancel", null);

        // Show the AlertDialog
        builder.create().show();
    }

    // Method to update the details in the Firebase Realtime Database
    private void updateDetailsInDatabase(String userId, String newEmail, String newUsername, String newPassword, String newAddress) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users_info").child(userId);

        // Update the details in the database
        userRef.child("email").setValue(newEmail);
        userRef.child("username").setValue(newUsername);
        userRef.child("password").setValue(newPassword);
        userRef.child("address").setValue(newAddress);
    }
}