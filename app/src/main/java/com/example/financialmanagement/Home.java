package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Home extends Fragment {

    FloatingActionButton add_button, add_expense_button, add_income_button;
    TextView add_expense_text, add_income_text, total_income, total_expenses, total_balance;
    String user_id;
    FirebaseUser user;
    RecyclerView recyclerView;
    DatabaseReference database_expenses, database_income;
    PiggyAdapter PiggyAdapter;
    ArrayList<Piggy> list;
    boolean isFABOpen;

    Double expsum=0.0, incsum=0.0;

    public void setExpsum(double expsum) {
        this.expsum = expsum;
    }

    public double getExpsum() {
        return this.expsum;
    }

    public void setIncsum(double incsum) {
        this.incsum = incsum;
    }

    public double getIncsum() {
        return this.incsum;
    }


    //retrieve balance
    public void Balance(){
        double income = Double.parseDouble(String.valueOf(getIncsum()));
        double expense = Double.parseDouble(String.valueOf(getExpsum()));
        double balance = income - expense;
        total_balance.setText(String.valueOf(balance));
        Log.d("ADebugTag", "expensesValue: " + Double.toString(balance));
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        // retrieve data from database
        recyclerView = v.findViewById(R.id.recyclerView);
        database_expenses = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Expenses");
        database_income = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Income");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        PiggyAdapter = new PiggyAdapter(getActivity(), list);
        recyclerView.setAdapter(PiggyAdapter);
        total_balance = (TextView)v.findViewById(R.id.txt_balance_amount);


        //retrieve total income value
        total_income = (TextView)v.findViewById(R.id.txt_income_amount);
        database_income.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                incsum = 0.0;

                for(DataSnapshot ds : snapshot.getChildren()) {

                    Map<String, Object> map = (Map<String,Object>) ds.getValue();
                    Object income = map.get("amount");
                    double income_amount = Double.parseDouble((String.valueOf(income)));
                    incsum += income_amount;
                    setIncsum(incsum);
                    total_income.setText(String.valueOf(incsum));
                    Balance();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        // retrieve total expenses value
        total_expenses = (TextView)v.findViewById(R.id.txt_expense_amount);
        database_expenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                expsum = 0.0;

                for(DataSnapshot ds : snapshot.getChildren()) {

                    Map<String, Object> map = (Map<String,Object>) ds.getValue();
                    Object expenses = map.get("amount");
                    double expense_amount = Double.parseDouble((String.valueOf(expenses)));
                    expsum += expense_amount;
                    setExpsum(expsum);
                    total_expenses.setText(String.valueOf(expsum));
                    Balance();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        // retrieve expenses record
        database_expenses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Piggy piggy = dataSnapshot.getValue(Piggy.class);
                    list.add(piggy);
                }
                PiggyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // retrieve income record
        database_income.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Piggy piggy = dataSnapshot.getValue(Piggy.class);
                    list.add(piggy);
                }
                PiggyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // define text
        add_expense_text = v.findViewById(R.id.add_expense_text);
        add_income_text = v.findViewById(R.id.add_income_text);

        // add button
        add_button = v.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        // add expense button
        add_expense_button = v.findViewById(R.id.add_expense_button);
        add_expense_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), ExpenseCategory.class);
                startActivity(in);
            }
        });

        // add income button
        add_income_button = v.findViewById(R.id.add_income_button);
        add_income_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), IncomeCategory.class);
                startActivity(in);
            }
        });
        
        // press anywhere to close the expandable fab
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFABMenu();
            }
        });

        return v;
    }


    // to display expandable floating action button
    private void showFABMenu(){
        isFABOpen = true;
        // set fab visibility
        add_expense_button.setVisibility(View.VISIBLE);
        add_income_button.setVisibility(View.VISIBLE);
        add_expense_text.setVisibility(View.VISIBLE);
        add_income_text.setVisibility(View.VISIBLE);
        // set main fab img
        add_button.setImageResource(R.drawable.ic_close);
    }


    // to not display expandable floating action button
    private void closeFABMenu(){
        isFABOpen = false;
        // set fab visibility
        add_expense_button.setVisibility(View.INVISIBLE);
        add_income_button.setVisibility(View.INVISIBLE);
        add_expense_text.setVisibility(View.INVISIBLE);
        add_income_text.setVisibility(View.INVISIBLE);
        //set main fab img
        add_button.setImageResource(R.drawable.ic_add);
    }
}