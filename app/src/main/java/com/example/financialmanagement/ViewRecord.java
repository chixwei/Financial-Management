package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class ViewRecord extends AppCompatActivity {

    ImageView back_button, delete_button, record_category_image;
    TextView record_category_name, record_category, record_amount, record_date, record_memo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewRecord.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // delete button
        delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code to delete
            }
        });

        // declare record details
        record_category_name = (TextView)findViewById(R.id.record_category_name);
        record_category = (TextView)findViewById(R.id.record_category);
        record_amount = (TextView)findViewById(R.id.record_amount);
        record_date = (TextView)findViewById(R.id.record_date);
        record_memo = (TextView)findViewById(R.id.record_memo);

        // get record details
        record_category_name.setText(getIntent().getStringExtra("category_name"));
        record_category.setText(getIntent().getStringExtra("category"));
        record_amount.setText(getIntent().getStringExtra("amount"));
        record_date.setText(getIntent().getStringExtra("date"));
        record_memo.setText(getIntent().getStringExtra("memo"));
    }
}