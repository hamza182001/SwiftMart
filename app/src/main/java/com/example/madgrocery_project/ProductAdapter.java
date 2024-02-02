package com.example.madgrocery_project;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product,ProductAdapter.Viewholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull Viewholder holder, int position, @NonNull Product model) {
        holder.tvItem.setText(model.getName());
        holder.tvItemCount.setText(model.getCount());
        holder.tvPrice.setText(model.getPrice());
        Glide.with(holder.itemView.getContext())
                .load(model.getUrl())
                .into(holder.ivPic);
        final int itemPosition=position;
        holder.ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.increaseCount();
                updateCountInDatabase(getRef(itemPosition), model.getCount());
                notifyItemChanged(itemPosition);
            }
        });
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.decreaseCount();
                updateCountInDatabase(getRef(itemPosition), model.getCount());
                notifyItemChanged(itemPosition);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(v.getContext(), ProductDetailScreen.class);
                String ProductName = model.getName();
                i.putExtra("product",ProductName);
                v.getContext().startActivity(i);

            }
        });
    }

    private void updateCountInDatabase(DatabaseReference reference, String newCount) {
        reference.child("count").setValue(newCount);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_design,parent,false);
        return new Viewholder(v);
    }


    class Viewholder extends RecyclerView.ViewHolder{
        ImageView ivPic;
        TextView tvItem,tvItemCount,tvPrice;
        ImageButton ibAdd, ibDelete;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            ivPic=itemView.findViewById(R.id.ivPic);
            tvItem=itemView.findViewById(R.id.tvItem);
            tvItemCount=itemView.findViewById(R.id.tvItemCount);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            ibAdd = itemView.findViewById(R.id.ibAdd);
            ibDelete = itemView.findViewById(R.id.ibDelete);


        }
    }
}
