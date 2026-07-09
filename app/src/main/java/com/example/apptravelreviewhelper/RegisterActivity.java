package com.example.apptravelreviewhelper;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtRegEmail, edtRegPassword, edtRegConfirmPassword;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ giao diện
        edtRegEmail = findViewById(R.id.edtRegEmail);
        edtRegPassword = findViewById(R.id.edtRegPassword);
        edtRegConfirmPassword = findViewById(R.id.edtRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        // Dùng HTML để bôi đậm và đổi màu xanh lá chữ "Quay lại Đăng nhập"
        tvBackToLogin.setText(android.text.Html.fromHtml("Đã có tài khoản? <b><font color='#4CAF50'>Quay lại Đăng nhập</font></b>", android.text.Html.FROM_HTML_MODE_LEGACY));

        // Bấm nút Đăng ký
        btnRegister.setOnClickListener(v -> registerUser());

        // Bấm chữ Quay lại Đăng nhập
        tvBackToLogin.setOnClickListener(v -> finish()); // Đóng màn hình này sẽ tự lùi về màn hình Đăng nhập
    }

    private void registerUser() {
        String email = edtRegEmail.getText().toString().trim();
        String password = edtRegPassword.getText().toString().trim();
        String confirmPassword = edtRegConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi hàm tạo tài khoản của Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        finish(); // Tạo xong thì đóng màn hình này để quay về Đăng nhập
                    } else {
                        Toast.makeText(RegisterActivity.this, "Lỗi đăng ký: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}