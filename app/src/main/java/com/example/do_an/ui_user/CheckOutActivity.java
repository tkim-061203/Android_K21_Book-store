package com.example.do_an.ui_user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.do_an.R;
import com.example.do_an.ui_admin.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CheckOutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tvTotalPrice, deliveryAddress;
    private Spinner paymentMethodSpinner;
    private Button confirmOrderButton;
    private ImageButton btnBack;
    private ArrayList<CartItem> cartItems;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        // Ánh xạ UI
        recyclerView = findViewById(R.id.recyclerView);
        tvTotalPrice = findViewById(R.id.total_price);
        paymentMethodSpinner = findViewById(R.id.payment_method_spinner);
        confirmOrderButton = findViewById(R.id.confirm_order_button);
        btnBack = findViewById(R.id.btnBack);
        deliveryAddress = findViewById(R.id.delivery_address);

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());

        // Cấu hình RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItems, tvTotalPrice);
        recyclerView.setAdapter(cartAdapter);

        // Thiết lập phương thức thanh toán
        setupPaymentMethodSpinner();

        // Lấy giỏ hàng từ Firebase
        fetchCartItems();

        // Lấy địa chỉ giao hàng từ Intent hoặc Firebase
        String address = getIntent().getStringExtra("ADDRESS");
        if (address != null && !address.isEmpty()) {
            deliveryAddress.setText(address);
        } else {
            fetchUserAddress();
        }

        // Xử lý nút đặt hàng
        confirmOrderButton.setOnClickListener(v -> placeOrder());

        deliveryAddress.setOnClickListener(v -> {
            Intent intent = new Intent(CheckOutActivity.this, Add_Address.class);
            startActivity(intent);
        });

    }

    // Thiết lập danh sách phương thức thanh toán trong Spinner
    private void setupPaymentMethodSpinner() {
        String[] paymentMethods = {"Cash on Delivery", "Credit Card", "Momo", "Bank Transfer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);
    }

    // Lấy giỏ hàng từ Firebase
    private void fetchCartItems() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userID);

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                double totalPrice = 0;

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartItem item = itemSnapshot.getValue(CartItem.class);
                    if (item != null) {
                        cartItems.add(item);
                        totalPrice += item.getPrice() * item.getQuantity();
                    }
                }

                cartAdapter.notifyDataSetChanged();
                tvTotalPrice.setText(String.format("Total: %,.2f VND", totalPrice));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CheckOutActivity", "Error fetching cart items", error.toException());
            }
        });
    }

    // Lấy địa chỉ từ Firebase nếu không có trong Intent
    private void fetchUserAddress() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userID).child("address");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String savedAddress = snapshot.getValue(String.class);
                    if (savedAddress != null && !savedAddress.isEmpty()) {
                        deliveryAddress.setText(savedAddress);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CheckOutActivity", "Failed to fetch address", error.toException());
            }
        });
    }

    // Xử lý đặt hàng
    private void placeOrder() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(userID);

        String orderID = orderRef.push().getKey();
        if (orderID == null) {
            Log.e("CheckOutActivity", "Failed to generate order ID");
            return;
        }

        // Kiểm tra xem dữ liệu có bị null không
        if (tvTotalPrice == null || tvTotalPrice.getText() == null) {
            Log.e("CheckOutActivity", "tvTotalPrice is null");
            return;
        }
        if (paymentMethodSpinner == null || paymentMethodSpinner.getSelectedItem() == null) {
            Log.e("CheckOutActivity", "paymentMethodSpinner is null");
            return;
        }
        if (deliveryAddress == null || deliveryAddress.getText() == null) {
            Log.e("CheckOutActivity", "deliveryAddress is null");
            return;
        }

        // Lấy ngày giờ hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String orderDate = sdf.format(new Date());

        // Tạo đối tượng đơn hàng
        Order order = new Order(orderID, userID, cartItems,
                tvTotalPrice.getText().toString(),
                paymentMethodSpinner.getSelectedItem().toString(),
                deliveryAddress.getText().toString(),
                orderDate, "Onboard");

        // Lưu đơn hàng vào Firebase
        orderRef.child(orderID).setValue(order).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase.getInstance().getReference("carts").child(userID).removeValue();
                Intent intent = new Intent(CheckOutActivity.this, OrderSuccessActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.e("CheckOutActivity", "Failed to place order", task.getException());
            }
        });
    }
}
