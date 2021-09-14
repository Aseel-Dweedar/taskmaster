package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddTask extends AppCompatActivity {

    private Intent chooseFile;
    private String imgName;
    private Uri imgData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = findViewById(R.id.buttonAddTask);
        EditText title = findViewById(R.id.editTextTaskTitle);
        EditText body = findViewById(R.id.editTextDescription);
        EditText state = findViewById(R.id.editTextTaskState);

        Map<String, Team> teamsList = new HashMap<>();
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Team.class),
                response -> {
                    for (Team oneTeam : response.getData()) {
                        teamsList.put(oneTeam.getName(), oneTeam);
                    }
                },
                error -> Log.e("MyAmplifyApp", error.toString(), error)
        );

        Button addPhoto = findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(v -> {

            chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(chooseFile, 1234);

        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {

                if (imgData != null) {
                    try {
                        InputStream exampleInputStream = getContentResolver().openInputStream(imgData);
                        Amplify.Storage.uploadInputStream(
                                imgName,
                                exampleInputStream,
                                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                        );
                    } catch (FileNotFoundException error) {
                        Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
                    }
                }

                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                int chosenButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton chosenButton = findViewById(chosenButtonId);
                String chosenTeam = chosenButton.getText().toString();

                TaskTodo todo = TaskTodo.builder()
                        .title(title.getText().toString())
                        .body(body.getText().toString())
                        .state(state.getText().toString())
                        .image(imgName)
                        .taskTeam((Team) teamsList.get(chosenTeam))
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(todo),
                        response2 -> {
                            Log.i("MyAmplifyApp", "Added Todo with id: " + response2.getData().getId());
                            Intent goToHome = new Intent(AddTask.this, MainActivity.class);
                            startActivity(goToHome);
                        },
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = new File(data.getData().getPath());
        imgName = file.getName();
        imgData = data.getData();
    }
}