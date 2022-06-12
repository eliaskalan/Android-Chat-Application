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

public class Chat extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String topic = getIntent().getStringExtra("TOPIC_FULL_NAME");
        String brokerIp = getIntent().getStringExtra("BROKER_IP");
        String brokerPort = getIntent().getStringExtra("BROKER_PORT");
        String topicId = getIntent().getStringExtra("TOPIC_ID");
        setTitle(topic + " | " + brokerIp + ":" + brokerPort);
    }

    public class ChatConnect extends AsyncTask<String,String ,String>
    {

        @Override
        protected String doInBackground(String... strings) {




            return null;
        }
    }
}