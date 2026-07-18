package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BillActivity extends AppCompatActivity {

    private TextView tvBillLocation, tvBillCheckIn, tvBillNights, tvBillAdults;
    private TextView tvBillRoomType, tvBillRoomCount, tvBillPaymentMethod, tvBillTotalPrice;
    private Button btnBackToHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        // 1. Ánh xạ View
        tvBillLocation = findViewById(R.id.tvBillLocation);
        tvBillCheckIn = findViewById(R.id.tvBillCheckIn);
        tvBillNights = findViewById(R.id.tvBillNights);
        tvBillAdults = findViewById(R.id.tvBillAdults);
        tvBillRoomType = findViewById(R.id.tvBillRoomType);
        tvBillRoomCount = findViewById(R.id.tvBillRoomCount);
        tvBillPaymentMethod = findViewById(R.id.tvBillPaymentMethod);
        tvBillTotalPrice = findViewById(R.id.tvBillTotalPrice);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        // 2. Nhận dữ liệu từ BookingActivity
        Intent intent = getIntent();
        if (intent != null) {
            tvBillLocation.setText(intent.getStringExtra("BILL_LOCATION"));
            tvBillCheckIn.setText(intent.getStringExtra("BILL_CHECK_IN"));
            tvBillNights.setText(intent.getIntExtra("BILL_NIGHTS", 1) + " đêm");
            tvBillAdults.setText(intent.getIntExtra("BILL_ADULTS", 1) + " người lớn");
            tvBillRoomType.setText(intent.getStringExtra("BILL_ROOM_TYPE"));
            tvBillRoomCount.setText(intent.getIntExtra("BILL_ROOM_COUNT", 1) + " phòng");
            tvBillPaymentMethod.setText(intent.getStringExtra("BILL_PAYMENT_METHOD"));
            tvBillTotalPrice.setText(intent.getStringExtra("BILL_TOTAL_PRICE"));
        }

        // 3. Xử lý nút "Về trang chủ"
        btnBackToHome.setOnClickListener(v -> {
            // Chuyển người dùng về MainActivity và xóa hết các trang trung gian đi
            Intent homeIntent = new Intent(BillActivity.this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        });
    }
}