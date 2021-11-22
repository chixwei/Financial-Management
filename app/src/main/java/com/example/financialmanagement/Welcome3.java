package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Welcome3 extends AppCompatActivity {

    ImageView back_button, dot1, dot2;
    Button login_button, signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome3);

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Welcome3.this, Welcome2.class);
                startActivity(intent);
            }
        });

        // login button
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome3.this, Login.class);
                startActivity(intent);
            }
        });

        // signup button
        signup_button = findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome3.this, SignUp.class);
                startActivity(intent);
            }
        });

        // dot1
        dot1 = findViewById(R.id.dot1);
        dot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome3.this, Welcome1.class);
                startActivity(intent);
            }
        });

        // dot2
        dot2 = findViewById(R.id.dot2);
        dot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome3.this, Welcome2.class);
                startActivity(intent);
            }
        });
    }
}