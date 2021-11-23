package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Welcome2 extends AppCompatActivity {

    Button skip_button;
    ImageView back_button, next_button, dot1, dot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);

        // skip button
        skip_button = findViewById(R.id.skip_button);
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome2.this, Login.class);
                startActivity(intent);
            }
        });

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Welcome2.this, Welcome1.class);
                startActivity(intent);
            }
        });

        // next button
        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome2.this, Welcome3.class);
                startActivity(intent);
            }
        });

        // dot1
        dot1 = findViewById(R.id.dot1);
        dot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome2.this, Welcome1.class);
                startActivity(intent);
            }
        });

        // dot3
        dot3 = findViewById(R.id.dot3);
        dot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome2.this, Welcome3.class);
                startActivity(intent);
            }
        });
    }
}