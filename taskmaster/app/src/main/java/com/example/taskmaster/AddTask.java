package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class AddTask extends AppCompatActivity {

    private Intent chooseFile;
    private String imgName;
    private Uri imgData;
    public double longitude;
    public double latitude;
    public String cityName;
    public String countryName;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(AddTask.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                cityName = addresses.get(0).getLocality();
                                countryName = addresses.get(0).getCountryName();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

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


                RadioGroup locationGroup = findViewById(R.id.locationGroup);
                int chosenLocationId = locationGroup.getCheckedRadioButtonId();
                RadioButton chosenLocationButton = findViewById(chosenLocationId);
                String chosenLocation = chosenLocationButton.getText().toString();
                com.amplifyframework.datastore.generated.model.Location taskLocation;

                switch (chosenLocation) {
                    case "Save By City Name":
                        taskLocation = new com.amplifyframework.datastore.generated.model.Location(cityName, longitude, latitude);
                        break;
                    case "Save By Country Name":
                        taskLocation = new com.amplifyframework.datastore.generated.model.Location(countryName, longitude, latitude);
                        break;
                    default:
                        taskLocation = null;
                        break;
                }

                TaskTodo todo = TaskTodo.builder()
                        .title(title.getText().toString())
                        .body(body.getText().toString())
                        .state(state.getText().toString())
                        .image(imgName)
                        .taskTeam((Team) teamsList.get(chosenTeam))
                        .location(taskLocation)
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