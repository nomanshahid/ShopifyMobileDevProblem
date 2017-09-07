package com.nomanshahid.shopifymobiledevproblem;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

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

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(shopifyURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Add toast to main thread here
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String jsonData = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            Log.v("MainActivity", "TOTAL SPENT: " +
                                    String.valueOf(getCustomerInfo(jsonObject, "Napoleon", "Batz")));
                            Log.v("MainActivity", "TOTAL QUANTITY: " +
                                    String.valueOf(getProductDetails(jsonObject, "Awesome Bronze Bag")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "There was an error. Please try again.",
                                Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No network available! Please connect and " +
                            "try again.",
                    Toast.LENGTH_LONG).show();
        }


    }

    private double getCustomerInfo(JSONObject jsonObject, String firstName, String lastName) throws JSONException {
        double totalSpent = 0;
        JSONArray orders = jsonObject.getJSONArray("orders");
        for (int i = 0; i < orders.length(); ++i) {
            JSONObject order = orders.getJSONObject(i);
            JSONObject customer = order.optJSONObject("customer");
            // It seems the API returns an incorrect value for customer.total_spent ... will calculate manually
            if (customer != null) {
                if (Objects.equals(customer.getString("first_name"), firstName) &&
                        Objects.equals(customer.getString("last_name"), lastName) &&
                        Objects.equals(order.getString("currency"), "CAD")) {
                    totalSpent += order.getDouble("total_price");
                }
            }
        }
        return totalSpent;
    }

    private int getProductDetails(JSONObject jsonObject, String productName) throws JSONException {
        int totalQuantity = 0;
        JSONArray orders = jsonObject.getJSONArray("orders");
        for (int i = 0; i < orders.length(); ++i) {
            JSONObject order = orders.getJSONObject(i);
            JSONArray lineItems = order.getJSONArray("line_items");
            for (int j = 0; j < lineItems.length(); ++j) {
                JSONObject lineItem = lineItems.getJSONObject(j);
                if(Objects.equals(lineItem.getString("title"), productName)) {
                    totalQuantity += lineItem.getInt("quantity");
                }

            }
        }
        return totalQuantity;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;

    }
}
