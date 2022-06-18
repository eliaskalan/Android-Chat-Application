package com.example.ds_2;

import static utils.socketMethods.closeEverything;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controller.Address;
import controller.Client;

public class Chat extends AppCompatActivity {
    private Intent intent;
    private ListView messageList;
    private static MessageAdapter messageAdapter;
    private ChatConnect chat;
    private String brokerIp;
    private String brokerPort;
    private Client client;
    private String name;
    private String topic;
    String username;
    String context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        topic = getIntent().getStringExtra("TOPIC_FULL_NAME");
        brokerIp = getIntent().getStringExtra("BROKER_IP");
        brokerPort = getIntent().getStringExtra("BROKER_PORT");
        String topicId = getIntent().getStringExtra("TOPIC_ID");
        name = getIntent().getStringExtra("USERNAME");
        setTitle(topic + " | " + brokerIp + ":" + brokerPort);

        messageList = findViewById(R.id.messageListView);
        final List<MessageItem> messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.item_message, messages);
        messageList.setAdapter(messageAdapter);
        chat = new ChatConnect();
        chat.execute();
    }

    public class ChatConnect extends AsyncTask<String,String ,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            try {
                client = new Client(new Address(brokerIp, Integer.parseInt(brokerPort)),name);
                System.out.println(name);
                client.initialBroker(topic);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String msgFromGroupChat;
                        while (client.consumer.socketIsConnected()) {
                            try {
                                msgFromGroupChat = client.consumer.readMessage();
                                String[] message =msgFromGroupChat.split(":");
                                for(int i=0; i < message.length; i++){
                                    if(i == 0){
                                        username = message[i];
                                    }else{
                                        context = message[i];
                                    }
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messageAdapter.add(new MessageItem(name, context));
                                    }
                                });
                            } catch (IOException e) {
                                closeEverything(client.consumer.getSocket(), client.consumer.getBufferedReader());
                            }
                        }
                    }
                }).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}