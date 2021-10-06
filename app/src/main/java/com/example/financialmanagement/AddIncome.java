package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddIncome extends AppCompatActivity {

    ImageView back_button;
    TextView income_category_name;
    EditText income_amount, income_date, income_memo;
    Button add_button;
    String user_id;
    FirebaseUser user;
    DatabaseReference ref;
    Piggy piggy;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddIncome.this, IncomeCategory.class);
                startActivity(intent);
            }
        });

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        // define variable
        income_category_name = findViewById(R.id.income_category_name);
        income_amount = findViewById(R.id.income_amount);
        income_date = findViewById(R.id.income_date);
        income_memo = findViewById(R.id.income_memo);

        // set amount decimal to max 2
        income_amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

        // select date
        income_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(AddIncome.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        income_date.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        piggy = new Piggy();
        ref = FirebaseDatabase.getInstance().getReference().child("User").child(user_id).child("Income");

        // add_button
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // error if empty
                if (income_amount.getText().toString().length() == 0) {
                    income_amount.setError("Income amount is required");
                } else {
                    piggy.setCategory_name(income_category_name.getText().toString().trim());
                    piggy.setAmount(Double.parseDouble(income_amount.getText().toString().trim()));
                    piggy.setDate(income_date.getText().toString().trim());
                    piggy.setMemo(income_memo.getText().toString().trim());
                    ref.push().setValue(piggy);
                    Toast.makeText(getApplicationContext(),"data inserted successfully",Toast.LENGTH_SHORT).show();

                    // back to home page
                    Intent intent = new Intent(AddIncome.this, Home.class);
                    startActivity(intent);
                }
            }
        });
    }

    // set decimal = 2
    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }
}