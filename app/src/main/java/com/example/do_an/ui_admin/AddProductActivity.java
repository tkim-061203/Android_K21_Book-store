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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_an.R;
import com.example.do_an.ui_admin.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    private EditText etNameProduct, etPriceProduct, etImageUrl;
    private Spinner spinnerCategory;
    private Button btnUpdate;
    private FirebaseFirestore db;
    private List<String> categoryList;
    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);

        // Initialize UI components
        etNameProduct = findViewById(R.id.etNameProduct);
        etPriceProduct = findViewById(R.id.etPriceProduct);
        etImageUrl = findViewById(R.id.etImageUrl);
        spinnerCategory = findViewById(R.id.spinnerCategory_Product);
        btnUpdate = findViewById(R.id.btnUpdate);
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        categoryList = new ArrayList<>();

        // Set up category spinner
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Load categories from Firestore
        loadCategoriesFromFirestore();

        // Handle back button
        btnBack.setOnClickListener(v -> finish());

        // Handle adding product
        btnUpdate.setOnClickListener(view -> addProductToFirestore());
    }

    private void loadCategoriesFromFirestore() {
        db.collection("categories")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        categoryList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String categoryName = document.getString("name");
                            if (categoryName != null) {
                                categoryList.add(categoryName);
                            }
                        }
                        categoryAdapter.notifyDataSetChanged();  // Refresh spinner with new data
                    } else {
                        Toast.makeText(AddProductActivity.this, "Error loading categories!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addProductToFirestore() {
        String name = etNameProduct.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String priceStr = etPriceProduct.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(category) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, "Please enter all required information!", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> productData = new HashMap<>();
        productData.put("name", name);
        productData.put("category", category);
        productData.put("price", price);
        productData.put("imageUrl", imageUrl);
        productData.put("timestamp", System.currentTimeMillis());

        db.collection("products")
                .add(productData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddProductActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(AddProductActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
