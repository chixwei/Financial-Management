package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Home extends Fragment {

    FloatingActionButton add_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);

        /*
        final View view = inflater.inflate(R.layout.activity_home, container, false);

        // add button
        add_button = (FloatingActionButton) view.findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, ExpenseCategory.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;

         */

    }
}