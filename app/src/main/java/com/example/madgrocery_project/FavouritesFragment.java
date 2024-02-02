package com.example.madgrocery_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class FavouritesFragment extends Fragment {
    RecyclerView rvFavourites;
    Button btnAddAllToCart;
    FavoriteAdapter favoriteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_favourites, container, false);
        rvFavourites=v.findViewById(R.id.rvFavourites);
        btnAddAllToCart=v.findViewById(R.id.btnAddAllToCart);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("user_id", "");
        rvFavourites.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<Favorite> options =
                new FirebaseRecyclerOptions.Builder<Favorite>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users_info").child(userId).child("favorites"), Favorite.class)
                        .build();


        favoriteAdapter = new FavoriteAdapter(options);
        rvFavourites.setAdapter(favoriteAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Start listening for changes in the Firebase Realtime Database
        favoriteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Stop listening for changes in the Firebase Realtime Database
        favoriteAdapter.stopListening();
    }



}