package com.example.madgrocery_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyAccountFragment extends Fragment {
Button btnLogOut;
TextView tvUsername,tvEmail;
ImageView profileImageView;
LinearLayout llNotification,llDetails,llAddress,llPaymentOption,llAbout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_account, container, false);
        llNotification = v.findViewById(R.id.llOrders);
        llDetails = v.findViewById(R.id.llDetails);
        llAbout = v.findViewById(R.id.llAbout);
        llAddress = v.findViewById(R.id.llAddress);
        llPaymentOption = v.findViewById(R.id.llPaymentOption);
        btnLogOut=v.findViewById(R.id.btnLogOut);
        tvUsername=v.findViewById(R.id.tvUsername);
        tvEmail=v.findViewById(R.id.tvEmail);
        profileImageView=v.findViewById(R.id.profileImageView);
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");
        llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(),NotificationScreen.class));
            }
        });
        llDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), MyDetailsScreen.class));
            }
        });
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), AboutScreen.class));
            }
        });
        llAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), DeliveryAddressScreen.class));
            }
        });
        llPaymentOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), PaymentMethodScreen.class));
            }
        });
        DatabaseReference user_info= FirebaseDatabase.getInstance().getReference().child("Users_info").child(userId);
        user_info.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    String userEmail = dataSnapshot.child("email").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String profilePicUrl = dataSnapshot.child("pic").getValue(String.class);


                    tvEmail.setText("Email: "+userEmail);
                    tvUsername.setText("Username: "+username);
                    Glide.with(requireActivity())
                            .load(profilePicUrl)
                            .placeholder(R.drawable.add_circle) // You can set a placeholder image
                            .error(profileImageView) // You can set an error image
                            .into(profileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user",getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getActivity(),Registration_Screen.class));

            }
        });


        return v;
    }
}