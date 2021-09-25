package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class IncomeCategory extends AppCompatActivity {

    ImageView back_button;
    Spinner spinner;
    Button go_to_add_income;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_category);

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(IncomeCategory.this, Home.class);
                startActivity(intent);
            }
        });

        // category spinner
        spinner = findViewById(R.id.spinner_category);

        // spinner list
        List<String> category_list = new ArrayList<>();
        category_list.add(0, "Income");
        category_list.add("Expenses");

        ArrayAdapter<String> categoryAdapter;
        categoryAdapter = new ArrayAdapter<>(this, R.layout.category_spinner, category_list);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (parent.getItemAtPosition(i).equals("Income")) {
                    // do nothing
                } else {
                    // on selecting a spinner
                    String item = parent.getItemAtPosition(i).toString();
                    // link to another activity
                    if (parent.getItemAtPosition(i).equals("Expenses")) {
                        Intent intent = new Intent(IncomeCategory.this, ExpenseCategory.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // go to add income **temporary**
        go_to_add_income = findViewById(R.id.go_to_add_income);
        go_to_add_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(IncomeCategory.this, AddIncome.class);
                startActivity(intent);
            }
        });
    }
}