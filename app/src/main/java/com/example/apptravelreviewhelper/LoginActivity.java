package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ giao diện
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        // Dùng HTML để bôi đậm và đổi màu xanh chữ "Đăng ký ngay"
        tvRegister.setText(android.text.Html.fromHtml("Chưa có tài khoản? <b><font color='#2196F3'>Đăng ký ngay</font></b>", android.text.Html.FROM_HTML_MODE_LEGACY));

        // Bấm nút Đăng nhập
        btnLogin.setOnClickListener(v -> loginUser());

        // Bấm chữ Đăng ký

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Email và Mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi hàm đăng nhập của Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, chuyển sang màn hình chính (MainActivity)
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish(); // Đóng màn hình đăng nhập
                    } else {
                        // Đăng nhập thất bại (sai pass hoặc tài khoản ko tồn tại)
                        Toast.makeText(LoginActivity.this, "Lỗi: Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}