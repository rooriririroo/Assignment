package com.example.soyeonlee.myapplication11;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button searchButton;
    String clientId = "_pi7iVhpztQUTQJOp6_J";
    String clientSecret="sPTIUdUInA";
    RequestQueue requestQueue;

    RecyclerItemAdapter adapter;
    ArrayList<RecyclerItem> recyclerItemArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    int display = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(this);

        editText = (EditText) findViewById(R.id.editText);
        searchButton = (Button) findViewById(R.id.searchButton);

        recyclerItemArrayList = new ArrayList<RecyclerItem>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerItemArrayList.clear();
                sendRequest();
            }
        });

        adapter = new RecyclerItemAdapter(this,recyclerItemArrayList);
        recyclerView.setAdapter(adapter);

    }

    public void sendRequest() {
        String url="https://openapi.naver.com/v1/search/movie.json?query="+editText.getText().toString()+"&display="+display;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                            processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //recyclerItemArrayList.clear();
                        Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
                        //Log.d("Error Message =>", ""+ error);

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("X-Naver-Client-Id",clientId);
                params.put("X-Naver-Client-Secret",clientSecret);
                //Log.d("getHedaer =>", ""+ params);
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void processResponse(String response) {
        if (response.contains(editText.getText().toString())) {
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(response);
            JsonArray memberArray = (JsonArray) jsonObject.get("items");

            for (int i = 0; i < memberArray.size(); i++) {
                JsonObject object = (JsonObject) memberArray.get(i);

                String image = object.get("image").getAsString();
                String title = object.get("title").getAsString();
                float userRating = object.get("userRating").getAsFloat() * (float) 0.5;
                String pubDate = object.get("pubDate").getAsString();
                String director = object.get("director").getAsString();
                String actor = object.get("actor").getAsString();
                String link = object.get("link").getAsString();

                recyclerItemArrayList.add(new RecyclerItem(image, title, userRating, pubDate, director, actor, link));
                //Log.d("getContent=>", object.get("userRating").getAsString());
            }
            adapter.notifyDataSetChanged();
        }

        else {
            Toast.makeText(getApplicationContext(),"찾으시는 결과가 없습니다.",Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    }
}
