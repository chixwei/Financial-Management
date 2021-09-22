package com.example.financialmanagement;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends Fragment {

    FloatingActionButton add_button, add_expense_button, add_income_button;
    TextView add_expense_text, add_income_text;
    boolean isFABOpen;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);

        // test database
        Toast.makeText(getActivity(),"Firebase connection successful", Toast.LENGTH_SHORT).show();

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