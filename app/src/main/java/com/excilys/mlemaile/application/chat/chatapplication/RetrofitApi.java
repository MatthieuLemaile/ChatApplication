package com.excilys.mlemaile.application.chat.chatapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by excilys on 06/04/17.
 */

public interface RetrofitApi {
    public static final String END_POINT = "http://10.0.1.131:9000/2.0/";

    @POST("messages")
    Call<Message> sendMessage(@Body Message message);

    @GET("connect")
    Call<User> basicLogin();

    @GET("messages")
    Call<List<Message>> getMessages();

    @POST("register")
    Call<User> register(@Body User user                                                                                         );
}
