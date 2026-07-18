package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.EditText;

public class BookingActivity extends AppCompatActivity {

    private String currentLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        TextView tvBookingLocation = (TextView) findViewById(R.id.tvBookingLocation);
        final TextInputEditText etTravelDate = (TextInputEditText) findViewById(R.id.etTravelDate);
        final TextInputEditText etNumberOfPeople = (TextInputEditText) findViewById(R.id.etNumberOfPeople);
        Button btnGoToPayment = (Button) findViewById(R.id.btnGoToPayment);

        if (getIntent().hasExtra("LOCATION_NAME")) {
            currentLocation = getIntent().getStringExtra("LOCATION_NAME");
            tvBookingLocation.setText("Đang đặt tour: " + currentLocation);
        }
        // 1. Ánh xạ ô nhập địa điểm
        EditText edtDestination = findViewById(R.id.edtDestination);

        // 2. Lấy tên địa điểm từ trang Chi tiết truyền sang và điền sẵn vào ô
        String locationName = getIntent().getStringExtra("LOCATION_NAME");
        if (locationName != null) {
            edtDestination.setText(locationName);
        }

        btnGoToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = etTravelDate.getText().toString().trim();
                String people = etNumberOfPeople.getText().toString().trim();

                if (date.isEmpty() || people.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Vui lòng nhập ngày đi và số người!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
                intent.putExtra("LOCATION_NAME", currentLocation);
                startActivity(intent);
            }
        });
    }
}
