package com.lucario.antidhrishtiteacher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BatchViewAdapter extends RecyclerView.Adapter<BatchViewAdapter.BatchViewHolder>{
    ArrayList<String> batchList;
    SelectBatch listener;
    public BatchViewAdapter(ArrayList<String> batchList, SelectBatch listener) {
        this.batchList = batchList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View thisItemsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.batch_view_items, parent, false);
        return new BatchViewHolder(thisItemsView);
    }

    @Override
    public void onBindViewHolder(@NonNull BatchViewHolder holder, int position) {
        holder.batchName.setText(batchList.get(position));
        holder.batchCard.setOnClickListener(e->{
            listener.selectBatch(batchList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return batchList.size();
    }

    public class BatchViewHolder extends RecyclerView.ViewHolder {
        private final TextView batchName;
        private final CardView batchCard;
        public BatchViewHolder(@NonNull  View itemView) {
            super(itemView);
            batchName = itemView.findViewById(R.id.batch_name);
            batchCard = itemView.findViewById(R.id.batch_card_view);
        }
    }

    public interface SelectBatch{
        void selectBatch(String batchName);
    }

}

