package com.example.madgrocery_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class CartFragment extends Fragment {

    RecyclerView rvCart;
    CartAdapter adapter;

    Button btnCheckout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_cart, container, false);
        rvCart=v.findViewById(R.id.rvCart);
        SharedPreferences sharedPreferences=requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId=sharedPreferences.getString("user_id","");
        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users_info").child(userId).child("cart"), Category.class)
                        .build();
        adapter = new CartAdapter(options);
        rvCart.setAdapter(adapter);
        btnCheckout=v.findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), CheckoutScreen.class));
            }
        });




        return v;
    }
    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();

        adapter.stopListening();
    }

}