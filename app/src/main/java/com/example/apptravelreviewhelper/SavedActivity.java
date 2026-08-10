package com.example.apptravelreviewhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SavedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private LocationAdapter adapter;
    private List<Location> savedList;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        recyclerView = findViewById(R.id.recyclerViewSaved);
        tvEmpty = findViewById(R.id.tvEmpty);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        savedList = new ArrayList<>();
        adapter = new LocationAdapter(this, savedList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadSavedLocations();
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        if (bottomNavigationView == null) return;
        
        // Đảm bảo icon "Đã lưu" luôn được highlight
        bottomNavigationView.setSelectedItemId(R.id.nav_saved);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_saved) return true;

            if (itemId == R.id.nav_hot) {
                Intent intent = new Intent(SavedActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return true;
            } else if (itemId == R.id.nav_search) {
                startActivity(new Intent(SavedActivity.this, SearchActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_booking) {
                startActivity(new Intent(SavedActivity.this, BookingActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_account) {
                startActivity(new Intent(SavedActivity.this, AccountActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_saved);
        }
        loadSavedLocations();
    }

    private void loadSavedLocations() {
        Set<String> savedIds = SavedLocationManager.getSavedIds(this);

        if (savedIds.isEmpty()) {
            savedList.clear();
            adapter.notifyDataSetChanged();
            showEmptyState(true);
            return;
        }

        db.collection("Locations")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            savedList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (savedIds.contains(document.getId())) {
                                    Location loc = document.toObject(Location.class);
                                    loc.setId(document.getId());
                                    savedList.add(loc);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            showEmptyState(savedList.isEmpty());
                        }
                    }
                });
    }

    private void showEmptyState(boolean isEmpty) {
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}