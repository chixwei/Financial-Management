package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private final static  int ID_INCOME = 1;
    private final static  int ID_EXPENSES = 2;
    private final static  int ID_HOME = 3;
    private final static  int ID_STATISTIC = 4;
    private final static  int ID_PROFILE = 5;


    FrameLayout frameLayout;
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.ic_nav_income));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.ic_nav_expenses));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.ic_nav_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.ic_nav_statistic));
        bottomNavigation.add(new MeowBottomNavigation.Model(5,R.drawable.ic_nav_profile));


        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new Home()).commit();

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;

                switch (item.getId()) {
                    case ID_INCOME:

                        fragment = new Income();
                        break;

                    case ID_EXPENSES:

                        fragment = new Expenses();
                        break;

                    case ID_HOME:

                        fragment = new Home();
                        break;

                    case ID_STATISTIC:

                        fragment = new Statistic();
                        break;

                    case ID_PROFILE:

                        fragment = new Profile();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();

            }
        });

        bottomNavigation.show(3,true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                //need this only can change page
            }
        });

    }

}