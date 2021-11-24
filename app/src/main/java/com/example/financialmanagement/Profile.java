package com.example.financialmanagement;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Map;

public class Profile extends Fragment {

    ImageView logout_button, edit_username;
    TextView username, total_balance, total_income, total_expenses;
    Dialog dialog;
    String user_id, Uname;
    FirebaseUser user;
    DatabaseReference ref, database_income, database_expenses;
    EditText new_username;

    Double expsum = 0.0, incsum = 0.0;

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
        total_balance.setText(String.format(Locale.US, "%.2f", balance));
        Log.d("ADebugTag", "expensesValue: " + Double.toString(balance));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        dialog = new Dialog(view.getContext());
        total_balance = (TextView) view.findViewById(R.id.txt_balance_amount);
        
        // logout button
        logout_button = view.findViewById(R.id.logout_button);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.logout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                // logout back button
                Button logout_back_button = dialog.findViewById(R.id.logout_back_button);
                logout_back_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // confirm logout button
                Button confirm_logout_button = dialog.findViewById(R.id.confirm_logout_button);
                confirm_logout_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), Welcome3.class);
                        startActivity(intent);
                    }
                });
            }
        });

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        // set username
        username = view.findViewById(R.id.username);
        ref = FirebaseDatabase.getInstance().getReference().child("User").child(user_id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Uname = snapshot.child("username").getValue().toString();
                username.setText(Uname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        total_balance = (TextView)view.findViewById(R.id.txt_balance_amount);
        database_expenses = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Expenses");
        database_income = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Income");

        // retrieve total income value
        total_income = (TextView)view.findViewById(R.id.txt_income_amount);
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
                    total_income.setText(String.format(Locale.US, "%.2f", incsum));
                    Balance();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // retrieve total expenses value
        total_expenses = (TextView)view.findViewById(R.id.txt_expense_amount);
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
                    total_expenses.setText(String.format(Locale.US, "%.2f", expsum));
                    Balance();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        database_expenses = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Expenses");
        database_income = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Income");

        //retrieve total income value
        total_income = (TextView)view.findViewById(R.id.txt_income_amount);
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
                    total_income.setText(String.format(Locale.US, "%.2f", incsum));
                    Balance();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // retrieve total expenses value
        total_expenses = (TextView)view.findViewById(R.id.txt_expense_amount);
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
                    total_expenses.setText(String.format(Locale.US, "%.2f", expsum));
                    Balance();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // edit username
        edit_username = view.findViewById(R.id.edit_username);
        edit_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.edit_username);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                new_username = dialog.findViewById(R.id.new_username);
                new_username.setText(Uname);

                // edit close button
                ImageView close_button = dialog.findViewById(R.id.close_button);
                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                // confirm edit button
                Button update_button = dialog.findViewById(R.id.update_button);
                update_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isUsernameChanged()) {
                            Toast.makeText(getActivity(), "Username has been updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "Username has not been updated", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return view;
    }

    private boolean isUsernameChanged() {
        if(!Uname.equals(new_username.getText().toString())) {
            ref.child("username").setValue(new_username.getText().toString());
            return true;
        } else {
            return false;
        }
    }
}