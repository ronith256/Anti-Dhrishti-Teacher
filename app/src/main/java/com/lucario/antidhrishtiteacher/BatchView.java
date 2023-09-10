package com.lucario.antidhrishtiteacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class BatchView extends AppCompatActivity  implements BatchViewAdapter.SelectBatch
{
    private String batches;
    private BatchViewAdapter adapter;
    private RecyclerView recyclerView;
    ArrayList<String> batchList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batches);
        SharedPreferences sharedPreferences = getSharedPreferences("cred", MODE_PRIVATE);
        batches = sharedPreferences.getString("batches", null);
        batchList = new ArrayList<>(Arrays.asList(batches.split(",")));
        recyclerView = findViewById(R.id.batches_recycler_view);
        adapter = new BatchViewAdapter(batchList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void selectBatch(String batchName) {
        Intent intent = new Intent(this, BatchSelectView.class);
        intent.putExtra("batch-name", batchName);
        startActivity(intent);
    }
}
