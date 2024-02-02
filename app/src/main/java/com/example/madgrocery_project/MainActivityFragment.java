package com.example.madgrocery_project;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivityFragment extends Fragment {
    private TextView tvLocation;
    private ViewPager2 vpSlides;
    private RecyclerView recyclerView1,recyclerView2;
    private int[] slides = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3,R.drawable.slide5,R.drawable.slide6,R.drawable.slide7};
    private int currentPage = 0;
    private ProductAdapter adapter1,adapter2;
    TextInputEditText etSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);
        etSearch=rootView.findViewById(R.id.etSearch);

        tvLocation = rootView.findViewById(R.id.tvLocation);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user", MODE_PRIVATE);

        String address = sharedPreferences.getString("address", "");
        tvLocation.setText(address);

        vpSlides = rootView.findViewById(R.id.vpSlides);

        vpSlides.setAdapter(new ImagePagerAdapter());

        final Handler handler = new Handler();
        final int delay = 3000;
        vpSlides.postDelayed(new Runnable() {
            public void run() {
                if (currentPage == slides.length) {
                    currentPage = 0;
                }
                vpSlides.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, delay);
            }
        }, delay);

        recyclerView1 = rootView.findViewById(R.id.rv1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByKey().endAt("p3"), Product.class)
                        .build();


        adapter1 = new ProductAdapter(options);
        recyclerView1.setAdapter(adapter1);

        recyclerView2 = rootView.findViewById(R.id.rv2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        FirebaseRecyclerOptions<Product> options2 =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Products").orderByKey().startAfter("p3"), Product.class)
                        .build();

         adapter2 = new ProductAdapter(options2);
        recyclerView2.setAdapter(adapter2);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter1.startListening();
        adapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter1.stopListening();
        adapter2.stopListening();
    }




    private class ImagePagerAdapter extends RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder> {
        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            holder.imageView.setImageResource(slides[position]);
        }

        @Override
        public int getItemCount() {
            return slides.length;
        }

        class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.slideImageView);
            }
        }
    }



}
