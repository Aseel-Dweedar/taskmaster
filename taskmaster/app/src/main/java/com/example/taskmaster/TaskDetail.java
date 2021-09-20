package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

import java.io.File;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        TextView title = findViewById(R.id.taskTitleDetail);
        TextView body = findViewById(R.id.taskBodyDetail);
        TextView state = findViewById(R.id.taskStateDetail);
        ImageView storeImg = findViewById(R.id.storeImg);
        TextView location = findViewById(R.id.locationText);
        title.setText(intent.getExtras().getString("taskName"));
        body.setText(intent.getExtras().getString("taskBody"));
        state.setText(intent.getExtras().getString("taskState"));
        location.setText(intent.getExtras().getString("taskLocation"));

        if (intent.getExtras().getString("taskImage") != null) {
            Amplify.Storage.downloadFile(
                    intent.getExtras().getString("taskImage"),
                    new File(getApplicationContext().getFilesDir() + "/" + intent.getExtras().getString("taskImage") + ".jpg"),
                    result -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(result.getFile().getPath());
                        storeImg.setImageBitmap(bitmap);
                        Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile().getName());
                    },
                    error -> Log.e("MyAmplifyApp", "Download Failure", error)
            );
        }

        Button goHomeButtonDetail = findViewById(R.id.homeButtonDetail);
        goHomeButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goHomeTasks = new Intent(TaskDetail.this, MainActivity.class);
                startActivity(goHomeTasks);
            }
        });
    }
}