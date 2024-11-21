package com.example.cs361final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText uname, pass;
    private Button loginBTN, registerBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uname = findViewById(R.id.usernameInput);
        pass = findViewById(R.id.passwordInput);
        loginBTN = findViewById(R.id.loginButton);
        registerBTN = findViewById(R.id.registerButton);

        //loginBTN.setOnClickListener(view -> Login());
        registerBTN.setOnClickListener(view -> {
            Intent R = new Intent(MainActivity.this, RegisterPage.class);
            startActivity(R);
            finish();
        });
    }

//    private void Login() {
//        String username = uname.getText().toString();
//        String passw = pass.getText().toString();
//        Log.i("MainActivity", "Username:" + username + " Password:" + passw);
//    }
}