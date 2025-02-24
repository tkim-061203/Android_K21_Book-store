package com.example.do_an.ui_admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.do_an.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText etCategoryName;
    private Button btnAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Reference UI elements
        etCategoryName = findViewById(R.id.etCategoryName);
        btnAddCategory = findViewById(R.id.btnUpdate);

        // Handle button click
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryToFirestore();
            }
        });
    }

    private void addCategoryToFirestore() {
        String categoryName = etCategoryName.getText().toString().trim();

        if (categoryName.isEmpty()) {
            Toast.makeText(this, "Category name cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new category
        Map<String, Object> category = new HashMap<>();
        category.put("name", categoryName);

        // Add to Firestore
        db.collection("categories")
                .add(category)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Category added successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close AddCategoryActivity
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

}