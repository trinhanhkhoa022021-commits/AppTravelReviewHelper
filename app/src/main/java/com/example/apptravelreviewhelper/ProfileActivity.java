package com.example.apptravelreviewhelper;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private TextView btnBack;
    private EditText edtEmail, edtPhone, edtDob;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1. Ánh xạ giao diện
        btnBack = findViewById(R.id.btnBack);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtDob = findViewById(R.id.edtDob);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);

        // 2. Lấy Email hiện tại hiển thị lên (không cho sửa)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            edtEmail.setText(currentUser.getEmail());
        }

        // 3. Sự kiện bấm nút Quay lại
        btnBack.setOnClickListener(v -> {
            finish(); // Đóng trang này, tự động quay về trang Tài khoản
        });

        // 4. Sự kiện bấm nút Lưu thông tin
        btnSaveProfile.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString();
            String dob = edtDob.getText().toString();

            if (phone.isEmpty() || dob.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Tạm thời báo thành công (Sau này sẽ đưa dữ liệu này lên Firestore lưu lại)
                Toast.makeText(this, "Đã lưu thông tin thành công!", Toast.LENGTH_SHORT).show();
                finish(); // Lưu xong tự quay về
            }
        });
    }
}