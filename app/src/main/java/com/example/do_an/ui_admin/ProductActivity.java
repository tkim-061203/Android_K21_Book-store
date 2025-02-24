package com.example.do_an.ui_admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.do_an.R;
import com.example.do_an.ui_admin.ProductAdapter;
import com.example.do_an.ui_admin.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product);

        // FloatingActionButton để thêm sản phẩm
        FloatingActionButton buttonAddProduct = findViewById(R.id.button_AddProduct);
        buttonAddProduct.setOnClickListener(view -> {
            Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
            startActivity(intent);
        });

        // Kết nối Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Cấu hình RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onEditClick(Product product) {
                // Xử lý chỉnh sửa sản phẩm
                Toast.makeText(ProductActivity.this, "Edit: " + product.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Product product) {
                // Xử lý xóa sản phẩm khỏi Firestore
                deleteProductFromFirestore(product);
            }
        });

        recyclerView.setAdapter(productAdapter);

        // Lấy dữ liệu sản phẩm từ Firestore
        fetchProductsFromFirestore();

        // Cấu hình BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_book);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    private void fetchProductsFromFirestore() {
        db.collection("products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(ProductActivity.this, "Error fetching products!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        productList.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Product product = doc.toObject(Product.class);
                            product.setId(doc.getId()); // Gán ID Firestore
                            productList.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void deleteProductFromFirestore(Product product) {
        db.collection("products").document(product.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ProductActivity.this, "Delete complete the product", Toast.LENGTH_SHORT).show();
                    productList.remove(product);
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(ProductActivity.this, "Error detele product", Toast.LENGTH_SHORT).show());
    }
}
