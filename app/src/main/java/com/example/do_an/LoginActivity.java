package com.example.do_an;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView signUpRedirectText, forgotPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find Views
        loginEmail = findViewById(R.id.input_signup_email);
        loginPassword = findViewById(R.id.input_Password);
        loginButton = findViewById(R.id.login);
        signUpRedirectText = findViewById(R.id.textView6);
        forgotPasswordText = findViewById(R.id.textView8);

        // Login Button OnClickListener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                // Validate email and password input
                if (email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty");
                } else if (password.isEmpty()) {
                    loginPassword.setError("Password cannot be empty");
                } else {
                    // Firebase authentication login
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    // Login success, get current user
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        // Retrieve user data from Realtime Database
                                        retrieveUserData(user.getUid());
                                    }
                                } else {
                                    // Login failed, show error message
                                    Toast.makeText(LoginActivity.this, "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        // Redirect to SignUp Activity when "Create a new account" text is clicked
        signUpRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();  // Close LoginActivity
            }
        });

        // Redirect to ForgotPassword Activity when "Forgot Password?" text is clicked
        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
    }

    // Retrieve user data from Firebase Realtime Database using userId (UID)
    private void retrieveUserData(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Get user data from Firebase Realtime Database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the user information (name, email, username) from the snapshot
                    HelperClass user = dataSnapshot.getValue(HelperClass.class);
                    if (user != null) {
                        // Successfully retrieved user data from database
                        // Here you can use user data for personalization or display
                        Toast.makeText(LoginActivity.this, "Welcome, " + user.getName(), Toast.LENGTH_SHORT).show();
                    }
                    // Navigate to the main screen (MainActivity) after successful login
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();  // Close LoginActivity
                } else {
                    // If the user data doesn't exist in the database, show an error message
                    Toast.makeText(LoginActivity.this, "No user data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the retrieval process
                Toast.makeText(LoginActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
