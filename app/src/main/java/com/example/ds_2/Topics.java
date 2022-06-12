package com.example.ds_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

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
                String localTopics = client.getTopic();
                System.out.println(localTopics);
                String[] topics =localTopics.split("~");


                Button mButton = findViewById(R.id.button2);
                mButton.setText(topics[1]);
                Button mButton2 =findViewById(R.id.button3);
                mButton2.setText(topics[2]);
                Button mButton3 = findViewById(R.id.button4);
                mButton3.setText(topics[3]);
                Button mButton4 =findViewById(R.id.button5);
                mButton4.setText(topics[4]);
                Button mButton5 = findViewById(R.id.button6);
                mButton5.setText(topics[5]);
                Button mButton6 =findViewById(R.id.button7);
                mButton6.setText(topics[6]);






            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}