package com.example.bloodhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegistrationActivity extends AppCompatActivity {

    private TextView backButton;
    private Button donorButton,recepientButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registration);


        backButton = findViewById(R.id.backButton);
        recepientButton = findViewById(R.id.recepient_button);
        donorButton = findViewById(R.id.donor_button);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectRegistrationActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });


        recepientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectRegistrationActivity.this,RecepientRegistrationActivity.class);
                startActivity(i);
            }
        });

        donorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectRegistrationActivity.this, DonorRegistrationActivity.class);
                startActivity(i);
            }
        });




    }
}