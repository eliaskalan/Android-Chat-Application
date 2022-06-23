package com.example.ds_2;

import static utils.socketMethods.closeEverything;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText editText;
    private Button sendButton;
    String otherClientUsername;
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
        editText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = editText.getText().toString();
                editText.setText("");
                writeOnAndroid(name, userMessage);
                sendMessage(userMessage);
            }
        });
        chat = new ChatConnect();
        chat.execute();
    }

    private void writeOnAndroid(String nameToWrite, String contextToWrite){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.add(new MessageItem(nameToWrite, contextToWrite));
            }
        });
    }
    private void sendMessage(String message){
        new Thread(new Runnable(){
            public void run() {
                try {
                    client.publisher.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public class ChatConnect extends AsyncTask<String,String ,String>
    {

        @Override
        protected String doInBackground(String... strings) {

            try {
                client = new Client(new Address(brokerIp, Integer.parseInt(brokerPort)),name);
                client.initialBroker(topic);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        otherClientUsername = "";
                        context = "";
                        String msgFromGroupChat;
                        while (client.consumer.socketIsConnected()) {
                            try {
                                msgFromGroupChat = client.consumer.readMessage();
                                String[] message =msgFromGroupChat.split(":");

                                for(int i=0; i < message.length; i++){
                                    if(i == 0){
                                        otherClientUsername = message[i];
                                    }else{
                                        context = message[i];
                                    }
                                }

                                if(!otherClientUsername.equals("")){
                                    writeOnAndroid(otherClientUsername, context);
                                }


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