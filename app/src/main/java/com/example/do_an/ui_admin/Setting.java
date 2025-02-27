package com.example.do_an.ui_admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.do_an.R;
import com.example.do_an.ui_login.LoginActivity;
import com.example.do_an.ui_user.Book;
import com.example.do_an.ui_user.BookAdapter;
import com.example.do_an.ui_user.ChangePasswordActivity;
import com.example.do_an.ui_user.EditProfile;
import com.example.do_an.ui_user.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Setting extends AppCompatActivity {
    private TextView adminEmailTextView;
    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private Button logoutButton;
    private FirebaseAuth mAuth;
    private Button btnEditProfile, btnChangePassword, btnLogout;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        // Ánh xạ UI
        profileImage = findViewById(R.id.profile_image);
        profileName = findViewById(R.id.profile_name);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnLogout = findViewById(R.id.btn_logout);

        // Cập nhật thông tin người dùng (Dữ liệu mẫu)
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        adminEmailTextView = findViewById(R.id.profile_email);
        logoutButton = findViewById(R.id.btn_logout);

        if (currentUser != null) {
            String email = currentUser.getEmail();
            adminEmailTextView.setText(email);
        } else {
            adminEmailTextView.setText("Chưa đăng nhập");
        }

        // Xử lý khi nhấn vào nút "Chỉnh sửa hồ sơ"
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, EditProfile.class);
            startActivityForResult(intent, 2);
        });

        // Xử lý khi nhấn vào nút "Đổi mật khẩu"
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, ChangePasswordActivity.class);
            startActivity(intent);
        });


        // Xử lý khi nhấn vào nút "Đăng xuất"
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(Setting.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_setting);

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
                    startActivity(new Intent(getApplicationContext(), ProductActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_order) {
                    startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                } else if (itemId == R.id.bottom_setting) {
                    return true;
                }
                return false;
            }
        });
    }
}
