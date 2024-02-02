package com.example.madgrocery_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CategoryActivity extends AppCompatActivity {
    RecyclerView rvCategoryItem;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        String search = getIntent().getStringExtra("search");


        rvCategoryItem = findViewById(R.id.rvCategoryItems);


        if (search != null && !search.isEmpty()) {
            String[] categories = {"fruits and vegetables", "meats and fish", "snacks", "bakery", "dairy and eggs", "beverages"};

            for (String category : categories) {
                Query query = FirebaseDatabase.getInstance().getReference()
                        .child(category)
                        .orderByChild(search)
                        .startAt(search.toLowerCase())
                        .endAt(search.toLowerCase() + "\uf8ff");

                FirebaseRecyclerOptions<Category> options =
                        new FirebaseRecyclerOptions.Builder<Category>()
                                .setQuery(query, Category.class)
                                .build();

                adapter = new CategoryAdapter(options);
                rvCategoryItem.setAdapter(adapter);

            }
            }

         else{

                String category = getIntent().getStringExtra("category");
                FirebaseRecyclerOptions<Category> options =
                        new FirebaseRecyclerOptions.Builder<Category>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child(category), Category.class)
                                .build();
                adapter = new CategoryAdapter(options, category);
                rvCategoryItem.setAdapter(adapter);
            }





    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
