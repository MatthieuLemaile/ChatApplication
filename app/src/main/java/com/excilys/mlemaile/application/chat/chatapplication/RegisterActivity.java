package com.excilys.mlemaile.application.chat.chatapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextUsername = null;
    private EditText editTextPassword = null;
    private EditText editTextPassword2 = null;
    private TextView resultRegister = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextUsername = (EditText) findViewById(R.id.registerUsername);
        editTextPassword = (EditText) findViewById(R.id.registerPassword);
        editTextPassword2 = (EditText) findViewById(R.id.registerPassword2);
        resultRegister = (TextView) findViewById(R.id.resultRegister);
        findViewById(R.id.buttonFormRegister).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonFormRegister:
                resultRegister.setText("");
                String password = editTextPassword.getText().toString();
                if(password.equals(editTextPassword2.getText().toString())) {
                    User user = new User();
                    user.setLogin(editTextUsername.getText().toString());
                    user.setPassword(password);
                    if (((ChatApplication) getApplicationContext()).isConnected(this)) {
                        ((ChatApplication) getApplicationContext()).createService(RetrofitApi.class,user.getLogin(),password).register(user).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if(response.code()==200){
                                    Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_LONG).show();
                                    finish();
                                }else{
                                    resultRegister.setText("Echec de l'inscription");
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                resultRegister.setText("Echec de l'inscription");
                                Log.d("Error", t.getMessage());
                            }
                        });
                    } else {
                        resultRegister.setText("Pas de connexion internet");
                    }
                }else{
                    resultRegister.setText("Ce ne sont pas les même mots de passe");
                }
                break;
        }
    }
}
