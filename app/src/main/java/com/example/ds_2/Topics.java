package com.example.ds_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.Socket;

import controller.Client;
import utils.Config;

public class Topics extends AppCompatActivity {
    Button confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        Bundle extras = getIntent().getExtras();
        Intent intent = new Intent(Topics.this,Chat.class);
        if (extras != null) {
            String name = extras.getString("USERNAME");
            //The key argument here must match that used in the other activity
            Login login = new Login();
            login.execute(name);
        }
        confirmButton = findViewById(R.id.button2);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(intent);
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
                client.initialConnectWithZookeeper(strings[0]);
                String localTopics = client.getTopic();
                System.out.println(localTopics);
                String[] topics =localTopics.split("~");

                LinearLayout layout = findViewById((R.id.linearLayout));
                //Button[] button= new Button[6];
                int count = layout.getChildCount();
                //int lastPosition = 0;

                for (int i=0; i<count; i++)
                {
                    View view = layout.getChildAt(i);
                    if(view instanceof Button)
                    {
                        ((Button) view).setText(topics[i]);
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public void nextActivity(View v)
    {
        Intent intent = new Intent(this,Chat.class);
        startActivity(intent);
    }
}