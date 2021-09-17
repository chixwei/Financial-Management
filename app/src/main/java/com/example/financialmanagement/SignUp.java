package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    ImageView back_button;
    EditText signupUname, signupEmail, signupPassword, signupRePassword;
    Button signupButton;
    TextView login_page;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //assign variable
        signupUname = findViewById(R.id.signupUname);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupRePassword = findViewById(R.id.signupRePassword);

        //sign up button
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //extract the data from the form
                String Uname = signupUname.getText().toString();
                String Email = signupEmail.getText().toString();
                String Password = signupPassword.getText().toString();
                String RePassword = signupRePassword.getText().toString();

                //Error if empty
                if(Uname.isEmpty()) {
                    signupUname.setError("Username is Required");
                    return;
                }

                if(Email.isEmpty()) {
                    signupEmail.setError("Email is Required");
                    return;
                }

                if(Password.isEmpty()) {
                    signupPassword.setError("Password is Required");
                    return;
                }

                if(RePassword.isEmpty()) {
                    signupRePassword.setError("Please Re-enter Your Password");
                    return;
                }

                //2 password must same
                if(!Password.equals(RePassword)) {
                    signupRePassword.setError("Password Do Not Match");
                    return;
                }

                //Data is valid
                //Register User

                Toast.makeText(SignUp.this, "Data Validated", Toast.LENGTH_SHORT).show();

                //Firebase
                fAuth = FirebaseAuth.getInstance();
                //If Success
                fAuth.createUserWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Go into Main Page
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();

                    }

                    //If Fail
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        // back button
        back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Welcome3.class);
                startActivity(intent);
            }
        });

        // login text
        login_page = findViewById(R.id.login_page);
        login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }
}

