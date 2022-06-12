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


public class MessageAdapter extends ArrayAdapter<MessageItem> {

    public MessageAdapter(Context context, int resource, List<MessageItem> objects) {
        super(context, resource, objects);
    }

    public MessageAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }
        MessageItem message = getItem(position);
        TextView messageTextView =  convertView.findViewById(R.id.messageTextView);
        TextView nameTextView =  convertView.findViewById(R.id.nameTextView);


        messageTextView.setVisibility(View.VISIBLE);
        messageTextView.setText(message.getMessage());
        nameTextView.setVisibility(View.VISIBLE);
        nameTextView.setText(message.getName());


        return convertView;
    }

}
