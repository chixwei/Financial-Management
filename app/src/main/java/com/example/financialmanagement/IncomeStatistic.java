package com.example.financialmanagement;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

import static com.github.mikephil.charting.components.Legend.LegendForm.CIRCLE;

public class IncomeStatistic extends Fragment {

    private PieChart pieChart;
    DatabaseReference firebase;
    FirebaseUser user;
    String user_id;
    double grandTotalUnTransformed = 0;

    HashMap<String, Double> allCategories = new HashMap<String, Double>();

    @Nullable

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_income_statistic, container, false);

        pieChart = v.findViewById(R.id.incomepiechart);
        setupPieChart();

        // get user id
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getUid();
        }

        //take expense value
        firebase = FirebaseDatabase.getInstance().getReference("User").child(user_id).child("Income");
        firebase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for(DataSnapshot ds : snapshot.getChildren()) {

                    Map<String, String> map1 = (Map<String,String>) ds.getValue();
                    String category_name = map1.get("category_name");
                    Log.d("ADebugTag", "Category Name: " + (category_name));

                    Map<String, Object> map2 = (Map<String,Object>) ds.getValue();
                    Object income = map2.get("amount");
                    double income_amount = Double.parseDouble((String.valueOf(income)));
                    Log.d("ADebugTag", "Expense Amount: " + Double.toString(income_amount));

                    //check if map contain the category name
                    if (allCategories.containsKey(category_name)) {
                        Double current_income_amount = allCategories.get(category_name);
                        Double new_income_amount = income_amount + current_income_amount;
                        allCategories.replace(category_name, new_income_amount);

                    } else {
                        allCategories.put(category_name, income_amount);
                    }
                    grandTotalUnTransformed += income_amount;

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
        //show label
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Income");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setXEntrySpace(7);
        l.setYEntrySpace(3);
        l.setTextSize(16);
        l.setDrawInside(false);
        l.setEnabled(true);
        l.setForm(CIRCLE);
        l.setWordWrapEnabled(true);



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        allCategories.forEach((key, value)-> {
            entries.add(new PieEntry(value.floatValue(), key));
        });

        //colors
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Income Category");
        dataSet.setColors(colors);

        //pie chart words
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(18f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        //animation
        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}