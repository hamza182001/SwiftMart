package com.example.madgrocery_project;

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

public class CartAdapter extends FirebaseRecyclerAdapter<Category,CartAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CartAdapter(@NonNull FirebaseRecyclerOptions<Category> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position, @NonNull Category model) {
        holder.tvItem.setText(model.getName());
        holder.tvPrice.setText(model.getPrice());
        holder.tvItemDescription.setText(model.getDescription());
        holder.tvItemCount.setText(model.getCount());
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
                if (Integer.parseInt(model.getCount()) == 0) {
                    removeItemFromCart(getRef(itemPosition));
                } else {
                    updateCountInDatabase(getRef(itemPosition), model.getCount());
                    notifyItemChanged(itemPosition);

                }
            }
        });
    }
    public String setPrice(String price) {
        String price2;
        if (price.endsWith("Rs")) {
            price2 = price.substring(0, price.length() - 2).trim();
            return price2;
        } else {
            price2=price;
        }
        return price2;
    }
    private void removeItemFromCart(DatabaseReference reference) {
        reference.removeValue(); // Removes the entire item from the cart
    }
    private void updateCountInDatabase(DatabaseReference reference, String newCount) {
        reference.child("count").setValue(newCount);
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_design,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvItem,tvItemCount,tvPrice,tvItemDescription;
        ImageButton ibAdd, ibDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPic=itemView.findViewById(R.id.ivCart);
            tvItem=itemView.findViewById(R.id.tvCartName);
            tvItemCount=itemView.findViewById(R.id.tvCartItemCount);
            tvItemDescription=itemView.findViewById(R.id.tvCartDescription);
            tvPrice=itemView.findViewById(R.id.tvCartPrice);
            ibAdd = itemView.findViewById(R.id.ibCartAdd);
            ibDelete = itemView.findViewById(R.id.ibCartDelete);
        }
    }
}
