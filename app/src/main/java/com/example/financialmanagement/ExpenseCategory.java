package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExpenseCategory extends AppCompatActivity {

    ImageView back_button;

    //Widgets
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //Firebase
    private DatabaseReference myRef;

    //Variables
    private ArrayList<DataModel> categoryList;
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_category);

        //retrieve data
        recyclerView = findViewById(R.id.expenseGrid);
        int numberOfColumns = 4;
        layoutManager = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Firebase
        myRef = FirebaseDatabase.getInstance().getReference();

        //ArrayList;
        categoryList = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        //Get Data Method
        GetDataFromFirebase();

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ExpenseCategory.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetDataFromFirebase() {
        Query query = myRef.child("expenseCategory");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataModel category = new DataModel();
                    category.setImageUrl(snapshot.child("image").getValue().toString());
                    category.setName(snapshot.child("title").getValue().toString());
                    categoryList.add(category);
                }
                expenseAdapter = new ExpenseAdapter(getApplicationContext(), categoryList);
                recyclerView.setAdapter(expenseAdapter);
                expenseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ClearAll() {
        if(categoryList != null) {
            categoryList.clear();
            if (expenseAdapter != null) {
                expenseAdapter.notifyDataSetChanged();
            }
        }
        categoryList = new ArrayList<>();
    }
}