package com.example.apptravelreviewhelper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class BookingActivity extends AppCompatActivity {

    private EditText edtCheckIn, edtCheckOut;
    private Button btnSelectCheckIn, btnSelectCheckOut, btnMinusAdult, btnPlusAdult, btnConfirmBooking;
    private ImageButton btnBack;
    private TextView tvAdultCount, tvHeaderTitle;
    private RadioGroup rgPaymentMethod;
    private LinearLayout panelCreditCard, panelMoMo;

    private Button btnMinusRoom, btnPlusRoom;
    private TextView tvNightCount, tvRoomCount, tvTotalPrice;
    private Spinner spinnerLocation, spinnerRoomType;

    private int adultCount = 3;
    private int nightCount = 0; 
    private int roomCount = 1;

    private Calendar checkInDate = null;
    private Calendar checkOutDate = null;

    private final String[] locations = {"Phú Quốc", "Đà Lạt", "Hội An", "Vịnh Hạ Long", "Nha Trang", "Đà Nẵng"};
    private final String[] roomTypes = {"Phòng cơ bản", "Phòng cao cấp", "Phòng Tổng thống"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initViews();
        setupSpinners();
        handleIntentData();
        setupListeners();

        calculateTotalPrice();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        edtCheckIn = findViewById(R.id.edtCheckIn);
        edtCheckOut = findViewById(R.id.edtCheckOut);
        btnSelectCheckIn = findViewById(R.id.btnSelectCheckIn);
        btnSelectCheckOut = findViewById(R.id.btnSelectCheckOut);
        
        btnMinusAdult = findViewById(R.id.btnMinusAdult);
        btnPlusAdult = findViewById(R.id.btnPlusAdult);
        tvAdultCount = findViewById(R.id.tvAdultCount);
        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        panelCreditCard = findViewById(R.id.panelCreditCard);
        panelMoMo = findViewById(R.id.panelMoMo);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);
        tvNightCount = findViewById(R.id.tvNightCount);

        btnMinusRoom = findViewById(R.id.btnMinusRoom);
        btnPlusRoom = findViewById(R.id.btnPlusRoom);
        tvRoomCount = findViewById(R.id.tvRoomCount);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerRoomType = findViewById(R.id.spinnerRoomType);
    }

    private void setupSpinners() {
        ArrayAdapter<String> locAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        spinnerLocation.setAdapter(locAdapter);
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (tvHeaderTitle != null) {
                    tvHeaderTitle.setText("Xác nhận đặt chỗ - " + locations[position]);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roomTypes);
        spinnerRoomType.setAdapter(roomAdapter);
        spinnerRoomType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateTotalPrice();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void handleIntentData() {
        String locationFromDetail = getIntent().getStringExtra("LOCATION_NAME");
        if (locationFromDetail != null && !locationFromDetail.isEmpty()) {
            for (int i = 0; i < locations.length; i++) {
                if (locations[i].equalsIgnoreCase(locationFromDetail.trim())) {
                    spinnerLocation.setSelection(i);
                    break;
                }
            }
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        
        btnSelectCheckIn.setOnClickListener(v -> showDatePicker(true));
        btnSelectCheckOut.setOnClickListener(v -> showDatePicker(false));

        btnMinusAdult.setOnClickListener(v -> {
            if (adultCount > 1) { adultCount--; tvAdultCount.setText(String.valueOf(adultCount)); }
        });
        btnPlusAdult.setOnClickListener(v -> {
            adultCount++; tvAdultCount.setText(String.valueOf(adultCount));
        });

        btnMinusRoom.setOnClickListener(v -> {
            if (roomCount > 1) {
                roomCount--;
                tvRoomCount.setText(String.valueOf(roomCount));
                calculateTotalPrice();
            }
        });
        btnPlusRoom.setOnClickListener(v -> {
            roomCount++;
            tvRoomCount.setText(String.valueOf(roomCount));
            calculateTotalPrice();
        });

        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbCreditCard) {
                panelCreditCard.setVisibility(View.VISIBLE);
                panelMoMo.setVisibility(View.GONE);
            } else if (checkedId == R.id.rbMoMo) {
                panelCreditCard.setVisibility(View.GONE);
                panelMoMo.setVisibility(View.VISIBLE);
            }
        });

        btnConfirmBooking.setOnClickListener(v -> {
            if (checkInDate == null || checkOutDate == null) {
                Toast.makeText(this, "Vui lòng chọn ngày nhận và trả phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nightCount <= 0) {
                Toast.makeText(this, "Ngày trả phòng phải sau ngày nhận phòng!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(BookingActivity.this, BillActivity.class);
            intent.putExtra("BILL_LOCATION", spinnerLocation.getSelectedItem().toString());
            intent.putExtra("BILL_CHECK_IN", edtCheckIn.getText().toString());
            intent.putExtra("BILL_NIGHTS", nightCount);
            intent.putExtra("BILL_ROOM_COUNT", roomCount);
            intent.putExtra("BILL_ROOM_TYPE", spinnerRoomType.getSelectedItem().toString());
            intent.putExtra("BILL_ADULTS", adultCount);
            intent.putExtra("BILL_TOTAL_PRICE", tvTotalPrice.getText().toString());

            String paymentMethod = (rgPaymentMethod.getCheckedRadioButtonId() == R.id.rbCreditCard) ? "Thẻ tín dụng" : "MoMo";
            intent.putExtra("BILL_PAYMENT_METHOD", paymentMethod);

            startActivity(intent);
        });
    }

    private void calculateNights() {
        if (checkInDate != null && checkOutDate != null) {
            long diff = checkOutDate.getTimeInMillis() - checkInDate.getTimeInMillis();
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            nightCount = (int) days;
            
            if (nightCount < 0) nightCount = 0;
            tvNightCount.setText(nightCount + " đêm");
            calculateTotalPrice();
        }
    }

    private void calculateTotalPrice() {
        double basePriceFor3Nights = 2500000;
        double pricePerNight = basePriceFor3Nights / 3.0;
        double extraNightFee = basePriceFor3Nights * 0.30;

        double totalNightPrice = 0;
        if (nightCount == 0) {
            totalNightPrice = 0;
        } else if (nightCount <= 3) {
            totalNightPrice = pricePerNight * nightCount;
        } else {
            totalNightPrice = basePriceFor3Nights + ((nightCount - 3) * extraNightFee);
        }

        double totalRoomPrice = totalNightPrice * roomCount;

        double roomMultiplier = 1.0; 
        if (spinnerRoomType.getSelectedItem() != null) {
            String type = spinnerRoomType.getSelectedItem().toString();
            if (type.equals("Phòng cao cấp")) {
                roomMultiplier = 1.5;
            } else if (type.equals("Phòng Tổng thống")) {
                roomMultiplier = 3.0;
            }
        }

        double finalPrice = totalRoomPrice * roomMultiplier;

        DecimalFormat formatter = new DecimalFormat("###,###,### VND");
        tvTotalPrice.setText(formatter.format(finalPrice));
    }

    private void showDatePicker(boolean isCheckIn) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    Calendar selected = Calendar.getInstance();
                    selected.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0);
                    selected.set(Calendar.MILLISECOND, 0);

                    if (isCheckIn) {
                        checkInDate = selected;
                        edtCheckIn.setText(date);
                    } else {
                        checkOutDate = selected;
                        edtCheckOut.setText(date);
                    }
                    calculateNights();
                }, year, month, day);
        datePickerDialog.show();
    }
}
