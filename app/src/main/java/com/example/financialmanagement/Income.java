package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Income extends Fragment {

    String user_id;
    FirebaseUser user;
    RecyclerView recyclerView;
    DatabaseReference database;
    PiggyAdapter PiggyAdapter;
    ArrayList<Piggy> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_income, container, false);

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        // retrieve data from database
        recyclerView = v.findViewById(R.id.recyclerView);
        database = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Income");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        PiggyAdapter = new PiggyAdapter(getActivity(), list);
        recyclerView.setAdapter(PiggyAdapter);

        database.orderByChild("date").addValueEventListener(new ValueEventListener() {
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

        return v;
    }
}