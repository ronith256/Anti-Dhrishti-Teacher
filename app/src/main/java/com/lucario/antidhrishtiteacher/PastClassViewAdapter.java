package com.lucario.antidhrishtiteacher;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PastClassViewAdapter extends RecyclerView.Adapter<PastClassViewAdapter.PastClassViewHolder> {


    @NonNull
    @Override
    public PastClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PastClassViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class PastClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView className;
        private final EditText startTime;
        private final EditText endTime;
        private final CardView createClassCard;
        public PastClassViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            createClassCard = itemView.findViewById(R.id.create_class_card);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
        }
    }
}
