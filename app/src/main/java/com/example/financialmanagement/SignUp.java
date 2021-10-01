package com.example.financialmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

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

        // assign variable
        signupUname = findViewById(R.id.signupUname);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);
        signupRePassword = findViewById(R.id.signupRePassword);

        // sign up button
        signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // extract the data from the form
                String Uname = signupUname.getText().toString().trim();
                String Email = signupEmail.getText().toString().trim();
                String Password = signupPassword.getText().toString().trim();
                String RePassword = signupRePassword.getText().toString().trim();

                // error if empty
                if(Uname.isEmpty()) {
                    signupUname.setError("Username is required");
                    signupUname.requestFocus();
                    return;
                }

                if(Email.isEmpty()) {
                    signupEmail.setError("Email is required");
                    return;
                }

                // check email pattern
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    signupEmail.setError("Please enter a valid email");
                    return;
                }

                if(Password.isEmpty()) {
                    signupPassword.setError("Password is required");
                    return;
                }

                // check password length must be >= 6
                if(Password.length() < 6) {
                    signupPassword.setError("Minimum password length should be 6 characters");
                    return;
                }

                if(RePassword.isEmpty()) {
                    signupRePassword.setError("Please re-enter your password");
                    return;
                }

                // 2 password must same
                if(!Password.equals(RePassword)) {
                    signupRePassword.setError("Password do not match");
                    return;
                }

                // data is valid
                Toast.makeText(SignUp.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();

                // firebase
                fAuth = FirebaseAuth.getInstance();

                // if success
                fAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(Uname, Email);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUp.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SignUp.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
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