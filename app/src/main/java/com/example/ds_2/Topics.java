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
    private String name;
    Login login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        setTitle("Topics");
        Bundle extras = getIntent().getExtras();
        intent = new Intent(Topics.this,Chat.class);
        if (extras != null) {
            name = extras.getString("USERNAME");
            login = new Login();
            login.execute(name);
        }
        topicsButtonListView = findViewById(R.id.buttonListView);
        final List<TopicButtonMessage> buttons = new ArrayList<>();
        topicsButtonAdapter = new TopicsButtonAdapter(this, R.layout.topic_buttons, buttons);
        topicsButtonListView.setAdapter(topicsButtonAdapter);
    }
    public void goToChat(View view) throws IOException {
        Button b = (Button) view;
        String topicFullName = b.getText().toString();
        intent.putExtra("TOPIC_FULL_NAME", topicFullName);
        intent.putExtra("USERNAME", name);
        String topicId = Character.toString(topicFullName.replace(" ", "").charAt(0));
        intent.putExtra("TOPIC_ID", topicId);
        new Thread(new Runnable(){
            public void run() {
                try {
                    if(client.getAddress() != Config.ZOOKEEPER_CLIENTS || !client.consumer.socketIsConnected()){
                        System.out.println("hello");
                        login = new Login();
                        login.execute(name);
                    }
                    Address address = login.getBroker(topicId);
                    intent.putExtra("BROKER_IP", address.getIp());
                    intent.putExtra("BROKER_PORT", Integer.toString(address.getPort()));
                    startActivity(intent);
                    client.hasTopic = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    public class Login extends AsyncTask<String,String ,String>
    {

        public Address getBroker(String topicId) throws IOException {
            if(client.hasTopic){
                client.closeSocket();
                login(name);
                client.getTopic();
            }
            client.hasTopic = false;
            client.publisher.sendOneTimeMessage(topicId);
            String topicName = client.consumer.listenForMessageOneTime();
            intent.putExtra("TOPIC_NAME", topicName);
            Address address = client.getBrokerAddress();
            return address;
        }

        @Override
        protected String doInBackground(String... strings) {


            try {
                login(strings[0]);
                String localTopics = client.getTopic();
                String[] topics =localTopics.split("~");

                for(int i = 0; i < topics.length; i++){
                    String text = topics[i];
                    if(!(text.replace(" ", "")).equals("")){
                        System.out.println(text);

                        new Thread(new Runnable(){
                            public void run() {
                                topicsButtonAdapter.add(new TopicButtonMessage(text));
                            }
                        }).start();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        public void login(String name) throws IOException {
            client = new Client(Config.ZOOKEEPER_CLIENTS, name);
            client.initialConnectWithZookeeper(name);
        }
    }
}