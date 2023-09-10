package com.lucario.antidhrishtiteacher;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class BatchSelectView extends AppCompatActivity {
    private String batchName;
    private RecyclerView pastClassView;

    private TextView className;
    private EditText startTime;
    private EditText endTime;
    private EditText credits;
    private CardView createClassCard;
    private Button createClassButton;

    private String baseURL = "https://antidhrishti.lucario.site";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_select);
        batchName = getIntent().getStringExtra("batch-name");
        pastClassView = findViewById(R.id.past_class_recycler_view);

        className = findViewById(R.id.class_name);
        createClassCard = findViewById(R.id.create_class_card);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
        createClassButton = findViewById(R.id.create_class_button);
        credits = findViewById(R.id.credits);
        createClassButton.setOnClickListener(e->{
            new Thread(()->{
                boolean created = createClass();
                runOnUiThread(()->{
                    if(created){
                        Toast.makeText(this, "Class created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Class creation failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }

    private boolean createClass(){

        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("batch_name", batchName)
                .add("class_name", className.getText().toString())
                .add("start_time", startTime.getText().toString())
                .add("end_time", endTime.getText().toString())
                .add("secret", "secret")
                .add("credits", credits.getText().toString())
                .build();

        Request request = new Request.Builder()
                .url(baseURL + "/create_class")
                .post(formBody)
                .addHeader("Accept", "application/json")
                .build();

        return false;
    }
}
