package com.example.cs361final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {
    private TextView username, gmail;
    private Button logoutBTN;
    String uname, pass;
    SharedPreferences sharedPreferences;

    protected void onCreate(Bundle SaveInstanceState) {
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.profile_page);
        username = findViewById(R.id.username);
        gmail = findViewById(R.id.gmail);
        logoutBTN = findViewById(R.id.LogoutButton);
        sharedPreferences = getSharedPreferences("App", MODE_PRIVATE);
        if(sharedPreferences.getString("logged", "false").equals("false")) {
            Intent L = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(L);
            finish();
        }

        username.setText(sharedPreferences.getString("username", ""));
        gmail.setText(sharedPreferences.getString("gmail", ""));

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://10.0.2.2:8080/Logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("username", "");
                                    editor.putString("gmail", "");
                                    editor.putString("apiKey", "");
                                    editor.apply();
                                    Intent P = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(P);
                                    finish();
                                } else {
                                    Toast.makeText(ProfilePage.this, response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username", sharedPreferences.getString("username", ""));
                        paramV.put("apiKey", sharedPreferences.getString("apiKey", ""));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}