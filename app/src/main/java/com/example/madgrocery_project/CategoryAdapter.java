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

public class CategoryAdapter extends FirebaseRecyclerAdapter<Category,CategoryAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private String category;
    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<Category> options,String category) {
        super(options);
        this.category=category;
    }
    public CategoryAdapter(@NonNull FirebaseRecyclerOptions<Category> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position, @NonNull Category model) {
            holder.tvItem.setText(model.getName());
            holder.tvPrice.setText(model.getPrice());
            holder.tvItemDescription.setText(model.getDescription());
            holder.tvItemCount.setText(model.getCount());
            Glide.with(holder.itemView.getContext())
                .load(model.getUrl())
                .into(holder.ivPic);
            final int itemPosition = position;
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
        holder.ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ProductDetailScreen.class);


                intent.putExtra("product", model.getName());
                intent.putExtra("category", category);

                holder.itemView.getContext().startActivity(intent);
            }
        });

    }



    private void updateCountInDatabase(DatabaseReference reference, String newCount) {
        reference.child("count").setValue(newCount);
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_design,parent,false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPic;
        TextView tvItem,tvItemCount,tvPrice,tvItemDescription;
        ImageButton ibAdd, ibDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPic=itemView.findViewById(R.id.ivCategoryPic);
            tvItem=itemView.findViewById(R.id.tvCategoryItemName);
            tvItemCount=itemView.findViewById(R.id.tvCategoryItemCount);
            tvItemDescription=itemView.findViewById(R.id.tvCategoryItemDescription);
            tvPrice=itemView.findViewById(R.id.tvCategoryPrice);
            ibAdd = itemView.findViewById(R.id.ibCategoryAdd);
            ibDelete = itemView.findViewById(R.id.ibCategoryDelete);

        }
    }
}
