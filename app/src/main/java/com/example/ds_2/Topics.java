package com.example.ds_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.IOException;
import java.net.Socket;

import controller.Client;
import utils.Config;

public class Topics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String name = extras.getString("USERNAME");
            //The key argument here must match that used in the other activity
            Login login = new Login();
            login.execute(name);


        }
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
                client.initialConnectWithZookeeper(strings[0]);

                System.out.println(client.getTopic());

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}