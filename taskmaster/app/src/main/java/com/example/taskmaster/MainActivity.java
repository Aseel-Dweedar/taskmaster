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

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTodo;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());

            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        Amplify.Auth.signInWithWebUI(
                this,
                result -> Log.i("AuthQuickStart", result.toString()),
                error -> Log.e("AuthQuickStart", error.toString())
        );


        Amplify.Auth.fetchAuthSession(
                result -> Log.i("AmplifyQuickstart", result.toString()),
                error -> Log.e("AmplifyQuickstart", error.toString())
        );


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String teamId = sharedPreferences.getString("teamId", "");

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

        Button signoutButton = findViewById(R.id.signoutButton);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Amplify.Auth.signOut(
                        () -> {
                            Log.i("AuthQuickstart", "Signed out successfully");
                            recreate();
//                            finish();
//                            startActivity(getIntent());
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recreate();
                    }
                }, 2000);
            }
        });
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

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AuthUser authUser = Amplify.Auth.getCurrentUser();
                TextView currentUser = findViewById(R.id.currentUser);
                if (authUser != null) currentUser.setText(authUser.getUsername());

            }
        }, 2000);
    }
}