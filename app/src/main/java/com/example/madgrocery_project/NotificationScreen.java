package com.example.madgrocery_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationScreen extends AppCompatActivity {

    RecyclerView recyclerViewNotifications;
    NotificationAdapter notificationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_screen);
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPreferences=getSharedPreferences("user",MODE_PRIVATE);
        String userId=sharedPreferences.getString("user_id","");

        FirebaseRecyclerOptions<NotificationModel> options =
                new FirebaseRecyclerOptions.Builder<NotificationModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users_info").child(userId).child("notifications"), NotificationModel.class)
                        .build();


        notificationAdapter = new NotificationAdapter(options);
        recyclerViewNotifications.setAdapter(notificationAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notificationAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationAdapter.stopListening();
    }

}