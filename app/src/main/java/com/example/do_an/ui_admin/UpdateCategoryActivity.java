package com.example.do_an.ui_admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.do_an.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateCategoryActivity extends AppCompatActivity {

    private EditText etCategoryName;  // EditText to update the category name
    private String categoryId;  // Store the category ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Initialize the EditText view
        etCategoryName = findViewById(R.id.etCategoryName);

        // Get the category data passed via the intent
        categoryId = getIntent().getStringExtra("category_id");
        String categoryName = getIntent().getStringExtra("category_name");

        // Set the EditText with the current category name
        if (categoryName != null) {
            etCategoryName.setText(categoryName);
        }
        // Update logic (e.g., when the user presses a save button)
        Button btnUpdateCategory = findViewById(R.id.btnUpdate);
        btnUpdateCategory.setOnClickListener(view -> updateCategory());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open CategoryActivity
                Intent intent = new Intent(UpdateCategoryActivity.this, CategoryActivity.class);
                startActivity(intent);
                finish(); // Close current activity
            }
        });

    }

    private void updateCategory() {
        // Get the updated category name
        String updatedCategoryName = etCategoryName.getText().toString();

        if (updatedCategoryName.isEmpty()) {
            Toast.makeText(this, "Please provide a valid category name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the category in Firestore (assuming you have a Firestore instance and categories collection)
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories")
                .document(categoryId)  // Use the category ID to update the correct document
                .update("name", updatedCategoryName)  // Update the category name
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateCategoryActivity.this, "Category updated successfully", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity and return to the previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateCategoryActivity.this, "Error updating category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
