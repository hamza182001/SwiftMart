package com.example.madgrocery_project;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailScreen extends AppCompatActivity {
    ImageView ivImageDetailScreen;
    TextView tvItemName,tvDescription,tvPrice,tvItemCountDetailScreen;
    Button btnAddToFavourites,btnAddToCart;
    ImageButton ibBackDetailScreen,ibDeleteItem,ibAddItem;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_screen);
        init();
        DatabaseReference productsRef;
        ibBackDetailScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(ProductDetailScreen.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        String product = getIntent().getStringExtra("product");
        String category = getIntent().getStringExtra("category");
        if (category != null && !category.isEmpty()) {

            productsRef = FirebaseDatabase.getInstance().getReference().child(category);


        } else {

             productsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        }

        productsRef.orderByChild("name").equalTo(product).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Product product = snapshot.getValue(Product.class);


                        tvItemName.setText(product.getName());
                        tvDescription.setText(snapshot.child("description").getValue(String.class));
                        tvPrice.setText(product.getPrice());
                        tvItemCountDetailScreen.setText(product.getCount());
                         url = product.getUrl();
                        Glide.with(ProductDetailScreen.this)
                                .load(url)
                                .into(ivImageDetailScreen);


                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ibAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(tvItemCountDetailScreen.getText().toString());
                int newCount = currentCount + 1;


                tvItemCountDetailScreen.setText(String.valueOf(newCount));

                updateCountInDatabase(product, String.valueOf(newCount));
            }
        });
        ibDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCount = Integer.parseInt(tvItemCountDetailScreen.getText().toString());
                int newCount = Math.max(0, currentCount - 1);


                tvItemCountDetailScreen.setText(String.valueOf(newCount));


                updateCountInDatabase(product, String.valueOf(newCount));
            }
        });
        btnAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                String userId = sharedPreferences.getString("user_id", "");
                String productName= tvItemName.getText().toString();
                String productDescription= tvDescription.getText().toString();
                String productCount = tvItemCountDetailScreen.getText().toString();
                String productPrice = tvPrice.getText().toString();
                String imageUrl =url ;

                checkItemInFavorites(productName, userId, new OnCheckFavoriteListener() {
                    @Override
                    public void onCheckFavorite(boolean isAlreadyInFavorites) {
                        if (isAlreadyInFavorites) {

                            Toast.makeText(ProductDetailScreen.this, "Item is already in favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean isFavorite = true;
                            updateFavoriteStatusInDatabase(productName, productDescription, isFavorite,imageUrl,productCount,productPrice);
                        }
                    }

                    @Override
                    public void onItemAlreadyInFavorites() {

                        Toast.makeText(ProductDetailScreen.this, "Item is already in favorites", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                String userId = sharedPreferences.getString("user_id", "");
                String productName = tvItemName.getText().toString();
                String productDescription = tvDescription.getText().toString();
                String productCount = tvItemCountDetailScreen.getText().toString();
                String productPrice = tvPrice.getText().toString();
                String imageUrl = url;
                int checkCount= Integer.parseInt(productCount);
                if(checkCount==0) {
                    Toast.makeText(ProductDetailScreen.this, "Increment the Item count to add to cart", Toast.LENGTH_SHORT).show();
                }
                else{

                addToCart(userId, productName, productDescription, imageUrl, productCount, productPrice);
                }
            }
        });


    }
    private void addToCart(String userId, String productName, String productDescription, String imageUrl, String productCount, String productPrice) {
        DatabaseReference userCartRef = FirebaseDatabase.getInstance().getReference().child("users_info").child(userId).child("cart");

        checkIfProductExists(userCartRef, productName, productDescription, new OnProductCheckListener() {
            @Override
            public void onProductCheck(boolean exists, String productId) {
                if (!exists) {
                    addProductToCart(userCartRef, productName, productDescription, imageUrl, productCount, productPrice);
                    Toast.makeText(ProductDetailScreen.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailScreen.this, "Item is already in the cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addProductToCart(DatabaseReference userCartRef, String productName, String productDescription, String imageUrl, String productCount, String productPrice) {
        String uniqueId = userCartRef.push().getKey();
        DatabaseReference productRef = userCartRef.child(uniqueId);

        productRef.child("name").setValue(productName);
        productRef.child("description").setValue(productDescription);
        productRef.child("url").setValue(imageUrl);
        productRef.child("count").setValue(productCount);
        productRef.child("price").setValue(productPrice);
    }
    private void updateCountInDatabase(String productName, String newCount) {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");


        productsRef.orderByChild("name").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        snapshot.getRef().child("count").setValue(newCount);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public interface OnCheckFavoriteListener {
        void onCheckFavorite(boolean isFavorite);
        void onItemAlreadyInFavorites();
    }

    private void checkItemInFavorites(String productName, String userId, OnCheckFavoriteListener callback) {
        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance()
                .getReference().child("users_info").child(userId).child("favorites");

        userFavoritesRef.child(productName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isFavorite = dataSnapshot.exists();
                if (isFavorite) {
                    callback.onItemAlreadyInFavorites();
                } else {
                    callback.onCheckFavorite(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }





    private void updateFavoriteStatusInDatabase(String productName, String productDescription, boolean isFavorite, String imageUrl, String productCount, String productPrice) {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");

        DatabaseReference userFavoritesRef = FirebaseDatabase.getInstance().getReference().child("users_info").child(userId).child("favorites");

        if (isFavorite) {
            checkIfProductExists(userFavoritesRef, productName, productDescription, new OnProductCheckListener() {
                @Override
                public void onProductCheck(boolean exists, String productId) {
                    if (!exists) {
                        addProductToFavorites(userFavoritesRef, productName, productDescription, imageUrl, productCount, productPrice);
                    } else {
                        Toast.makeText(ProductDetailScreen.this, "Item is already in favorites", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // If it's not a favorite, remove the entire product node
            removeProduct(userFavoritesRef, productName, productDescription);
        }
    }

    private void checkIfProductExists(DatabaseReference userFavoritesRef, String productName, String productDescription, OnProductCheckListener callback) {
        userFavoritesRef.orderByChild("name").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Check if the description matches
                        if (snapshot.child("description").getValue(String.class).equals(productDescription)) {
                            callback.onProductCheck(true, snapshot.getKey());
                            return;
                        }
                    }
                }
                callback.onProductCheck(false, null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
                callback.onProductCheck(false, null);
            }
        });
    }

    private void addProductToFavorites(DatabaseReference userFavoritesRef, String productName, String productDescription, String imageUrl, String productCount, String productPrice) {
        String uniqueId = userFavoritesRef.push().getKey();
        DatabaseReference productRef = userFavoritesRef.child(uniqueId);

        productRef.child("name").setValue(productName);
        productRef.child("description").setValue(productDescription);
        productRef.child("url").setValue(imageUrl);  // Save the image URL
        productRef.child("count").setValue(productCount);  // Save the count
        productRef.child("price").setValue(productPrice);  // Save the price
    }

    interface OnProductCheckListener {
        void onProductCheck(boolean exists, String productId);
    }


    private void removeProduct(DatabaseReference userFavoritesRef, String productName, String productDescription) {
        // Query for the specific product by name and description
        userFavoritesRef.orderByChild("name").equalTo(productName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Check if the description matches
                        if (snapshot.child("description").getValue(String.class).equals(productDescription)) {
                            // Remove the entire product node
                            snapshot.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }

    private void init(){
        ivImageDetailScreen=findViewById(R.id.ivImageDetailScreen);
        tvItemName=findViewById(R.id.tvItemName);
        tvDescription=findViewById(R.id.tvDescription);
        tvPrice=findViewById(R.id.tvPrice);
        tvItemCountDetailScreen=findViewById(R.id.tvItemCountDetailScreen);
        btnAddToFavourites=findViewById(R.id.btnAddToFavourites);
        btnAddToCart=findViewById(R.id.btnAddToCart);
        ibBackDetailScreen=findViewById(R.id.ibBackDetailScreen);
        ibDeleteItem=findViewById(R.id.ibDeleteItem);
        ibAddItem=findViewById(R.id.ibAddItem);

    }
}