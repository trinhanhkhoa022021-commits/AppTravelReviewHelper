package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText edtSearchBox;
    private RecyclerView rvSearchResults;
    private LinearLayout layoutSuggestions;
    private List<Location> locationList = new ArrayList<>();
    private List<Location> filteredList = new ArrayList<>();
    private LocationAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Ánh xạ
        layoutSuggestions = findViewById(R.id.layoutSuggestions);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        edtSearchBox = findViewById(R.id.edtSearchBox);
        ImageView btnBack = findViewById(R.id.btnBack);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Xử lý nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý 2 mục gợi ý
        findViewById(R.id.itemNearby).setOnClickListener(v -> {
            // Mở Google Maps tìm địa điểm du lịch gần đây (Việt hóa từ khóa)
            String query = "địa điểm du lịch gần đây";
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(query));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            
            try { 
                startActivity(mapIntent); 
            } catch (Exception e) { 
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/" + Uri.encode(query)))); 
            }
        });

        findViewById(R.id.itemFlight).setOnClickListener(v -> {
            // Mở web tìm vé máy bay
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/flights"));
            startActivity(intent);
        });

        // Cài đặt RecyclerView
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LocationAdapter(this, filteredList);
        rvSearchResults.setAdapter(adapter);

        // Lấy dữ liệu
        fetchData();

        // Xử lý tìm kiếm
        edtSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    layoutSuggestions.setVisibility(View.GONE);
                    rvSearchResults.setVisibility(View.VISIBLE);
                    filter(s.toString());
                } else {
                    layoutSuggestions.setVisibility(View.VISIBLE);
                    rvSearchResults.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Tự động bật bàn phím
        edtSearchBox.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) imm.showSoftInput(edtSearchBox, InputMethodManager.SHOW_IMPLICIT);

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        if (bottomNavigationView == null) return;
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_search) return true;

            if (itemId == R.id.nav_hot) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return false;
            } else if (itemId == R.id.nav_saved) {
                startActivity(new Intent(SearchActivity.this, SavedActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return false;
            } else if (itemId == R.id.nav_booking) {
                startActivity(new Intent(SearchActivity.this, BookingActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return false;
            } else if (itemId == R.id.nav_account) {
                startActivity(new Intent(SearchActivity.this, AccountActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return false;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_search);
        }
    }

    private void fetchData() {
        FirebaseFirestore.getInstance().collection("Locations").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                locationList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) locationList.add(doc.toObject(Location.class));
            }
        });
    }

    private void filter(String text) {
        filteredList.clear();
        for (Location loc : locationList) {
            if (loc.getName().toLowerCase().contains(text.toLowerCase())) filteredList.add(loc);
        }
        adapter.notifyDataSetChanged();
    }
}