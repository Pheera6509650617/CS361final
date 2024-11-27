package com.example.cs361final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {
    private EditText gmailT, unameT, passwordT, confirmP;
    private TextView warningT;
    Button registerBTN, loginBTN;
    String gmail, uname, password ,CP;
    protected void onCreate(Bundle SaveInstanceState) {
        super.onCreate(SaveInstanceState);
        setContentView(R.layout.register_page);
        warningT = findViewById(R.id.warning2);
        gmailT = findViewById(R.id.gmailRegister);
        unameT = findViewById(R.id.usernameInput2);
        passwordT = findViewById(R.id.passwordInput2);
        registerBTN = findViewById(R.id.RegisterButton);
        loginBTN = findViewById(R.id.loginButton);
        confirmP = findViewById(R.id.confirmPassword);

        loginBTN.setOnClickListener(view -> {
            Intent LL = new Intent(RegisterPage.this, LoginPage.class);
            startActivity(LL);
            finish();
        });

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmail = String.valueOf(gmailT.getText());
                uname = String.valueOf(unameT.getText());
                password = String.valueOf(passwordT.getText());
                CP = String.valueOf(confirmP.getText());

                if(!password.equals(CP)) {
                    warningT.setText(R.string.confirmPass_warn);
                } else {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = "http://10.0.2.2:8080/register.php";
                    Log.d("RegisterPage", "Gmail: " + gmail + ", Username: " + uname + ", Password: " + password);
                    Log.d("RegisterPage", "Request URL: " + url);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.equals("success")) {
                                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                        Intent L = new Intent(getApplicationContext(), LoginPage.class);
                                        startActivity(L);
                                        finish();
                                    } else {
                                        warningT.setText(response);
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            warningT.setText("Error: " + error.toString());
                            Toast.makeText(getApplicationContext(), "Request failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        protected Map<String, String> getParams() {
                            Map<String, String> paramV = new HashMap<>();
                            paramV.put("gmail", gmail);
                            paramV.put("username", uname);
                            paramV.put("password", password);
                            return paramV;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }
}