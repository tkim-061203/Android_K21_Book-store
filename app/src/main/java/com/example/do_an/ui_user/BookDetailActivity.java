package com.example.do_an.ui_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.do_an.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookDetailActivity extends AppCompatActivity {

    private ImageView bookImage;
    private TextView bookTitle, bookAuthor, bookPrice, bookCategory, bookDescription;
    private Button btnBuy;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        // Ánh xạ UI
        bookImage = findViewById(R.id.bookImage);
        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookPrice = findViewById(R.id.bookPrice);
        bookCategory = findViewById(R.id.bookCategory);
        bookDescription = findViewById(R.id.bookDescription);
        btnBuy = findViewById(R.id.btnBuy);
        btnBack = findViewById(R.id.btnBack);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String bookId = intent.getStringExtra("bookId");
            String name = intent.getStringExtra("bookName");
            String author = intent.getStringExtra("bookAuthor");
            String price = intent.getStringExtra("bookPrice");
            String imageUrl = intent.getStringExtra("bookImageUrl");
            String category = intent.getStringExtra("bookCategory");
            String description = intent.getStringExtra("bookDescription");

            // Gán dữ liệu vào UI
            bookTitle.setText(name);
            bookAuthor.setText(author);
            bookPrice.setText(price);
            bookCategory.setText(category != null ? category : "Unknown");
            bookDescription.setText(description != null ? description : "No description available");

            // Load ảnh từ URL với Glide
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_image_24) // Placeholder nếu ảnh chưa tải xong
                    .error(R.drawable.baseline_image_24) // Ảnh lỗi nếu không tải được
                    .into(bookImage);
        }

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý nút "Thêm vào giỏ hàng"
        btnBuy.setOnClickListener(v -> {
            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userID);

            String bookId = intent.getStringExtra("bookId");
            String name = intent.getStringExtra("bookName");
            String price = intent.getStringExtra("bookPrice");
            String imageUrl = intent.getStringExtra("bookImageUrl");

            CartItem cartItem = new CartItem(bookId, name, imageUrl, Double.parseDouble(price.replace(" VND", "").replace(",", "")), 1);

            cartRef.child(bookId).setValue(cartItem)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(BookDetailActivity.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(BookDetailActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        }
    }