package com.nomanshahid.shopifymobiledevproblem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String shopifyURL = "https://shopicruit.myshopify.com/admin/orders.json?page=1&" +
                "access_token=c32313df0d0ef512ca64d5b336a0d7c6";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(shopifyURL)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                }

            }
        });


    }
}
