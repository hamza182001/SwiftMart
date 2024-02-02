package com.example.madgrocery_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PaymentMethodScreen extends AppCompatActivity {
    private RadioGroup paymentOptionsGroup;
    private Button btnContinue;
    private static final String PREF_SELECTED_PAYMENT_OPTION = "selected_payment_option";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_screen);
        paymentOptionsGroup = findViewById(R.id.paymentOptionsGroup);
        btnContinue = findViewById(R.id.btnContinue);

        // Check if a payment option is already selected and set it
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String selectedOption = preferences.getString(PREF_SELECTED_PAYMENT_OPTION, null);
        if (selectedOption != null) {
            RadioButton radioButton = findRadioButtonByText(paymentOptionsGroup, selectedOption);
            if (radioButton != null) {
                radioButton.setChecked(true);
            }
        }

        paymentOptionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Enable the Continue button when an option is selected
                btnContinue.setEnabled(true);
            }
        });
    }

    // This method will be called when the Continue button is clicked
    public void onContinueButtonClick(View view) {
        int selectedId = paymentOptionsGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            // No option selected, display a toast or handle it in your preferred way
            Toast.makeText(this, "Please select a payment option", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the selected payment option
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedPaymentOption = selectedRadioButton.getText().toString();

            // Save the selected option in SharedPreferences
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREF_SELECTED_PAYMENT_OPTION, selectedPaymentOption);
            editor.apply();

            // Now you can use the selectedPaymentOption string to handle the option accordingly
            if ("Cash".equals(selectedPaymentOption)) {
                // Handle Cash option
            } else if ("Card".equals(selectedPaymentOption)) {
                // Handle Card option
            }
        }
    }

    // Helper method to find RadioButton in a RadioGroup by text
    private RadioButton findRadioButtonByText(RadioGroup radioGroup, String text) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View view = radioGroup.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) view;
                if (radioButton.getText().toString().equals(text)) {
                    return radioButton;
                }
            }
        }
        return null;
    }
}