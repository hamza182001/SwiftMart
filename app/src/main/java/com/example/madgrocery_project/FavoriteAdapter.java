package com.example.madgrocery_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class FavoriteAdapter extends FirebaseRecyclerAdapter<Favorite,FavoriteAdapter.Viewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FavoriteAdapter(@NonNull FirebaseRecyclerOptions<Favorite> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FavoriteAdapter.Viewholder holder, int position, @NonNull Favorite model) {
        holder.tvFavoritesName.setText(model.getName());
        holder.tvFavoritesDescription.setText(model.getDescription());
        Glide.with(holder.ivFavorites.getContext())
                .load(model.getUrl())
                .into(holder.ivFavorites);
        holder.btnRemoveFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("user_id", "");
                DatabaseReference favouriteRef= FirebaseDatabase.getInstance().getReference().child("users_info")
                        .child(userId).child("favorites");
                favouriteRef.orderByChild("name").equalTo(model.getName())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    snapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });
            }

            });

        holder.btnGoToDetailScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = holder.itemView.getContext();
                String product= model.getName();
                Intent i = new Intent(context,ProductDetailScreen.class);
                i.putExtra("product",product);
                context.startActivity(i);


            }
        });
    }

    @NonNull
    @Override
    public FavoriteAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorites_design,parent,false);
        return new Viewholder(v);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView ivFavorites;
        TextView tvFavoritesName,tvFavoritesDescription;
        Button btnRemoveFavorite;
        ImageButton btnGoToDetailScreen;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ivFavorites=itemView.findViewById(R.id.ivFavorites);
            tvFavoritesName=itemView.findViewById(R.id.tvFavoritesName);
            tvFavoritesDescription=itemView.findViewById(R.id.tvFavoritesDescription);
            btnRemoveFavorite=itemView.findViewById(R.id.btnRemoveFavorite);
            btnGoToDetailScreen=itemView.findViewById(R.id.btnGoToDetailScreen);

        }
    }
}
