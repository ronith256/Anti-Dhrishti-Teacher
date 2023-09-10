package com.lucario.antidhrishtiteacher;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PastClassViewAdapter extends RecyclerView.Adapter<PastClassViewAdapter.PastClassViewHolder> {
    ArrayList<ClassNameTime> classList;
    getPastClassData listener;
    PastClassViewAdapter(ArrayList<ClassNameTime> classList ,getPastClassData listener){
        this.classList = classList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PastClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View thisItemsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_class_items, parent, false);
        return new PastClassViewAdapter.PastClassViewHolder(thisItemsView);
    }

    @Override
    public void onBindViewHolder(@NonNull PastClassViewHolder holder, int position) {
        ClassNameTime classNameTime = classList.get(position);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateTimeFormat.format(classNameTime.getStartTime());
        holder.className.setText(classNameTime.getClassName());
        holder.classDate.setText(dateTime);
        holder.pastClassCard.setOnClickListener(e->{
            listener.getAttendance(classNameTime.getClassName(),dateTime);
        });
    }

    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class PastClassViewHolder extends RecyclerView.ViewHolder {
        private final TextView className;
        private final TextView classDate;
        private final CardView pastClassCard;
        public PastClassViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.class_name);
            pastClassCard = itemView.findViewById(R.id.class_card_view);
            classDate = itemView.findViewById(R.id.class_date);
        }
    }

    public interface getPastClassData{
        void getAttendance(String className, String startTime);
    }
}
