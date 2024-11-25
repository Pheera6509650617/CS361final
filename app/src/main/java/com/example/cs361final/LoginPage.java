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

public class LoginPage extends AppCompatActivity {
    private EditText uname, pass;
    private Button loginBTN, registerBTN;
    private TextView warn;
    String username, password, apiKey, gmail;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        uname = findViewById(R.id.usernameInput);
        pass = findViewById(R.id.passwordInput);
        warn = findViewById(R.id.warning);
        loginBTN = findViewById(R.id.loginButton);
        registerBTN = findViewById(R.id.registerButton);
        sharedPreferences = getSharedPreferences("App", MODE_PRIVATE);

        if(sharedPreferences.getString("logged", "false").equals("true")) {
            Intent P = new Intent(getApplicationContext(), FeedPage.class);
            startActivity(P);
            finish();
        }

        registerBTN.setOnClickListener(view -> {
            Intent R = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(R);
            finish();
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = String.valueOf(uname.getText());
                password = String.valueOf(pass.getText());
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://10.0.2.2:8080/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if(status.equals("success")) {
                                        username = jsonObject.getString("username");
                                        gmail = jsonObject.getString("gmail");
                                        apiKey = jsonObject.getString("apiKey");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged", "true");
                                        editor.putString("username", username);
                                        editor.putString("gmail", gmail);
                                        editor.putString("apiKey", apiKey);
                                        editor.apply();
                                        Intent F = new Intent(getApplicationContext(), FeedPage.class);
                                        startActivity(F);
                                        finish();
                                    } else {
                                        warn.setText(message);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        warn.setText("Error: " + error.toString());
                        Toast.makeText(getApplicationContext(), "Request failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("username", username);
                        paramV.put("password", password);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}