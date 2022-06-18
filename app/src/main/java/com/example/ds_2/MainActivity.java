package com.example.ds_2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import controller.Client;
import utils.Config;

public class MainActivity extends AppCompatActivity {


    Button confirmButton;
    EditText username;
    SocketHandler socketHandler = new SocketHandler();





    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Aueb Chat");
        Intent intent = new Intent(MainActivity.this,Topics.class);
        confirmButton = findViewById(R.id.button);

        username =  findViewById(R.id.editTextTextPersonName);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                if(name!=null && !name.equals(""))
                {

                    intent.putExtra("USERNAME",name);
                    startActivity(intent);
                    //Login login = new Login();
                    //login.execute(name);
                    //login.doInBackground(name);
                    //nextActivity(v);
                }
            }
        });

    }


public class Initial extends AsyncTask<String,String ,String>
{
    @Override
    protected String doInBackground(String... strings) {


        try {
            socketHandler.setSocket(new Socket("10.0.2.2",22346));
            Client client = new Client(Config.ZOOKEEPER_CLIENTS,strings[0]);
            client.initialConnectWithZookeeper(strings[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
}
