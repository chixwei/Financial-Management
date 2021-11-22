package com.example.financialmanagement;

import static com.example.financialmanagement.R.*;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class Statistic extends Fragment {

    Button expense_button, income_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(layout.activity_statistic, container, false);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_statistic, new ExpenseStatistic());
        fragmentTransaction.commit();

        //expense statistic fragment
        expense_button = v.findViewById(R.id.expenses_statistic_button);
        expense_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_statistic, new ExpenseStatistic());
                fragmentTransaction.commit();
            }
        });

        //income statistic fragment
        income_button = v.findViewById(id.income_statistic_button);
        income_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(id.frame_statistic, new IncomeStatistic());
                fragmentTransaction.commit();
            }
        });


        return v;
    }


}