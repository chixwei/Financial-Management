package com.example.financialmanagement;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Welcome1 extends AppCompatActivity {

    Button skip_button;
    ImageView next_button, dot2, dot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome1);

        // skip button
        skip_button = findViewById(R.id.skip_button);
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome1.this, Login.class);
                startActivity(intent);
            }
        });

        // next button
        next_button = findViewById(R.id.next_button);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome1.this, Welcome2.class);
                startActivity(intent);
            }
        });

        // dot2
        dot2 = findViewById(R.id.dot2);
        dot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome1.this, Welcome2.class);
                startActivity(intent);
            }
        });

        // dot3
        dot3 = findViewById(R.id.dot3);
        dot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome1.this, Welcome3.class);
                startActivity(intent);
            }
        });
    }
}