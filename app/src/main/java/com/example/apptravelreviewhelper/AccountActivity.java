package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private TextView tvGreeting;
    private Button btnLogout;
    private CardView btnPersonalInfo;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // 1. Ánh xạ ID
        tvGreeting = findViewById(R.id.tvGreeting);
        btnLogout = findViewById(R.id.btnLogout);
        btnPersonalInfo = findViewById(R.id.btnPersonalInfo);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // 2. Lấy Email và Cắt tên làm Lời chào
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            String email = currentUser.getEmail();
            // Lấy toàn bộ chữ đứng trước dấu @
            String name = email.substring(0, email.indexOf("@"));
            tvGreeting.setText("CHÀO " + name);
        }


        // 3. Sự kiện bấm vào mục Thông tin cá nhân
        btnPersonalInfo.setOnClickListener(v -> {
            // Chuyển sang trang ProfileActivity
            Intent intent = new Intent(AccountActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        // 4. Sự kiện Đăng xuất
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // 5. Cấu hình Thanh Menu dưới cùng
        bottomNavigationView.setSelectedItemId(R.id.nav_account); // Chọn sẵn tab Tài khoản

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_hot) {
                // Quay về trang Điểm Hot (MainActivity)
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish(); // Đóng trang Tài khoản lại
                return false;
            } else if (itemId == R.id.nav_search) {
                Toast.makeText(this, "Sắp ra mắt: Trang Tìm kiếm", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_saved) {
                Toast.makeText(this, "Sắp ra mắt: Trang Đã lưu", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_booking) {
                Toast.makeText(this, "Sắp ra mắt: Trang Đặt chỗ", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_account) {
                return true; // Đang ở tab này rồi, không làm gì cả
            }
            return false;
        });
    }
}