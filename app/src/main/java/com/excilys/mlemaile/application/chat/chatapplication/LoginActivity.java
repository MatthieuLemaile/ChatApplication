package com.excilys.mlemaile.application.chat.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String KEY_CONNECTION_RESULT = "keyConnectionResult";
    public static final String KEY_USERNAME = "keyUsername";
    public static final String PREF_USERNAME = "prefUsername";
    public static final String PREF_PASSWORD = "prefPassword";
    private EditText editTextUsername = null;
    private EditText editTextPassword = null;
    private ProgressBar progressBar = null;
    private TextView textViewResultConnexion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = (EditText) findViewById(R.id.loginUserName);
        editTextPassword = (EditText) findViewById(R.id.loginPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewResultConnexion = (TextView) findViewById(R.id.resultConnexion);
        findViewById(R.id.buttonConnexion).setOnClickListener(this);
        findViewById(R.id.buttonRegister).setOnClickListener(this);
        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
//        if(sharedPref.contains(PREF_USERNAME)){
//            launchLoggedActivity();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonConnexion:
                textViewResultConnexion.setText("");
                if(((ChatApplication) getApplicationContext()).isConnected(this)) {
                    ((ChatApplication) getApplicationContext()).createService(RetrofitApi.class, editTextUsername.getText().toString(), editTextPassword.getText().toString()).basicLogin().enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                connected();
                            } else {
                                textViewResultConnexion.setText("La connexion a échoué");
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            textViewResultConnexion.setText("La connexion a échoué");
                            Log.d("Error", t.getMessage());
                        }
                    });
                }else{
                    textViewResultConnexion.setText("Pas de connexion internet");
                }
                break;
            case R.id.buttonRegister:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void launchLoggedActivity(){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONNECTION_RESULT,textViewResultConnexion.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        textViewResultConnexion.setText(savedInstanceState.getString(KEY_CONNECTION_RESULT));
    }

    private void connected(){
        Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
        SharedPreferences sharedPref = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_USERNAME, editTextUsername.getText().toString());
        editor.putString(PREF_PASSWORD,editTextPassword.getText().toString());
        editor.apply();
        launchLoggedActivity();
    }

}
