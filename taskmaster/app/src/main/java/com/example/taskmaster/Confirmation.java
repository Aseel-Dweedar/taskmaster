package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;

public class Confirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        EditText confirmationInput = findViewById(R.id.confirmationInput);
        EditText usernameConfirm = findViewById(R.id.usernameConfirm);
        Button confirmButton = findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(v -> {
            Amplify.Auth.confirmSignUp(
                    usernameConfirm.getText().toString(),
                    confirmationInput.getText().toString(),
                    result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                    error -> Log.e("AuthQuickstart", error.toString())
            );

            Intent goHome = new Intent(Confirmation.this, MainActivity.class);
            startActivity(goHome);
        });
    }
}