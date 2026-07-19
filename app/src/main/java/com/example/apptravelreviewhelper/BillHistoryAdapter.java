package com.example.apptravelreviewhelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BillHistoryAdapter extends RecyclerView.Adapter<BillHistoryAdapter.BillViewHolder> {

    private Context context;
    private List<Bill> billList;

    public BillHistoryAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        Bill bill = billList.get(position);
        holder.tvItemLocation.setText(bill.getLocation());
        holder.tvItemCheckIn.setText("Ngày nhận phòng: " + bill.getCheckIn());
        holder.tvItemDetail.setText(
                bill.getNights() + " đêm • " + bill.getRoomCount() + " phòng • " + bill.getAdults() + " người lớn"
        );
        holder.tvItemTotalPrice.setText(bill.getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemLocation, tvItemCheckIn, tvItemDetail, tvItemTotalPrice;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemLocation = itemView.findViewById(R.id.tvItemLocation);
            tvItemCheckIn = itemView.findViewById(R.id.tvItemCheckIn);
            tvItemDetail = itemView.findViewById(R.id.tvItemDetail);
            tvItemTotalPrice = itemView.findViewById(R.id.tvItemTotalPrice);
        }
    }
}