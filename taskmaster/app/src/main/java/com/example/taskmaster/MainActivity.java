package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTodo;
import com.amplifyframework.datastore.generated.model.Team;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //    AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String teamId = sharedPreferences.getString("teamId", "");

//        Team team1 = Team.builder().name("team1").build();
//        Team team2 = Team.builder().name("team2").build();
//        Team team3 = Team.builder().name("team3").build();
//
//        Amplify.API.mutate(
//                ModelMutation.create(team1),
//                response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(team2),
//                response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );
//        Amplify.API.mutate(
//                ModelMutation.create(team3),
//                response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//                error -> Log.e("MyAmplifyApp", "Create failed", error)
//        );


//        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasksDatabase").allowMainThreadQueries().build();

        if (!teamId.equals("")) {
            RecyclerView tasksListRecyclerView = findViewById(R.id.taskRecyclerView);
            Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    tasksListRecyclerView.getAdapter().notifyDataSetChanged();
                    return false;
                }
            });
            List<TaskTodo> tasksList = new ArrayList<TaskTodo>();
            Amplify.API.query(
                    ModelQuery.get(Team.class, teamId),
                    response -> {
                        for (TaskTodo taskTodo : response.getData().getTaskTodo()) {
                            tasksList.add(taskTodo);
                        }
                        handler.sendEmptyMessage(1);
                    },
                    error -> Log.e("MyAmplifyApp", error.toString(), error)
            );
            tasksListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            tasksListRecyclerView.setAdapter(new TaskAdapter(tasksList));
        }


        Button addTaskButton = findViewById(R.id.addTask);
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

//        Button allTaskButton = findViewById(R.id.allTask);
//        allTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View V) {
//                Intent goToAllTasks = new Intent(MainActivity.this, AllTasks.class);
//                startActivity(goToAllTasks);
//            }
//        });

//        Button labButton = findViewById(R.id.labButton);
//        labButton.setOnClickListener((view -> {
//            String taskTitle = labButton.getText().toString();
//            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
//            goToTaskDetail.putExtra("taskName", taskTitle);
//            startActivity(goToTaskDetail);
//        }));
//
//        Button codeButton = findViewById(R.id.codeButton);
//        codeButton.setOnClickListener((view -> {
//            String taskTitle = codeButton.getText().toString();
//            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
//            goToTaskDetail.putExtra("taskName", taskTitle);
//            startActivity(goToTaskDetail);
//        }));
//
//        Button readButton = findViewById(R.id.readButton);
//        readButton.setOnClickListener((view -> {
//            String taskTitle = readButton.getText().toString();
//            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
//            goToTaskDetail.putExtra("taskName", taskTitle);
//            startActivity(goToTaskDetail);
//        }));

    }

    @Override
    protected void onResume() {
        super.onResume();

        String usernameTasks = "’s tasks";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = sharedPreferences.getString("username", "Your");
        TextView userTasks = findViewById(R.id.myTask);
        userTasks.setText(username + usernameTasks);

        String chooseTeamName = sharedPreferences.getString("teamName", "Choose a team");
        TextView teamName = findViewById(R.id.teamName);
        teamName.setText(chooseTeamName);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(getApplicationContext(), "Override onStart()", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(getApplicationContext(), "Override onPause()", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(getApplicationContext(), "Override onStop()", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Toast.makeText(getApplicationContext(), "Override onRestart()", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(), "Override onDestroy()", Toast.LENGTH_SHORT).show();
//    }

}