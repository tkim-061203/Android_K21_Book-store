package com.example.do_an.ui_admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.do_an.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private DatabaseReference orderRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList);
        recyclerView.setAdapter(orderAdapter);

        loadOrdersFromFirebase();

        setupBottomNavigation();
    }
    private void loadOrdersFromFirebase() {
        DatabaseReference allOrdersRef = FirebaseDatabase.getInstance().getReference("orders");

        allOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FirebaseDebug", "Dữ liệu toàn bộ orders: " + snapshot.toString());

                orderList.clear();
                if (!snapshot.exists()) {
                    Log.e("FirebaseError", "Không có đơn hàng nào trong hệ thống!");
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) { // Lặp qua từng user
                    for (DataSnapshot orderSnapshot : userSnapshot.getChildren()) { // Lặp qua từng đơn hàng
                        Order order = orderSnapshot.getValue(Order.class);
                        if (order != null) {
                            orderList.add(order);
                        } else {
                            Log.e("FirebaseError", "Không thể parse dữ liệu Order từ: " + orderSnapshot.toString());
                        }
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Lỗi khi tải dữ liệu: " + error.getMessage());
            }
        });
    }



    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_order);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_category) {
                    navigateTo(CategoryActivity.class);
                    return true;
                } else if (itemId == R.id.bottom_book) {
                    navigateTo(ProductActivity.class);
                    return true;
                } else if (itemId == R.id.bottom_order) {
                    return true;
                } else if (itemId == R.id.bottom_setting) {
                    navigateTo(Setting.class);
                    return true;
                }
                return false;
            }
        });
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(getApplicationContext(), targetActivity));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
