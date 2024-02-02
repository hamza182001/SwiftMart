package com.example.madgrocery_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckoutScreen extends AppCompatActivity {

    Button btnPlaceOrder;
    TextView tvCheckoutPrice;
    TextView tvCheckoutAddress;
    ImageButton ibBack;
    int total=0;
    String totalString;
    ArrayList<String> orderedItemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_screen);
        btnPlaceOrder=findViewById(R.id.btnPlaceOrder);
        tvCheckoutPrice=findViewById(R.id.tvCheckoutPrice);
        tvCheckoutAddress=findViewById(R.id.tvCheckoutAddress);
        ibBack=findViewById(R.id.ibBackCheckout);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");


        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference().child("users_info").child(userId).child("cart");


        userCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalItemCount = 0;
                int totalPrice = 0;

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                    String priceString = itemSnapshot.child("price").getValue(String.class);
                    String countString = itemSnapshot.child("count").getValue(String.class);
                    int count = Integer.parseInt(countString);
                    totalItemCount+=count;

                    String cleanedPriceString = priceString.replaceAll("[^\\d]", "");
                    int price = Integer.parseInt(cleanedPriceString);
                    totalPrice += (price*count);


                }
                total =totalPrice;
                 totalString= String.valueOf(total);
                tvCheckoutPrice.setText(totalString+"Rs");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference usersInfoRef = FirebaseDatabase.getInstance().getReference().child("Users_info").child(userId);

        usersInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String address = dataSnapshot.child("address").getValue(String.class);


                    tvCheckoutAddress.setText(address);
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    orderedItemsList = new ArrayList<>();

                    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference()
                            .child("users_info")
                            .child(userId)
                            .child("cart");

                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                                    .child("users_info")
                                    .child(userId)
                                    .child("orders");

                            ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long lastOrderId = snapshot.getChildrenCount() + 1;

                                    DatabaseReference newOrderRef = ordersRef.child(String.valueOf(lastOrderId));

                                    for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                                        String itemName = itemSnapshot.child("name").getValue(String.class);
                                        String itemKey = newOrderRef.child("items").push().getKey();
                                        newOrderRef.child("items").child(itemKey).setValue(itemName);
                                    }

                                    newOrderRef.child("price").setValue(totalString);
                                    // Move cart removal here, after the order is successfully placed
                                    cartRef.removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error == null) {

                                                startActivity(new Intent(CheckoutScreen.this, OrderSuccessful.class));
                                                finish();
                                            } else {

                                                Toast.makeText(CheckoutScreen.this, "Failed to remove items from cart", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle order placement error
                                    Toast.makeText(CheckoutScreen.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle cart data retrieval error
                            Toast.makeText(CheckoutScreen.this, "Failed to retrieve cart items", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(CheckoutScreen.this, "Please select a delivery option", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}