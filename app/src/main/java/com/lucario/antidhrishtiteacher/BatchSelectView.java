package com.lucario.antidhrishtiteacher;

import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BatchSelectView extends AppCompatActivity implements PastClassViewAdapter.getPastClassData {
    private String batchName;
    private RecyclerView pastClassView;

    private TextView className;
    private EditText startTime;
    private EditText endTime;
    private EditText credits;
    private CardView createClassCard;
    private Button createClassButton;

    private PastClassViewAdapter pastClassViewAdapter;

    private ArrayList<ClassNameTime> classList;

    private String baseURL = "https://antidhrishti.lucario.site";
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_select);
        batchName = getIntent().getStringExtra("batch-name");
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        loadClassList();
        pastClassView = findViewById(R.id.past_class_recycler_view);
        className = findViewById(R.id.class_name);
        createClassCard = findViewById(R.id.create_class_card);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);
        createClassButton = findViewById(R.id.create_class_button);
        credits = findViewById(R.id.credits);
        pastClassViewAdapter = new PastClassViewAdapter(classList ,this);

        // Once the endTime edit text is clicked, show a date time picker and then give the user the option to select the date and time and show the output in the edit text.
        endTime.setOnClickListener(e -> {
            showDateTimePicker(endTime);
        });

        startTime.setOnClickListener(e -> {
            showDateTimePicker(startTime);
        });

        createClassButton.setOnClickListener(e->{
            new Thread(()->{
                boolean created = createClass();
                runOnUiThread(()->{
                    if(created){
                        try {
                            classList.add(new ClassNameTime(className.getText().toString(), sdf.parse(startTime.getText().toString())));
                            pastClassViewAdapter.classList = classList;
                            pastClassViewAdapter.notifyDataSetChanged();
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }
                        writeClassList();
                        Toast.makeText(this, "Class created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Class creation failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        pastClassView.setLayoutManager(new LinearLayoutManager(this));
        pastClassView.setAdapter(pastClassViewAdapter);
    }

    private void showDateTimePicker(EditText editText){
        // Get current date and time
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a new instance of DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Create a new instance of TimePickerDialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timeView, selectedHour, selectedMinute) -> {
                // Format date and time into yyyy-mm-dd hh:mm format
                String formattedDateTime = String.format(Locale.getDefault(),
                        "%04d-%02d-%02d %02d:%02d:00", selectedYear, selectedMonth + 1, selectedDay, selectedHour, selectedMinute);

                // Set formatted date and time into the EditText
                editText.setText(formattedDateTime);
            }, hour, minute, true);

            // Show the TimePickerDialog
            timePickerDialog.show();
        }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void writeClassList(){
        try {
            FileOutputStream fos = openFileOutput("class.ser", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(classList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadClassList(){
        try {
            FileInputStream fis = openFileInput("class.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            classList = (ArrayList<ClassNameTime>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Initialize the chat list if it doesn't exist
        if (classList == null) {
            classList = new ArrayList<>();
        }
    }

    private boolean createClass(){

        OkHttpClient client = new OkHttpClient();

        FormBody formBody = new FormBody.Builder()
                .add("batch_name", batchName)
                .add("class_name", className.getText().toString().trim())
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

        try {
            Response response = client.newCall(request).execute();
            String resp = response.body().string();
            return resp.equals("true");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateAttendanceReport(String className, String startTime){
        OkHttpClient client = new OkHttpClient();
        // Create a SimpleDateFormat object with the desired output pattern

        FormBody formBody = new FormBody.Builder()
                .add("batch_name", batchName)
                .add("class_name", className)
                .add("start_time", startTime)
                .add("secret", "secret")
                .build();


        Request request = new Request.Builder()
                .url(baseURL + "/get_attendance")
                .post(formBody)
                .addHeader("Accept", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void downloadFile(Context context, String fileName) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        String fileUrl = baseURL + "/" + fileName;
        Uri downloadUri = Uri.parse(fileUrl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        // Set the destination folder
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, fileName);

        // Set other optional parameters
        request.setTitle(fileName);
        request.setDescription("Downloading file...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(true);

        // Enqueue the download
        long downloadId = downloadManager.enqueue(request);
    }
    @Override
    public void getAttendance(String className, String startTime) {
       new Thread(()->{
           String fileName = generateAttendanceReport(className, startTime);
           System.out.println(fileName);
           downloadFile(BatchSelectView.this,fileName);
       }).start();
    }
}


