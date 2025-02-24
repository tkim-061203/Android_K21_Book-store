package com.example.do_an.ui_login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.ui_admin.CategoryActivity;
import com.example.do_an.ui_user.MainActivity;
import com.example.do_an.R;
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
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        // Find Views
        loginEmail = findViewById(R.id.input_signup_email);
        loginPassword = findViewById(R.id.input_Password);
        loginButton = findViewById(R.id.login);
        signUpRedirectText = findViewById(R.id.textView6);
        forgotPasswordText = findViewById(R.id.textView8);
        CheckBox showPasswordCheckBox = findViewById(R.id.showPasswordCheckBox);
        // Login Button OnClickListener

        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            loginPassword.setSelection(loginPassword.getText().length());
        });

        loginButton.setOnClickListener(view -> {
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
        });

        // Redirect to SignUp Activity when "Create a new account" text is clicked
        signUpRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            finish();  // Close LoginActivity
        });

        // Redirect to ForgotPassword Activity when "Forgot Password?" text is clicked
        forgotPasswordText.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }

    // Retrieve user data from Firebase Realtime Database using userId (UID)
    private void retrieveUserData(String userId) {
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy dữ liệu người dùng từ Firebase
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HelperClass user = dataSnapshot.getValue(HelperClass.class);

                    if (user != null) {
                        String role = user.getRole(); // Lấy role từ Firebase

                        if ("admin".equals(role)) {
                            Toast.makeText(LoginActivity.this, "Welcome, Admin " + user.getName(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, CategoryActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Welcome, " + user.getName(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        }
                        finish(); // Đóng LoginActivity sau khi đăng nhập thành công
                    } else {
                        Toast.makeText(LoginActivity.this, "User data is corrupted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "No user data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();  // Close LoginActivity
    }
}
