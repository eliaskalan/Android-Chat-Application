package com.example.ds_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;



public class MainActivity extends AppCompatActivity {


    Button confirmButton;
    EditText username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmButton = findViewById(R.id.button);

        username =  findViewById(R.id.editTextTextPersonName);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                if(name!=null)
                {
                  nextActivity(v);
                }
            }
        });

    }




    public void nextActivity(View v)
    {
        Intent intent = new Intent(this,Topics.class);
        startActivity(intent);
    }

}
