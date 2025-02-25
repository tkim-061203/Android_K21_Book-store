package com.example.do_an.ui_admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateProductActivity extends AppCompatActivity {

    private EditText etNameProduct, etNameAuthor, etPriceProduct, etImageUrl, etDescription;
    private Spinner spinnerCategoryProduct;
    private Button btnUpdate;
    private Spinner spinnerCategory;
    private List<String> categoryList = new ArrayList<>();
    private ArrayAdapter<String> categoryAdapter;
    private FirebaseFirestore db;
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        spinnerCategory = findViewById(R.id.spinnerCategory_Product);
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get the product data passed from the previous activity
        productId = getIntent().getStringExtra("product_id");
        String productName = getIntent().getStringExtra("product_name");
        String author = getIntent().getStringExtra("product_author");
        double price = getIntent().getDoubleExtra("product_price", 0);
        String imageUrl = getIntent().getStringExtra("product_imageUrl");
        String description = getIntent().getStringExtra("product_description");

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Initialize the views
        etNameProduct = findViewById(R.id.etNameProduct);
        etNameAuthor = findViewById(R.id.etNameAuthor);
        etPriceProduct = findViewById(R.id.etPriceProduct);
        etImageUrl = findViewById(R.id.etImageUrl);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategoryProduct = findViewById(R.id.spinnerCategory_Product);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Pre-fill the fields with the existing product data
        etNameProduct.setText(productName);
        etNameAuthor.setText(author);
        etPriceProduct.setText(String.valueOf(price));
        etImageUrl.setText(imageUrl);
        etDescription.setText(description);

        // Handle back button click
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Handle update button click
        btnUpdate.setOnClickListener(view -> updateProduct());
        loadCategoriesFromFirestore();
    }

    // Method to load categories from Firestore
    private void loadCategoriesFromFirestore() {
        db.collection("categories")  // Assuming the categories are stored in the "categories" collection
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        categoryList.clear();  // Clear existing categories
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String categoryName = document.getString("name");
                            if (categoryName != null) {
                                categoryList.add(categoryName);  // Add category to the list
                            }
                        }
                        categoryAdapter.notifyDataSetChanged();  // Notify the adapter to update the spinner
                    } else {
                        Toast.makeText(UpdateProductActivity.this, "Error loading categories!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateProduct() {
        // Get updated values from the EditText fields
        String updatedName = etNameProduct.getText().toString();
        String updatedAuthor = etNameAuthor.getText().toString();
        String priceText = etPriceProduct.getText().toString();
        String updatedImageUrl = etImageUrl.getText().toString();
        String updatedCategory = spinnerCategoryProduct.getSelectedItem().toString();
        String updatedDescription = etDescription.getText().toString();

        if (updatedName.isEmpty() || updatedAuthor.isEmpty() || priceText.isEmpty() || updatedCategory.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double updatedPrice;
        try {
            updatedPrice = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map with updated values (using constructor to create the Product object)
        Map<String, Object> updatedProduct = new HashMap<>();
        updatedProduct.put("name", updatedName);
        updatedProduct.put("author", updatedAuthor);
        updatedProduct.put("price", updatedPrice);
        updatedProduct.put("category", updatedCategory);
        updatedProduct.put("imageUrl", updatedImageUrl);
        updatedProduct.put("description", updatedDescription);

        // Update Firestore with the new data
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("products").document(productId)
                .set(updatedProduct)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateProductActivity.this, "Product updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity and return to the previous screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateProductActivity.this, "Error updating product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
