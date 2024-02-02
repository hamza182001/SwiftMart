package com.example.madgrocery_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;

public class Location_Screen extends AppCompatActivity {
    Button btnLocation;
    private static final String PREF_AUTHENTICATED = "authenticated";

    private static final String PREF_ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_screen);
        AutoCompleteTextView autoCompleteCity = findViewById(R.id.autoCompleteCity);
        AutoCompleteTextView autoCompleteArea = findViewById(R.id.autoCompleteArea);
        btnLocation = findViewById(R.id.btnLocation); // Assuming the button has an ID btnLocation

        final HashMap<String, String[]> cityAreaMap = new HashMap<>();
        cityAreaMap.put("Lahore", new String[]{"Gulberg", "Johar Town", "Model Town", "DHA", "Muslim Town"});
        cityAreaMap.put("Karachi", new String[]{"Clifton", "Saddar", "Defence", "North Nazimabad"});
        cityAreaMap.put("Faislabad", new String[]{"D Ground", "Susan Road", "Madina Town", "Jinnah Colony"});
        cityAreaMap.put("Multan", new String[]{"Cantt", "Bosan Road", "Shalimar", "Gulgasht"});
        cityAreaMap.put("Islamabad", new String[]{"SectorG-8", "SectorF-10", "SectorH-12", "SectorE-7"});

        String[] citySuggestions = {"Lahore", "Karachi", "Faislabad", "Multan", "Islamabad"};
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, citySuggestions);
        autoCompleteCity.setAdapter(cityAdapter);

        autoCompleteCity.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCity = (String) parent.getItemAtPosition(position);
            if (cityAreaMap.containsKey(selectedCity)) {
                ArrayAdapter<String> areaAdapter = new ArrayAdapter<>(Location_Screen.this, android.R.layout.simple_dropdown_item_1line, cityAreaMap.get(selectedCity));
                autoCompleteArea.setAdapter(areaAdapter);

                autoCompleteArea.setOnItemClickListener((parentArea, viewArea, positionArea, idArea) -> {
                    String selectedArea = (String) parentArea.getItemAtPosition(positionArea);
                    String address = selectedCity + ", " + selectedArea;


                    btnLocation.setOnClickListener(v -> {
                        Bundle b = getIntent().getExtras();
                        String Username = b.getString("Username");
                        String Email = b.getString("Email");
                        String Password = b.getString("Password");

                        String City = autoCompleteCity.getText().toString();
                        String Area = autoCompleteArea.getText().toString();

                        if (isValidLocation(City, Area,cityAreaMap)){

                            addUserToDatabase(Username, Email, Password, address);
                            saveUserLocation(address);
                            Intent i =new Intent(Location_Screen.this,MainActivity.class);
                            i.putExtra("address",address);
                            navigateToNextScreen(i);
                        }
                        else {
                            Toast.makeText(Location_Screen.this, "Invalid location", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
        });
    }
    private void saveUserLocation(String address) {
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_ADDRESS, address);
        editor.putBoolean(PREF_AUTHENTICATED, true);
        editor.apply();
    }
    private void addUserToDatabase(String username, String email, String password, String address) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users_info");

        String userId = databaseReference.push().getKey();

        User newUser = new User(userId, username, email, password, address);

        databaseReference.child(userId).setValue(newUser);
    }

    private void navigateToNextScreen(Intent i) {
        startActivity(i);
        finish();
    }

    private boolean isValidLocation(String selectedCity, String selectedArea, HashMap<String, String[]> cityAreaMap) {

        return cityAreaMap.containsKey(selectedCity) && Arrays.asList(cityAreaMap.get(selectedCity)).contains(selectedArea);
    }

}