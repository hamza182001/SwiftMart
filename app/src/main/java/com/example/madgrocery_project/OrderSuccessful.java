package com.example.madgrocery_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderSuccessful extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_successful);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");
        NotificationModel notification = new NotificationModel("Your order has been placed successfully!");
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference()
                .child("users_info")
                .child(userId)
                .child("notifications");
        String notificationId = notificationsRef.push().getKey();
        notificationsRef.child(notificationId).setValue(notification);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDeliveryNotification("Your order has been delivered successfully!",userId);
                startActivity(new Intent(OrderSuccessful.this, MainActivity.class));
                Toast.makeText(OrderSuccessful.this, "Please select a delivery option", Toast.LENGTH_SHORT).show();
            }
        },5000);

    }
    private void showDeliveryNotification(String message,String userId) {

        NotificationModel notification = new NotificationModel(message);


        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference()
                .child("users_info")
                .child(userId)
                .child("notifications");

        String notificationId = notificationsRef.push().getKey();
        notificationsRef.child(notificationId).setValue(notification);
    }
}