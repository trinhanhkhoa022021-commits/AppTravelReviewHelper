package com.example.apptravelreviewhelper;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BillHistoryManager {
    private static final String PREF_NAME = "bill_history";
    private static final String KEY_BILLS = "bills_json";

    // Thêm 1 bill mới vào lịch sử
    public static void addBill(Context context, Bill bill) {
        try {
            List<Bill> bills = getAllBills(context);
            bills.add(bill);

            JSONArray array = new JSONArray();
            for (Bill b : bills) {
                JSONObject obj = new JSONObject();
                obj.put("location", b.getLocation());
                obj.put("checkIn", b.getCheckIn());
                obj.put("nights", b.getNights());
                obj.put("adults", b.getAdults());
                obj.put("roomType", b.getRoomType());
                obj.put("roomCount", b.getRoomCount());
                obj.put("paymentMethod", b.getPaymentMethod());
                obj.put("totalPrice", b.getTotalPrice());
                obj.put("timestamp", b.getTimestamp());
                array.put(obj);
            }

            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_BILLS, array.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Lấy toàn bộ lịch sử, mới nhất lên đầu
    public static List<Bill> getAllBills(Context context) {
        List<Bill> result = new ArrayList<>();
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_BILLS, null);

        if (json == null) return result;

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Bill bill = new Bill(
                        obj.getString("location"),
                        obj.getString("checkIn"),
                        obj.getInt("nights"),
                        obj.getInt("adults"),
                        obj.getString("roomType"),
                        obj.getInt("roomCount"),
                        obj.getString("paymentMethod"),
                        obj.getString("totalPrice"),
                        obj.getLong("timestamp")
                );
                result.add(bill);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Sắp xếp mới nhất lên đầu
        Collections.sort(result, new Comparator<Bill>() {
            @Override
            public int compare(Bill b1, Bill b2) {
                return Long.compare(b2.getTimestamp(), b1.getTimestamp());
            }
        });

        return result;
    }
}