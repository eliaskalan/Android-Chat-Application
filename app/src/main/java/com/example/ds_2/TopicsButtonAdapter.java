package com.example.ds_2;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;


public class TopicsButtonAdapter extends ArrayAdapter<TopicButtonMessage> {

    public TopicsButtonAdapter(Context context, int resource, List<TopicButtonMessage> objects) {
        super(context, resource, objects);
    }

    public TopicsButtonAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.topic_buttons, parent, false);
        }
        TopicButtonMessage message = getItem(position);
        TextView messageTextView =  convertView.findViewById(R.id.buttonTopic);


        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getText());


        return convertView;
    }

}
