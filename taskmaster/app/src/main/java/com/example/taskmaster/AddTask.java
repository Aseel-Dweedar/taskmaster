package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTodo;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddTask extends AppCompatActivity {
    //    AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = findViewById(R.id.buttonAddTask);
        EditText title = findViewById(R.id.editTextTaskTitle);
        EditText body = findViewById(R.id.editTextDescription);
        EditText state = findViewById(R.id.editTextTaskState);

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        Map< String,String> teamsList = new HashMap<>();
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Team.class),
                response -> {
                    for (Team oneTeam : response.getData()) {
                        teamsList.put(oneTeam.getName(), oneTeam.getId());
                    }
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {

                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                int chosenButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton chosenButton = findViewById(chosenButtonId);
                String chosenTeam = chosenButton.getText().toString();

                Amplify.API.query(
                        ModelQuery.get(Team.class, teamsList.get(chosenTeam)),
                        response -> {
                            Log.i("MyAmplifyApp", ((Team) response.getData()).getName());

                            TaskTodo todo = TaskTodo.builder()
                                    .title(title.getText().toString())
                                    .body(body.getText().toString())
                                    .state(state.getText().toString()).taskTeam((Team) response.getData())
                                    .build();

                            Amplify.API.mutate(
                                    ModelMutation.create(todo),
                                    response2 -> Log.i("MyAmplifyApp", "Added Todo with id: " + response2.getData().getId()),
                                    error -> Log.e("MyAmplifyApp", "Create failed", error)
                            );
                        },
                        error -> Log.e("MyAmplifyApp", error.toString(), error)
                );

                Toast.makeText(getApplicationContext(), "submitted!", Toast.LENGTH_SHORT).show();
                Intent goToHome = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToHome);

//                Task newTask = new Task(title.getText().toString(), body.getText().toString(), state.getText().toString());
//                appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasksDatabase").allowMainThreadQueries().build();
//                appDatabase.taskDao().insertAll(newTask);

            }
        });

        //        Button homeButton = findViewById(R.id.homeAddTask);
//        homeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View V) {
//                Intent goToHome = new Intent(AddTask.this, MainActivity.class);
//                startActivity(goToHome);
//            }
//        });

    }
}