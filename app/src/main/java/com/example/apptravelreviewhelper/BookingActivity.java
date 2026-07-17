package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

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
