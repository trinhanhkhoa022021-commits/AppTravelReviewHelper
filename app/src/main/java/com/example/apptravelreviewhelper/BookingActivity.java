package com.example.apptravelreviewhelper;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;
import java.util.HashMap;

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
    private ImageView imgBookingLocation;

    // View cho trạng thái thành công
    private LinearLayout layoutSuccess;
    private TextView tvSuccessMessage;
    private Button btnNewBooking;
    private androidx.cardview.widget.CardView cardViewDetail;

    private int adultCount = 3;
    private int nightCount = 0; 
    private int roomCount = 1;

    private Calendar checkInDate = null;
    private Calendar checkOutDate = null;
    private String currentImageUrl = ""; // Biến lưu URL ảnh hiện tại

    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    private final String[] locations = {"Phú Quốc", "Đà Lạt", "Hội An", "Vịnh Hạ Long", "Nha Trang", "Đà Nẵng"};
    private final String[] roomTypes = {"Phòng cơ bản", "Phòng cao cấp", "Phòng Tổng thống"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang xử lý đặt chỗ...");
        progressDialog.setCancelable(false);

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
        imgBookingLocation = findViewById(R.id.imgBookingLocation);

        layoutSuccess = findViewById(R.id.layoutSuccess);
        tvSuccessMessage = findViewById(R.id.tvSuccessMessage);
        btnNewBooking = findViewById(R.id.btnNewBooking);
        cardViewDetail = findViewById(R.id.cardViewDetail);
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
                // Senior Logic: Tự động lấy ảnh từ Firebase khi người dùng chọn địa điểm khác trên Spinner
                fetchImageUrlFromFirebase(locations[position]);
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

    // Senior Tip: Hàm này truy vấn collection 'Locations' để lấy chính xác imageUrl dựa trên tên
    private void fetchImageUrlFromFirebase(String locationName) {
        db.collection("Locations")
                .whereEqualTo("name", locationName)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String url = queryDocumentSnapshots.getDocuments().get(0).getString("imageUrl");
                        if (url != null && !url.isEmpty()) {
                            currentImageUrl = url; // Cập nhật URL hiện tại để lưu vào Booking sau này
                            Glide.with(this)
                                    .load(url)
                                    .placeholder(android.R.drawable.ic_menu_gallery)
                                    .error(android.R.drawable.ic_menu_report_image)
                                    .into(imgBookingLocation);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    android.util.Log.e("BookingActivity", "Lỗi lấy ảnh: " + e.getMessage());
                });
    }

    private void handleIntentData() {
        String locationFromDetail = getIntent().getStringExtra("LOCATION_NAME");
        String imageFromDetail = getIntent().getStringExtra("IMAGE_URL");
        
        if (imageFromDetail != null && !imageFromDetail.isEmpty()) {
            currentImageUrl = imageFromDetail;
            Glide.with(this).load(currentImageUrl).placeholder(android.R.drawable.ic_menu_gallery).into(imgBookingLocation);
        }

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
            if (!isNetworkAvailable()) {
                Toast.makeText(this, "Không có kết nối Internet. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkInDate == null || checkOutDate == null) {
                Toast.makeText(this, "Vui lòng chọn ngày nhận và trả phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (nightCount <= 0) {
                Toast.makeText(this, "Ngày trả phòng phải sau ngày nhận phòng!", Toast.LENGTH_SHORT).show();
                return;
            }

            String location = spinnerLocation.getSelectedItem().toString();
            String checkIn = edtCheckIn.getText().toString();
            String roomType = spinnerRoomType.getSelectedItem().toString();
            String totalPrice = tvTotalPrice.getText().toString();
            String paymentMethod = (rgPaymentMethod.getCheckedRadioButtonId() == R.id.rbCreditCard) ? "Thẻ tín dụng" : "MoMo";

            progressDialog.show();

            // Lưu dữ liệu dạng Map để dễ dàng mở rộng trường imageUrl
            Map<String, Object> billMap = new HashMap<>();
            billMap.put("location", location);
            billMap.put("checkIn", checkIn);
            billMap.put("nights", nightCount);
            billMap.put("adults", adultCount);
            billMap.put("roomType", roomType);
            billMap.put("roomCount", roomCount);
            billMap.put("paymentMethod", paymentMethod);
            billMap.put("totalPrice", totalPrice);
            billMap.put("timestamp", System.currentTimeMillis());
            billMap.put("imageUrl", currentImageUrl); // LƯU URL ẢNH VÀO FIRESTORE

            db.collection("Bookings")
                    .add(billMap)
                    .addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        Toast.makeText(BookingActivity.this, "Đặt chỗ thành công!", Toast.LENGTH_SHORT).show();

                        // Cải tiến: Hiển thị trạng thái hoàn tất thay vì chuyển trang ngay
                        showSuccessState(location, checkIn, roomCount, roomType, totalPrice, paymentMethod);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(BookingActivity.this, "Lỗi khi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    private void showSuccessState(String location, String checkIn, int roomCount, String roomType, String totalPrice, String paymentMethod) {
        // Vô hiệu hóa form và nút xác nhận
        btnConfirmBooking.setVisibility(View.GONE);
        btnSelectCheckIn.setEnabled(false);
        btnSelectCheckOut.setEnabled(false);
        btnMinusAdult.setEnabled(false);
        btnPlusAdult.setEnabled(false);
        btnMinusRoom.setEnabled(false);
        btnPlusRoom.setEnabled(false);
        spinnerLocation.setEnabled(false);
        spinnerRoomType.setEnabled(false);
        rgPaymentMethod.setEnabled(false);

        // Hiển thị panel thành công
        layoutSuccess.setVisibility(View.VISIBLE);
        tvSuccessMessage.setText("Đã đặt chỗ: " + location + " - Bấm để xem chi tiết ->");

        // Lựa chọn 1: Xem chi tiết
        cardViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(BookingActivity.this, BillActivity.class);
            intent.putExtra("BILL_LOCATION", location);
            intent.putExtra("BILL_CHECK_IN", checkIn);
            intent.putExtra("BILL_NIGHTS", nightCount);
            intent.putExtra("BILL_ROOM_COUNT", roomCount);
            intent.putExtra("BILL_ROOM_TYPE", roomType);
            intent.putExtra("BILL_ADULTS", adultCount);
            intent.putExtra("BILL_TOTAL_PRICE", totalPrice);
            intent.putExtra("BILL_PAYMENT_METHOD", paymentMethod);
            startActivity(intent);
        });

        // Lựa chọn 2: Đặt chỗ mới
        btnNewBooking.setOnClickListener(v -> resetForm());
    }

    private void resetForm() {
        // Reset dữ liệu logic
        adultCount = 3;
        roomCount = 1;
        nightCount = 0;
        checkInDate = null;
        checkOutDate = null;

        // Reset UI
        tvAdultCount.setText(String.valueOf(adultCount));
        tvRoomCount.setText(String.valueOf(roomCount));
        tvNightCount.setText("0 đêm");
        edtCheckIn.setText("");
        edtCheckOut.setText("");
        spinnerLocation.setSelection(0);
        spinnerRoomType.setSelection(0);
        rgPaymentMethod.check(R.id.rbCreditCard);
        calculateTotalPrice();

        // Kích hoạt lại form
        btnConfirmBooking.setVisibility(View.VISIBLE);
        btnSelectCheckIn.setEnabled(true);
        btnSelectCheckOut.setEnabled(true);
        btnMinusAdult.setEnabled(true);
        btnPlusAdult.setEnabled(true);
        btnMinusRoom.setEnabled(true);
        btnPlusRoom.setEnabled(true);
        spinnerLocation.setEnabled(true);
        spinnerRoomType.setEnabled(true);
        rgPaymentMethod.setEnabled(true);

        // Ẩn panel thành công
        layoutSuccess.setVisibility(View.GONE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

                    // Senior Dev Suggestion: Validate date (Check-in should not be in the past)
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);

                    if (selected.before(today)) {
                        Toast.makeText(this, "Ngày được chọn không được là ngày trong quá khứ!", Toast.LENGTH_SHORT).show();
                        return;
                    }

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
