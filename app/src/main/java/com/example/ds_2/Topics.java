package com.example.ds_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.Address;
import controller.Client;
import utils.Config;

public class Topics extends AppCompatActivity {
    Client client;
    private ListView topicsButtonListView;
    private TopicsButtonAdapter topicsButtonAdapter;
    private Intent intent;
    Login login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        Bundle extras = getIntent().getExtras();
        intent = new Intent(Topics.this,Chat.class);
        if (extras != null) {
            String name = extras.getString("USERNAME");
            //The key argument here must match that used in the other activity
            login = new Login();
            login.execute(name);
        }
        topicsButtonListView = findViewById(R.id.buttonListView);
        final List<TopicButtonMessage> buttons = new ArrayList<>();
        topicsButtonAdapter = new TopicsButtonAdapter(this, R.layout.item_message, buttons);
        topicsButtonListView.setAdapter(topicsButtonAdapter);
    }
    public void goToChat(View view) throws IOException {
        Button b = (Button) view;
        String topicFullName = b.getText().toString();
        intent.putExtra("TOPIC_FULL_NAME", topicFullName);
        String topicId = Character.toString(topicFullName.replace(" ", "").charAt(0));
        intent.putExtra("TOPIC_ID", topicId);
        new Thread(new Runnable(){
            public void run() {
                try {
                    Address address = login.getBroker(topicId);
                    intent.putExtra("BROKER_IP", address.getIp());
                    intent.putExtra("BROKER_PORT", Integer.toString(address.getPort()));
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public class Login extends AsyncTask<String,String ,String>
    {

        public Address getBroker(String topicId) throws IOException {
            client.publisher.sendOneTimeMessage(topicId);
            String topicName = client.consumer.listenForMessageOneTime();
            Address address = client.getBrokerAddress();
            return address;
        }

        @Override
        protected String doInBackground(String... strings) {


            try {

                client = new Client(Config.ZOOKEEPER_CLIENTS,strings[0]);
                client.initialConnectWithZookeeper(strings[0]);
                String localTopics = client.getTopic();
                String[] topics =localTopics.split("~");

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