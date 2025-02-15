package com.example.do_an;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText signupName, signupEmail, signupUsername, signupPassword;
    private Button signupButton;
    private TextView loginRedirectText;
    FirebaseDatabase database;
    DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth and Database Reference
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Find Views
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup);
        loginRedirectText = findViewById(R.id.LoginRedirectText);

        // Handle SignUp Button Click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();

                // Validate inputs
                if (name.isEmpty()) {
                    signupName.setError("Name cannot be empty");
                } else if (email.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                } else if (username.isEmpty()) {
                    signupUsername.setError("Username cannot be empty");
                } else if (password.isEmpty()) {
                    signupPassword.setError("Password cannot be empty");
                } else {
                    // Firebase Authentication - Create a new user
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("SignUp", "createUserWithEmailAndPassword completed");

                                    if (task.isSuccessful()) {
                                        // If the task is successful, proceed with saving user data
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            HelperClass helperClass = new HelperClass(name, email, username, password);
                                            reference.child(username).setValue(helperClass);
                                            Log.d("SignUp", "Update database completed");
                                            Toast.makeText(SignUpActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    } else {
                                        // If task failed, log the specific exception
                                        Exception exception = task.getException();
                                        if (exception != null) {
                                            String errorMessage = exception.getMessage();
                                            Log.e("SignUpError", "Sign up failed: " + errorMessage);
                                            Toast.makeText(SignUpActivity.this, "Sign Up Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                                        } else {
                                            Log.e("SignUpError", "Sign up failed: Unknown error");
                                            Toast.makeText(SignUpActivity.this, "Sign Up Failed: Unknown error", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }).addOnFailureListener(e -> {
                                // General failure listener
                                Log.e("SignUpError", "Authentication failed: " + e.getMessage());
                                Toast.makeText(SignUpActivity.this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });
                }
            }
        });

        // Redirect to Login Activity when "Already have an account?" is clicked
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
