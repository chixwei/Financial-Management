package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Locale;

public class ViewRecord extends AppCompatActivity {

    ImageView back_button, delete_button, record_category_image, record_image;
    TextView record_category_name, record_category, record_amount, record_date, record_memo;
    Dialog dialog;
    DatabaseReference ref;
    String user_id;
    FirebaseUser user;
    StorageReference storageReference;

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

        dialog = new Dialog(ViewRecord.this);

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        // delete button
        delete_button = findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.delete);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                // logout back button
                Button delete_back_button = dialog.findViewById(R.id.delete_back_button);
                delete_back_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // confirm logout button
                Button confirm_delete_button = dialog.findViewById(R.id.confirm_delete_button);
                confirm_delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // code to delete record
                        ref = FirebaseDatabase.getInstance().getReference().child("User").child(user_id).child(getIntent().getStringExtra("category"));
                        //Query query = ref.child(ref.getKey());
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // get all the child key of that category
                                for (DataSnapshot child: dataSnapshot.getChildren()) {
                                    String key = child.getKey();
                                    Log.d("Record key:", key);
                                }

                                /*
                                // get only the first key of child of that category
                                String key = dataSnapshot.getChildren().iterator().next().getKey();
                                DatabaseReference ref2 = ref.child(key);
                                Log.d("Tag", "Get Key: " +(key));*/

                                // remove the record
                                //dataSnapshot.getRef().removeValue();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        // go to home after delete
                        Intent intent = new Intent(ViewRecord.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        // declare record details
        record_category_name = (TextView)findViewById(R.id.record_category_name);
        record_category_image = (ImageView) findViewById(R.id.record_category_image);
        record_category = (TextView)findViewById(R.id.record_category);
        record_amount = (TextView)findViewById(R.id.record_amount);
        record_date = (TextView)findViewById(R.id.record_date);
        record_memo = (TextView)findViewById(R.id.record_memo);
        record_image = (ImageView)findViewById(R.id.record_image);

        // get record details
        record_category_name.setText(getIntent().getStringExtra("category_name"));
        record_category.setText(getIntent().getStringExtra("category"));
        record_amount.setText(getIntent().getStringExtra("amount"));
        record_date.setText(getIntent().getStringExtra("date"));
        record_memo.setText(getIntent().getStringExtra("memo"));
        //record_image.setImageURI(Uri.parse("image"));

        Glide.with(ViewRecord.this)
                .load(getIntent().getStringExtra("category_image"))
                .into(record_category_image);
        Log.d("Tag", "cat_img: " +(getIntent().getStringExtra("category_image")));

        /*
        Glide.with(ViewRecord.this)
                .load(getIntent().getStringExtra("image"))
                .into(record_image);
        Log.d("Tag", "rec_img: " +(getIntent().getStringExtra("image")));
        */
    }
}