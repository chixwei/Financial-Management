package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateRecord extends AppCompatActivity {

    ImageView back_button, delete_button, category_image;
    TextView category_name;
    EditText amount, date, memo, img;
    Spinner spinner;
    Button update_button;
    Double Amount = 0.0;
    String _Category_Name, _Category_Img, _Amount, _Date, _Memo, _Img, _Category, _Record_Id, user_id;
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
                Intent intent = new Intent(UpdateRecord.this, MainActivity.class);
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
        category_image = findViewById(R.id.category_image);
        amount = findViewById(R.id.amount);
        date = findViewById(R.id.date);
        memo = findViewById(R.id.memo);
        img = findViewById(R.id.img);

        // get record details
        _Category_Name = getIntent().getStringExtra("category_name");
        _Category_Img = getIntent().getStringExtra("category_image");
        _Amount = getIntent().getStringExtra("amount");
        _Date = getIntent().getStringExtra("date");
        _Memo = getIntent().getStringExtra("memo");
        _Category = getIntent().getStringExtra("category");
        _Img = getIntent().getStringExtra("image");
        _Record_Id = getIntent().getStringExtra("record_id");

        // set record details
        category_name.setText(_Category_Name);
        amount.setText(_Amount);
        date.setText(_Date);
        memo.setText(_Memo);
        img.setText(_Img);
        Log.d("Tag", "rec_img: " + _Img);

        // set category image
        Glide.with(UpdateRecord.this)
                .load(_Category_Img)
                .into(category_image);
        Log.d("Tag", "cat_img: " + _Category_Img);

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
        category_list.add("EUR - Euro");
        category_list.add("INR - Indian Rupee");
        category_list.add("JPY - Japanese Yen");
        category_list.add("KRW - South Korean Won");
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
                        date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
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

        Log.d("Tag", "user id test!: " + user_id);
        Log.d("Tag", "category test!: " + _Category);
        Log.d("Tag", "record id test!: " + _Record_Id);

        // declare database
        ref = FirebaseDatabase.getInstance().getReference().child("User").child(user_id).child(_Category).child(_Record_Id);
        storageReference = FirebaseStorage.getInstance().getReference().child("User").child(user_id).child(_Category);

        // update button
        update_button = findViewById(R.id.update_button);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change currency
                String selectedCurrency = spinner.getSelectedItem().toString();
                switch (selectedCurrency) {
                    case "BGP - British Pound":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(amount.getText().toString()) / 0.178));
                        break;
                    case "CNY - Chinese Yuan Renminbi":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(amount.getText().toString()) / 1.517));
                        break;
                    case "EUR - Euro":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(amount.getText().toString()) / 0.212));
                        break;
                    case "INR - Indian Rupee":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(amount.getText().toString()) / 17.669));
                        break;
                    case "JPY - Japanese Yen":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(amount.getText().toString()) / 27.318));
                        break;
                    case "KRW - South Korean Won":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(amount.getText().toString()) / 282.036));
                        break;
                    case "SGD - Singapore Dollar":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(amount.getText().toString()) / 0.325));
                        break;
                    case "USD - US Dollar":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(amount.getText().toString()) / 0.234));
                        break;
                    default:
                        Amount = Double.parseDouble(amount.getText().toString());
                }

                // loop the below functions
                ArrayList<Boolean> status = new ArrayList<>();
                status.add(isMemoChanged());
                status.add(isDateChanged());
                status.add(isAmountChanged());
                status.add(isImageChanged());

                if (status.contains(true)) {
                    Toast.makeText(getApplicationContext(), "Record details have been updated", Toast.LENGTH_SHORT).show();
                    // back to home page
                    Intent intent = new Intent(UpdateRecord.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Record details is same cannot be updated", Toast.LENGTH_SHORT).show();
                    // back to home page
                    Intent intent = new Intent(UpdateRecord.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    // select image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                // get file name
                Cursor returnCursor = getContentResolver().query(FilePathUri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                img.setText(returnCursor.getString(nameIndex));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean isAmountChanged(){
        if(!_Amount.equals(amount.getText().toString())) {
            ref.child("amount").setValue(Amount);
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

    private boolean isImageChanged() {
        if (!img.getText().toString().equals("")) {
            if (getIntent().getStringExtra("image") == null || !getIntent().getStringExtra("image").equals(img.getText().toString())) {
                String imageFileName = System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);
                ref.child("image_url").setValue(imageFileName);
                StorageReference storageReference2 = storageReference.child(imageFileName);
                storageReference2.putFile(FilePathUri).addOnSuccessListener(taskSnapshot -> {
                    _Img = imageFileName;
                });
                return true;
            }
        } else {
            return false;
        }
        return false;
    }
}