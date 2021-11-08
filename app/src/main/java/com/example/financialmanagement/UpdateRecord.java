package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateRecord extends AppCompatActivity {

    ImageView back_button, delete_button;
    TextView category_name;
    EditText amount, date, memo, img;
    Button update_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateRecord.this, Home.class);
                startActivity(intent);
            }
        });

        // delete button
        delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code to delete record
            }
        });

        // define variable
        category_name = findViewById(R.id.category_name);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        memo = findViewById(R.id.memo);
        img = findViewById(R.id.img);

        // set record details
        //category_name.setText();
    }
}