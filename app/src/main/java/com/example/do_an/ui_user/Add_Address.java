package com.example.do_an.ui_user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.do_an.R;

public class Add_Address extends AppCompatActivity {
    private EditText etAddress;
    private Button btnUpdate;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        etAddress = findViewById(R.id.et_address);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);

        // Xử lý khi nhấn nút Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Quay lại màn hình trước đó
            }
        });

        // Xử lý khi nhấn nút Add
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = etAddress.getText().toString().trim();

                if (address.isEmpty()) {
                    Toast.makeText(Add_Address.this, "Vui lòng nhập địa chỉ!", Toast.LENGTH_SHORT).show();
                } else {
                    // Chuyển sang CheckoutActivity và truyền địa chỉ
                    Intent intent = new Intent(Add_Address.this, CheckOutActivity.class);
                    intent.putExtra("ADDRESS", address);
                    startActivity(intent);
                }
            }
        });
    }}