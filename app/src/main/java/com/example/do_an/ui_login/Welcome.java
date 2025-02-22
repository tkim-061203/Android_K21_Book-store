package com.example.do_an.ui_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.R;

public class Welcome extends AppCompatActivity {
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view first
        setContentView(R.layout.activity_welcome);

        // Now the layout is inflated, we can safely access the button
        login = findViewById(R.id.welcome_login);

        // Set the onClickListener
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this, LoginActivity.class));
                finish(); // Finish current activity to prevent going back
            }
        });
    }
}
