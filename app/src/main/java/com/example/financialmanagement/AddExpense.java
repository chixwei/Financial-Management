package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddExpense extends AppCompatActivity {

    ImageView back_button;
    EditText expense_amount, expense_date, expense_memo;
    Button add_button;
    String user_id;
    FirebaseUser user;
    DatabaseReference ref, expense_ref;
    Piggy piggy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddExpense.this, ExpenseCategory.class);
                startActivity(intent);
            }
        });

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        expense_amount = findViewById(R.id.expense_amount);
        expense_memo = findViewById(R.id.expense_memo);

        piggy = new Piggy();
        ref = FirebaseDatabase.getInstance().getReference().child(user_id);

        // add_button
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // error if empty
                if (expense_amount.getText().toString().length() == 0) {
                    expense_amount.setError("Expense amount is required");
                } else {
                    expense_ref = ref.child("Expenses");
                    piggy.setExpense_amount(Double.parseDouble(expense_amount.getText().toString().trim()));
                    piggy.setExpense_memo(expense_memo.getText().toString().trim());
                    expense_ref.push().setValue(piggy);
                    Toast.makeText(getApplicationContext(),"data inserted successfully",Toast.LENGTH_SHORT).show();

                    // back to home page
                    Intent intent = new Intent(AddExpense.this, Home.class);
                    startActivity(intent);
                }
            }
        });

    }
}