package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddExpense extends AppCompatActivity {

    ImageView back_button;
    TextView expense_category_name;
    EditText expense_amount, expense_date, expense_memo, expense_img;
    Spinner spinner;
    Double Amount = 0.0;
    Button add_button;
    String user_id;
    FirebaseUser user;
    DatabaseReference ref, expenseCategory;
    Piggy piggy;
    DatePickerDialog picker;
    Uri FilePathUri;
    StorageReference storageReference;
    int Image_Request_Code = 7;

    TextView title;
    private String expimageurl, name;

    public void setExpimageurl(String expimageurl) {
        this.expimageurl = expimageurl;
    }

    public String getExpimageurl() {
        return this.expimageurl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        //get category text
        title = (TextView) findViewById(R.id.expense_category_name);
        title.setText(getIntent().getStringExtra("title"));
        String title_word = title.getText().toString();

        //get category image
        new AddExpense.DownloadImageTask((ImageView) findViewById(R.id.addExpenseImage))
                .execute(getIntent().getExtras().getString("image"));

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddExpense.this, ExpenseCategory.class);
                startActivity(intent);
            }
        });

        // category spinner
        spinner = findViewById(R.id.spinner_category);
        expense_amount = findViewById(R.id.expense_amount);

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

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        // define variable
        expense_category_name = findViewById(R.id.expense_category_name);
        expense_date = findViewById(R.id.expense_date);
        expense_memo = findViewById(R.id.expense_memo);
        expense_img = findViewById(R.id.expense_img);

        // select date
        expense_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // date picker dialog
                picker = new DatePickerDialog(AddExpense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        expense_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        // select image
        expense_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code);
            }
        });

        piggy = new Piggy();
        ref = FirebaseDatabase.getInstance().getReference().child("User").child(user_id).child("Expenses");
        storageReference = FirebaseStorage.getInstance().getReference().child("User").child(user_id).child("Expenses");

        //get category title and icon url
        expenseCategory = FirebaseDatabase.getInstance().getReference("expenseCategory");
        expenseCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get icon url
                    Map<String, String> map = (Map<String, String>) ds.getValue();
                    String img = map.get("image");
                    if (map.get("title").equals(title.getText().toString())) {
                        setExpimageurl(img);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // add_button
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // error if empty
                if (expense_amount.getText().toString().length() == 0) {
                    expense_amount.setError("Expense amount is required");
                    return;
                }
                if (expense_date.getText().toString().length() == 0) {
                    expense_date.setError("Expense date is required");
                    return;
                }

                //change currency
                String selectedCurrency = spinner.getSelectedItem().toString();
                switch (selectedCurrency) {
                    case "BGP - British Pound":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(expense_amount.getText().toString()) / 0.178));
                        break;
                    case "CNY - Chinese Yuan Renminbi":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(expense_amount.getText().toString()) / 1.517));
                        break;
                    case "EUR - Euro":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(expense_amount.getText().toString()) / 0.212));
                        break;
                    case "INR - Indian Rupee":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(expense_amount.getText().toString()) / 17.669));
                        break;
                    case "JPY - Japanese Yen":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(expense_amount.getText().toString()) / 27.318));
                        break;
                    case "KRW - South Korean Won":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(expense_amount.getText().toString()) / 282.036));
                        break;
                    case "SGD - Singapore Dollar":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(expense_amount.getText().toString()) / 0.325));
                        break;
                    case "USD - US Dollar":
                        Amount = Double.parseDouble(String.format("%.2f", Double.parseDouble(expense_amount.getText().toString()) / 0.234));
                        break;
                    default:
                        Amount = Double.parseDouble(expense_amount.getText().toString());
                }

                if (FilePathUri != null && !FilePathUri.equals(Uri.EMPTY)) {
                    String imageFileName = System.currentTimeMillis() + "." + GetFileExtension(FilePathUri);
                    StorageReference storageReference2 = storageReference.child(imageFileName);
                    storageReference2.putFile(FilePathUri).addOnSuccessListener(taskSnapshot -> {
                        piggy.setCategory_url(getExpimageurl());
                        piggy.setCategory("Expenses");
                        piggy.setCategory_name(expense_category_name.getText().toString().trim());
                        //piggy.setAmount(Double.parseDouble(expense_amount.getText().toString().trim()));
                        piggy.setAmount(Amount);
                        piggy.setDate(expense_date.getText().toString().trim());
                        piggy.setMemo(expense_memo.getText().toString().trim());
                        piggy.setImage_url(imageFileName);
                        String ImageUploadId = ref.push().getKey();
                        ref.child(ImageUploadId).setValue(piggy);
                        Toast.makeText(getApplicationContext(), "data inserted successfully", Toast.LENGTH_SHORT).show();
                        // back to home page
                        Intent intent = new Intent(AddExpense.this, MainActivity.class);
                        startActivity(intent);
                    });
                } else {
                    piggy.setCategory_url(getExpimageurl());
                    piggy.setCategory("Expenses");
                    piggy.setCategory_name(expense_category_name.getText().toString().trim());
                    //piggy.setAmount(Double.parseDouble(expense_amount.getText().toString().trim()));
                    piggy.setAmount(Amount);
                    piggy.setDate(expense_date.getText().toString().trim());
                    piggy.setMemo(expense_memo.getText().toString().trim());
                    String ImageUploadId = ref.push().getKey();
                    ref.child(ImageUploadId).setValue(piggy);
                    Toast.makeText(getApplicationContext(), "data inserted successfully", Toast.LENGTH_SHORT).show();
                    // back to home page
                    Intent intent = new Intent(AddExpense.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    // get category image
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
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
                expense_img.setText(returnCursor.getString(nameIndex));
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
}