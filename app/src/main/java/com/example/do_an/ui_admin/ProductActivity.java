package com.example.do_an.ui_admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private EditText searchBox;  // Search EditText
    private RecyclerView recyclerView;  // RecyclerView for displaying products
    private ProductAdapter productAdapter;
    private List<Product> productList;  // List of all products
    private List<Product> filteredList;  // List for filtered products based on search query

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the product lists
        productList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Initialize the search box
        searchBox = findViewById(R.id.searchBox);

        // Initialize ProductAdapter with filteredList
        productAdapter = new ProductAdapter(this, filteredList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onDeleteClick(Product product) {
                // Handle delete action (you can add your delete logic here)
                Toast.makeText(ProductActivity.this, "Product deleted: " + product.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set the adapter to RecyclerView
        recyclerView.setAdapter(productAdapter);

        // Fetch all products and update filteredList
        fetchProducts();

        // Set up the search functionality
        setupSearch();

        FloatingActionButton buttonAddProduct = findViewById(R.id.button_AddProduct);

        buttonAddProduct.setOnClickListener(view -> {
            Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_book);

        // Using the OnItemSelectedListener correctly
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_category) {
                    startActivity(new Intent(getApplicationContext(), CategoryActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_book) {
                    return true;
                } else if (itemId == R.id.bottom_order) {
                    startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_setting) {
                    startActivity(new Intent(getApplicationContext(), Setting.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return false;
            }
        });


    }

    // Method to fetch all products from Firestore
    private void fetchProducts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch products from Firestore (assuming collection name is "products")
        db.collection("products")
                .get()  // Get all products in the collection
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the filteredList and productList to avoid duplicates
                        productList.clear();
                        filteredList.clear();

                        // Iterate over the documents returned from Firestore
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Map Firestore document to Product object
                            Product product = document.toObject(Product.class);
                            productList.add(product);  // Add product to the list

                            // Add the product to the filtered list initially (before search filtering)
                            filteredList.add(product);
                        }

                        // Notify adapter that data has changed and needs to be displayed
                        productAdapter.notifyDataSetChanged();
                    } else {
                        // Log the error in case of failure
                        Toast.makeText(ProductActivity.this, "Error getting products: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Setup the search functionality
    private void setupSearch() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());  // Filter products as the user types
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Filter products based on the search query
    private void filterProducts(String query) {
        filteredList.clear();  // Clear the filtered list before adding new results

        if (query.isEmpty()) {
            filteredList.addAll(productList);  // If search is empty, show all products
        } else {
            for (Product product : productList) {
                // Check if the product name contains the query, case insensitive
                if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(product);  // Add matching products to filtered list
                }
            }
        }

        // Notify the adapter that the data has changed
        productAdapter.notifyDataSetChanged();
    }
}
