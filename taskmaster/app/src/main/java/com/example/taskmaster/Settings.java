package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskTodo;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener((View -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            EditText usernameInput = findViewById(R.id.usernameInput);
            String username = usernameInput.getText().toString();
            sharedPreferencesEditor.putString("username", username);

            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            int chosenButtonId = radioGroup.getCheckedRadioButtonId();
            RadioButton chosenButton = findViewById(chosenButtonId);
            String teamName = chosenButton.getText().toString();
            sharedPreferencesEditor.putString("teamName", teamName);
            sharedPreferencesEditor.putString("teamId", teamsList.get(teamName));

            sharedPreferencesEditor.apply();
            Intent goHomeSetting = new Intent(Settings.this, MainActivity.class);
            startActivity(goHomeSetting);
        }));

    }
}