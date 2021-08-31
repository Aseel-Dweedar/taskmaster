package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Task> tasksList = new ArrayList<Task>();
        tasksList.add(new Task("Lab" , "This is the Lab" , "assigned"));
        tasksList.add(new Task("Code Challenge" , "This is the Code Challenge" , "complete"));
        tasksList.add(new Task("Reading" , "This is the Reading" , "new"));

        RecyclerView tasksListRecyclerView = findViewById(R.id.taskRecyclerView);
        tasksListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksListRecyclerView.setAdapter(new TaskAdapter(tasksList));

        Button allTaskButton = findViewById(R.id.allTask);
        Button addTaskButton = findViewById(R.id.addTask);

        allTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToAllTasks = new Intent(MainActivity.this, AllTasks.class);
                startActivity(goToAllTasks);
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToAllTasks = new Intent(MainActivity.this, AddTask.class);
                startActivity(goToAllTasks);
            }
        });

        Button goSettingButton = findViewById(R.id.settingsButton);
        goSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goSetting = new Intent(MainActivity.this, Settings.class);
                startActivity(goSetting);
            }
        });


        Button labButton = findViewById(R.id.labButton);
        labButton.setOnClickListener((view -> {
            String taskTitle = labButton.getText().toString();
            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
            goToTaskDetail.putExtra("taskName", taskTitle);
            startActivity(goToTaskDetail);
        }));

        Button codeButton = findViewById(R.id.codeButton);
        codeButton.setOnClickListener((view -> {
            String taskTitle = codeButton.getText().toString();
            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
            goToTaskDetail.putExtra("taskName", taskTitle);
            startActivity(goToTaskDetail);
        }));

        Button readButton = findViewById(R.id.readButton);
        readButton.setOnClickListener((view -> {
            String taskTitle = readButton.getText().toString();
            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
            goToTaskDetail.putExtra("taskName", taskTitle);
            startActivity(goToTaskDetail);
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();

        String usernameTasks = "’s tasks";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = sharedPreferences.getString("username", "Your");
        TextView userTasks = findViewById(R.id.myTask);
        userTasks.setText(username+usernameTasks);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "Override onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "Override onPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "Override onStop()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(), "Override onRestart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "Override onDestroy()", Toast.LENGTH_SHORT).show();
    }

}