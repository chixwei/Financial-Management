package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IncomeCategory extends AppCompatActivity {

    ImageView back_button;

    //Widgets
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //Firebase
    private DatabaseReference myRef;

    //Variables
    private ArrayList<DataModel> categoryList;
    private IncomeAdapter incomeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_category);

        //retrieve data
        recyclerView = findViewById(R.id.incomeGrid);
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
                Intent intent= new Intent(IncomeCategory.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetDataFromFirebase() {

        Query query = myRef.child("incomeCategory");
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
                incomeAdapter = new IncomeAdapter(getApplicationContext(), categoryList);
                recyclerView.setAdapter(incomeAdapter);
                incomeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ClearAll() {
        if(categoryList != null) {
            categoryList.clear();
            if (incomeAdapter != null) {
                incomeAdapter.notifyDataSetChanged();
            }
        }
        categoryList = new ArrayList<>();
    }
}