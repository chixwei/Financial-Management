package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateRecord extends AppCompatActivity {

    ImageView back_button, delete_button;
    TextView category_name;
    EditText amount, date, memo, img;
    Spinner spinner;
    Button update_button;
    Double Amount = 0.0;
    String _Category_Name, _Amount, _Date, _Memo, _Img, _Category, user_id;
    FirebaseUser user;
    DatabaseReference ref;
    Piggy piggy;
    DatePickerDialog picker;
    Uri FilePathUri;
    StorageReference storageReference;
    int Image_Request_Code = 7;

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

        // get record details
        _Category_Name = getIntent().getStringExtra("category_name");
        _Amount = getIntent().getStringExtra("amount");
        _Date = getIntent().getStringExtra("date");
        _Memo = getIntent().getStringExtra("memo");
        _Category = getIntent().getStringExtra("category");

        // set record details
        category_name.setText(_Category_Name);
        amount.setText(_Amount);
        date.setText(_Date);
        memo.setText(_Memo);

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        // category spinner
        spinner = findViewById(R.id.spinner_category);

        // spinner list
        List<String> category_list = new ArrayList<>();
        category_list.add(0, "MYR - Malaysian Ringgit");
        category_list.add("BGP - British Pound");
        category_list.add("CNY - Chinese Yuan Renminbi");
        category_list.add("SGD - Singapore Dollar");
        category_list.add("USD - US Dollar");

        ArrayAdapter<String> categoryAdapter;
        categoryAdapter = new ArrayAdapter<>(this, R.layout.category_spinner, category_list);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(categoryAdapter);

        // select date
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(UpdateRecord.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        // select image
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference().child("User").child(user_id).child(_Category);

        /*
        // update button
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change currency
                String selectedCurrency = spinner.getSelectedItem().toString();
                switch (selectedCurrency) {
                    case "BGP - British Pound":
                        Amount = Double.parseDouble(amount.getText().toString()) / 0.176;
                        break;
                    case "CNY - Chinese Yuan Renminbi":
                        Amount = Double.parseDouble(amount.getText().toString()) / 1.547;
                        break;
                    case "SGD - Singapore Dollar":
                        Amount = Double.parseDouble(amount.getText().toString()) / 0.326;
                        break;
                    case "USD - US Dollar":
                        Amount = Double.parseDouble(amount.getText().toString()) / 0.241;
                        break;
                    default:
                        Amount = Double.parseDouble(amount.getText().toString());
                }

                if (isAmountChanged() || isDateChanged() || isMemoChanged()) {
                    Toast.makeText(getApplicationContext(), "Record details have been updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Record details is same cannot be updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }
    /*
    private boolean isAmountChanged(){
        if(!_Amount.equals(amount.getText().toString())) {
            ref.child("amount").setValue(amount.getText().toString());
            _Amount = amount.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isDateChanged(){
        if(!_Date.equals(date.getText().toString())) {
            ref.child("date").setValue(date.getText().toString());
            _Date = date.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isMemoChanged(){
        if(!getIntent().getStringExtra("memo").equals(memo.getText().toString())) {
            ref.child("memo").setValue(memo.getText().toString());
            _Memo = memo.getText().toString();
            return true;
        } else {
            return false;
        }
    }

     */
}