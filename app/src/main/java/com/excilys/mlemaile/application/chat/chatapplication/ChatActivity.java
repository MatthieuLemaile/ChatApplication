package com.excilys.mlemaile.application.chat.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextMessage = null;
    SharedPreferences sharedPref = null;
    private ListView listView = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        editTextMessage = (EditText) findViewById(R.id.editTextSendMessage);
        listView = (ListView) findViewById(R.id.listMessages);
        sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        findViewById(R.id.buttonSendMessage).setOnClickListener(this);
        findViewById(R.id.buttonDisconnection).setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_message);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessage();
            }
        });
        getMessage();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonSendMessage:
                Message message = new Message();
                message.setMessage(editTextMessage.getText().toString());
                editTextMessage.setText("");
                message.setLogin(sharedPref.getString(LoginActivity.PREF_USERNAME,""));
                if(((ChatApplication) getApplicationContext()).isConnected(this)) {
                    ((ChatApplication) getApplicationContext()).createService(RetrofitApi.class, sharedPref.getString(LoginActivity.PREF_USERNAME, ""), sharedPref.getString(LoginActivity.PREF_PASSWORD, "")).sendMessage(message).enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if(response.code()==200) {
                                Toast.makeText(ChatActivity.this, "Sent", Toast.LENGTH_LONG).show();
                                getMessage();
                            }else{
                                Toast.makeText(ChatActivity.this, "Not sent", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                            Toast.makeText(ChatActivity.this, "Fail", Toast.LENGTH_LONG).show();
                            Log.d("Error", t.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(ChatActivity.this, "Disconnected", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.buttonDisconnection:
                SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                sharedPref.edit().clear().apply();
                finish();
                break;
        }
    }

    public void getMessage(){
        if(((ChatApplication) getApplicationContext()).isConnected(this)) {
            ((ChatApplication) getApplicationContext()).createService(RetrofitApi.class, sharedPref.getString(LoginActivity.PREF_USERNAME, ""), sharedPref.getString(LoginActivity.PREF_PASSWORD, "")).getMessages().enqueue(new Callback<List<Message>>() {
                @Override
                public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                    if(response.code()==200){
                        listView.setAdapter(new MessageAdapter(ChatActivity.this,response.body()));
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<List<Message>> call, Throwable t) {

                }
            });
        }else{
            Toast.makeText(ChatActivity.this, "Disconnected", Toast.LENGTH_LONG).show();
        }
    }

}
