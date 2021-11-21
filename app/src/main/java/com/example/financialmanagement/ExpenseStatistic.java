package com.example.financialmanagement;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExpenseStatistic extends AppCompatActivity {

    private PieChart pieChart;
    DatabaseReference firebase;
    FirebaseUser user;
    String user_id, category_name;
    Double same_amount = 0.0;
    double grandTotalUnTransformed = 0;

    HashMap<String, Double> allCategories = new HashMap<String, Double>();

    @Nullable

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_statistic, container, false);

        pieChart = v.findViewById(R.id.expensepiechart);
        setupPieChart();

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        //take expense value
        firebase = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Expenses");
        firebase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {

                    Map<String, String> map1 = (Map<String,String>) ds.getValue();
                    String category_name = map1.get("category_name");
                    Log.d("ADebugTag", "Category Name: " + (category_name));

                    Map<String, Object> map2 = (Map<String,Object>) ds.getValue();
                    Object expenses = map2.get("amount");
                    double expense_amount = Double.parseDouble((String.valueOf(expenses)));
                    Log.d("ADebugTag", "Expense Amount: " + Double.toString(expense_amount));

                    //check if map contain the category name
                    if (allCategories.containsKey(category_name)) {
                        Double current_expense_amount = allCategories.get(category_name);
                        Double new_expense_amount = expense_amount + current_expense_amount;
                        allCategories.replace(category_name, new_expense_amount);

                    } else {
                        allCategories.put(category_name, expense_amount);
                    }
                    grandTotalUnTransformed += expense_amount;


//                    if (category_name == category_name) {
//                        same_amount += expense_amount;
//                        Log.d("ADebugTag", "Same Amount: " + Double.toString(same_amount));
//                    } else {
//                        Double dif_amount = expense_amount;
//                        Log.d("ADebugTag", "Dif Amount: " + Double.toString(dif_amount));
//                    }

//                    expsum += expense_amount;
//                    setExpsum(expsum);
//                    total_expenses.setText(String.format(Locale.US, "%.2f", expsum));
//                    Balance();

                }
                allCategories.forEach((key, value) -> {
                    allCategories.replace(key, value / grandTotalUnTransformed);
                });
                loadPieChartData();

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        return v;
    }


    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        allCategories.forEach((key, value)-> {
            entries.add(new PieEntry(value.floatValue(), key));
        });

//        entries.add(new PieEntry(0.2f, "Food & Dining"));
//        entries.add(new PieEntry(0.15f, "Medical"));
//        entries.add(new PieEntry(0.10f, "Entertainment"));
//        entries.add(new PieEntry(0.25f, "Electricity and Gas"));
//        entries.add(new PieEntry(0.3f, "Housing"));

        //colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);

        //pie chart words
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        //animation
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}