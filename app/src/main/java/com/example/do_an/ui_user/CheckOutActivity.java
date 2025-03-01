package com.example.do_an.ui_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_an.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerView);
        tvTotalPrice = findViewById(R.id.total_price);
        paymentMethodSpinner = findViewById(R.id.payment_method_spinner);
        confirmOrderButton = findViewById(R.id.confirm_order_button);
        btnBack = findViewById(R.id.btnBack);
        deliveryAddress = findViewById(R.id.delivery_address);

        // Quay lại màn hình trước
        btnBack.setOnClickListener(v -> finish());

        // Thiết lập RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItems, tvTotalPrice);
        recyclerView.setAdapter(cartAdapter);

        // Lấy dữ liệu giỏ hàng từ Firebase
        fetchCartItems();

        // Xác nhận đặt hàng
        confirmOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void fetchCartItems() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userID);

        cartRef.addValueEventListener(new ValueEventListener() {
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
                // Xử lý lỗi nếu có
            }
        });
    }

    private void placeOrder() {
        String selectedPaymentMethod = paymentMethodSpinner.getSelectedItem().toString();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("orders").child(userID);

        // Lưu đơn hàng vào Firebase
        DatabaseReference newOrderRef = orderRef.push();
        newOrderRef.child("items").setValue(cartItems);
        newOrderRef.child("total_price").setValue(tvTotalPrice.getText().toString());
        newOrderRef.child("payment_method").setValue(selectedPaymentMethod);
        newOrderRef.child("address").setValue(deliveryAddress.getText().toString());

        // Xóa giỏ hàng sau khi đặt hàng thành công
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userID);
        cartRef.removeValue();

        // Hiển thị thông báo đặt hàng thành công
        Intent intent = new Intent(CheckOutActivity.this, OrderSuccessActivity.class);
        startActivity(intent);
        finish();
    }
}
