package com.excilys.mlemaile.application.chat.chatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by excilys on 06/04/17.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, List<Message> messages){
        super(context,0,messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_message,parent, false);
        }

        TextView pseudoTextView = (TextView) convertView.findViewById(R.id.messagePseudo);
        TextView textTextView = (TextView) convertView.findViewById(R.id.messageText);
        Message message = getItem(position);
        pseudoTextView.setText(message.getLogin());
        textTextView.setText(message.getMessage());
        MessageViewHolder viewHolder = (MessageViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MessageViewHolder();
            viewHolder.pseudo = (TextView) convertView.findViewById(R.id.messagePseudo);
            viewHolder.text = (TextView) convertView.findViewById(R.id.messageText);
            convertView.setTag(viewHolder);
        }

        viewHolder.pseudo.setText(message.getLogin());
        viewHolder.text.setText(message.getMessage());
        return convertView;
    }
}
