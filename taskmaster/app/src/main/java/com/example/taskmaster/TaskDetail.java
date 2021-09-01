package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Button goHomeButtonDetail = findViewById(R.id.homeButtonDetail);
        goHomeButtonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goHomeTasks = new Intent(TaskDetail.this, MainActivity.class);
                startActivity(goHomeTasks);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
//        String taskName = intent.getExtras().getString("taskName");
//        String taskBody = intent.getExtras().getString("taskBody");
//        String taskState = intent.getExtras().getString("taskState");
        TextView title = findViewById(R.id.taskTitleDetail);
        TextView body = findViewById(R.id.taskBodyDetail);
        TextView state = findViewById(R.id.taskStateDetail);
        title.setText(intent.getExtras().getString("taskName"));
        body.setText(intent.getExtras().getString("taskBody"));
        state.setText(intent.getExtras().getString("taskState"));
    }
}