package com.example.ds_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {
    private Intent intent;
    private ListView messageList;
    private MessageAdapter messageAdapter;
    private ChatConnect chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String topic = getIntent().getStringExtra("TOPIC_FULL_NAME");
        String brokerIp = getIntent().getStringExtra("BROKER_IP");
        String brokerPort = getIntent().getStringExtra("BROKER_PORT");
        String topicId = getIntent().getStringExtra("TOPIC_ID");
        setTitle(topic + " | " + brokerIp + ":" + brokerPort);

        messageList = findViewById(R.id.messageListView);
        final List<MessageItem> messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, R.layout.item_message, messages);
        messageList.setAdapter(messageAdapter);
        messageAdapter.add(new MessageItem("Giannis", "Hello everyone!"));
        messageAdapter.add(new MessageItem("Elias", "Hello Gianni!"));
        chat = new ChatConnect();
        chat.execute();
    }

    public class ChatConnect extends AsyncTask<String,String ,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            messageAdapter.add(new MessageItem("Giannis", "Elias this message is from bg!"));
            return null;
        }
    }
}