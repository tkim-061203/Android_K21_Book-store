package com.example.do_an.ui_user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.do_an.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.do_an.ui_user.CartItem;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private ArrayList<CartItem> cartItems;
    private TextView tvTotalPrice;

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, TextView tvTotalPrice) {
        this.context = context;
        this.cartItems = cartItems;
        this.tvTotalPrice = tvTotalPrice;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvBookTitle.setText(item.getName());
        holder.tvBookPrice.setText(String.format("%,.0f VND", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(context).load(item.getImageUrl()).into(holder.ivBookImage);

        // Xử lý tăng số lượng sách
        holder.btnIncrease.setOnClickListener(v -> updateQuantity(item, item.getQuantity() + 1));

        // Xử lý giảm số lượng sách
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                updateQuantity(item, item.getQuantity() - 1);
            }
        });

        // Xử lý xóa sách khỏi giỏ hàng
        holder.btnDelete.setOnClickListener(v -> removeFromCart(item));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // Cập nhật số lượng sách và tổng giá trị
    private void updateQuantity(CartItem item, int newQuantity) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userID);
        cartRef.child(item.getId()).child("quantity").setValue(newQuantity)
                .addOnSuccessListener(aVoid -> fetchCartItems(context, tvTotalPrice)); // Cập nhật tổng giá
    }

    // Xóa sản phẩm khỏi giỏ hàng
    private void removeFromCart(CartItem item) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userID);
        cartRef.child(item.getId()).removeValue()
                .addOnSuccessListener(aVoid -> fetchCartItems(context, tvTotalPrice)); // Cập nhật tổng giá
    }

    // Hàm lấy danh sách giỏ hàng và cập nhật tổng giá
    public static void fetchCartItems(final Context context, final TextView tvTotalPrice) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userID);

        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartItem> cartList = new ArrayList<>();
                double totalPrice = 0;

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    CartItem item = itemSnapshot.getValue(CartItem.class);
                    cartList.add(item);
                    totalPrice += item.getPrice() * item.getQuantity();
                }

                RecyclerView recyclerView = ((CartActivity) context).findViewById(R.id.recyclerCart);
                recyclerView.setAdapter(new CartAdapter(context, cartList, tvTotalPrice));

                // Cập nhật tổng giá tiền trong CartActivity
                tvTotalPrice.setText(String.format("Total: %,.0f VND", totalPrice));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvBookPrice, tvQuantity;
        ImageView ivBookImage, btnDelete;
        ImageButton btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvTitle);
            tvBookPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ivBookImage = itemView.findViewById(R.id.imgBook);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
