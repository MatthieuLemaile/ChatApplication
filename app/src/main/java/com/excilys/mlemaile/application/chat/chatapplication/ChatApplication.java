package com.excilys.mlemaile.application.chat.chatapplication;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by excilys on 06/04/17.
 */

public class ChatApplication extends Application {

    private String baseUrl;
    private Retrofit.Builder builder;
    private Retrofit retrofit;


    @Override
    public void onCreate() {
        super.onCreate();
        baseUrl = "http://10.0.1.131:9000/2.0/";
        builder = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create());
    }

    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public <S> S createService(Class<S> serviceClass) {

        return createService(serviceClass, null, null);
    }

    public <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null, null);
    }

    public <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);

                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }

    public boolean isConnected(Activity activity){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
            if(ni!=null) {
                NetworkInfo.State state = connectivityManager.getActiveNetworkInfo().getState();
                if (state != null && state.equals(NetworkInfo.State.CONNECTED)) {
                    connected = true;
                }
            }
        }
        return connected;
    }
}
