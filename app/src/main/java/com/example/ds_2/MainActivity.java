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
    Socket socket;





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

                    Login login = new Login();
                    login.execute(name);
                    //login.doInBackground(name);
                    nextActivity(v);
                }
            }
        });

    }


public class Login extends AsyncTask<String,String ,String>
{

    private void login(final String... strings)
    {

    }

    @Override
    protected String doInBackground(String... strings) {


        try {

            Client client = new Client(Config.ZOOKEEPER_CLIENTS,strings[0]);
            client.initialConnectWithZookeeperAndGetTopic(strings[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}


    public void nextActivity(View v)
    {
        Intent intent = new Intent(this,Topics.class);
        startActivity(intent);
    }

}
