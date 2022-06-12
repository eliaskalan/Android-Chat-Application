package com.example.ds_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import controller.Client;
import utils.Config;

public class Topics extends AppCompatActivity {
    Button confirmButton;
    private ListView topicsButtonListView;
    private TopicsButtonAdapter topicsButtonAdapter;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        Bundle extras = getIntent().getExtras();
        intent = new Intent(Topics.this,Chat.class);
        if (extras != null) {
            String name = extras.getString("USERNAME");
            //The key argument here must match that used in the other activity
            Login login = new Login();
            login.execute(name);
        }
        topicsButtonListView = findViewById(R.id.buttonListView);
        final List<TopicButtonMessage> buttons = new ArrayList<>();
        topicsButtonAdapter = new TopicsButtonAdapter(this, R.layout.item_message, buttons);
        topicsButtonListView.setAdapter(topicsButtonAdapter);
    }
    public void goToChat(View view){
        Button b = (Button) view;
        this.intent.putExtra("TOPIC_ID", b.getText().toString());
        startActivity(intent);
    }

    public class Login extends AsyncTask<String,String ,String>
    {

        @Override
        protected String doInBackground(String... strings) {


            try {

                Client client = new Client(Config.ZOOKEEPER_CLIENTS,strings[0]);
                client.initialConnectWithZookeeper(strings[0]);
                String localTopics = client.getTopic();
                System.out.println(localTopics);
                String[] topics =localTopics.split("~");

                LinearLayout layout = findViewById((R.id.linearLayout));
                for(int i = 0; i < topics.length; i++){
                    String text = topics[i];
                    if(!(text.replace(" ", "")).equals("")){
                        topicsButtonAdapter.add(new TopicButtonMessage((topics[i])));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}