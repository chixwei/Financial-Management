package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExpense extends AppCompatActivity {

    ImageView back_button;
    EditText expense_amount, expense_date, expense_memo;
    Button add_button;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddExpense.this, AddExpenseCategory.class);
                startActivity(intent);
            }
        });

        expense_amount = findViewById(R.id.expense_amount);
        expense_date = findViewById(R.id.expense_date);
        expense_memo = findViewById(R.id.expense_memo);

        ref = FirebaseDatabase.getInstance().getReference().child("Expenses");

        // add_button
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}