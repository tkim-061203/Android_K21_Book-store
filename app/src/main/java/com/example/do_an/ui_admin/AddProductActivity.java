package com.example.do_an.ui_admin;

import android.os.Bundle;
import android.text.TextUtils;
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

public class AddProductActivity extends AppCompatActivity {

    private EditText etNameProduct, etPriceProduct, etImageUrl, etDescription, etAuthor;
    private Spinner spinnerCategory;
    private Button btnAddProduct;
    private FirebaseFirestore db;
    private ArrayAdapter<String> categoryAdapter;
    private List<String> categoryList;  // List to store categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        categoryList = new ArrayList<>();

        // Initialize UI components
        etNameProduct = findViewById(R.id.etNameProduct);
        etPriceProduct = findViewById(R.id.etPriceProduct);
        etImageUrl = findViewById(R.id.etImageUrl);
        etDescription = findViewById(R.id.etSummaryContent);
        etAuthor = findViewById(R.id.etNameAuthor);
        spinnerCategory = findViewById(R.id.spinnerCategory_Product);
        btnAddProduct = findViewById(R.id.btnUpdate);

        // Set up category spinner
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Handle back button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Handle add product button
        btnAddProduct.setOnClickListener(view -> addProductToFirestore());

        // Load categories from Firestore
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
                        Toast.makeText(AddProductActivity.this, "Error loading categories!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to add a new product to Firestore
    private void addProductToFirestore() {
        // Get input values
        String name = etNameProduct.getText().toString().trim();
        String priceStr = etPriceProduct.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String author = etAuthor.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        // Validate input fields
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(description) || TextUtils.isEmpty(author)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse price as a double
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to hold the product data
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", name);
        productData.put("author", author);
        productData.put("price", price);
        productData.put("category", category);
        productData.put("imageUrl", imageUrl);
        productData.put("description", description);

        // Add product data to Firestore
        db.collection("products")
                .add(productData)  // Add a new document with auto-generated ID
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddProductActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                    finish();  // Close the activity after adding
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
