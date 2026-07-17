package com.example.apptravelreviewhelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        TextView tvPaymentSummary = (TextView) findViewById(R.id.tvPaymentSummary);
        final RadioGroup rgPaymentMethod = (RadioGroup) findViewById(R.id.rgPaymentMethod);
        Button btnConfirmPayment = (Button) findViewById(R.id.btnConfirmPayment);

        if (getIntent().hasExtra("LOCATION_NAME")) {
            String location = getIntent().getStringExtra("LOCATION_NAME");
            tvPaymentSummary.setText("Thanh toán cho tour: " + location);
        }

        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rgPaymentMethod.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRadio = (RadioButton) findViewById(selectedId);
                    String paymentMethod = selectedRadio.getText().toString();

                    Toast.makeText(PaymentActivity.this, "Đã đặt tour thành công!\nPhương thức: " + paymentMethod, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(PaymentActivity.this, "Vui lòng chọn phương thức thanh toán!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
