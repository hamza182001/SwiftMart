package com.example.madgrocery_project;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;


public class ExploreFragment extends Fragment {
    CardView FruitsVeges,Bakery,Beverages,Snacks,MeatsFish,DairyEggs;
    TextInputEditText etSearchProduct;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.fragment_explore, container, false);
         init();
       final Handler handler = new Handler(Looper.getMainLooper());
         FruitsVeges.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openCategoryActivity("fruits and vegetables");
             }
         });
         Bakery.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openCategoryActivity("bakery");
             }
         });
         Beverages.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openCategoryActivity("beverages");
             }
         });
         Snacks.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openCategoryActivity("snacks");
             }
         });
         MeatsFish.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openCategoryActivity("meats and fish");
             }
         });
         DairyEggs.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 openCategoryActivity("dairy and eggs");
             }
         });
        etSearchProduct.setOnFocusChangeListener((v, hasFocus) -> {

        });

        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {



                handler.removeCallbacksAndMessages(null);


                handler.postDelayed(() -> {

                    if (s.length() > 0) {

                        openCategoryActivity("search");
                    }
                }, 1000);
            }
        });


        return v;
    }
   private void init(){
        FruitsVeges=v.findViewById(R.id.FruitsVegetable);
        Bakery=v.findViewById(R.id.Bakery);
       Beverages=v.findViewById(R.id.Beverages);
       Snacks=v.findViewById(R.id.Snacks);
       MeatsFish=v.findViewById(R.id.MeatsFish);
       DairyEggs=v.findViewById(R.id.DairyEggs);
       etSearchProduct=v.findViewById(R.id.etSearchProduct);
   }
    private void openCategoryActivity(String category) {
        Intent intent = new Intent(getActivity(), CategoryActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

}